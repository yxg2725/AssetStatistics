package com.huadin.assetstatistics.utils;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.ReaderParams;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.Reader;

import static android.content.ContentValues.TAG;

/**
 * Created by 华电 on 2017/7/24.
 */

public class RFIDUtils {


  private static String address = "/dev/ttyMT2";
  private static int antportc = 1;//天线口      1表示一天线
  private final SoundPool soundPool;
  private final ReaderParams rParams;
  private  RfidPower mRpower;

  private Handler mHandler = new Handler();


  private volatile  static RFIDUtils instance;//多线程访问
  private  Reader mReader;

  //单例
  public static RFIDUtils getInstance(){
    if (instance==null){
      synchronized (RFIDUtils.class){
        if (instance==null){
          instance = new RFIDUtils();
        }
      }
    }
    return instance;
  }

  //构造
  public RFIDUtils(){
    mReader = new Reader();
    rParams = new ReaderParams();

    //初始化声音线程池
    soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
    soundPool.load(MyApplication.getContext(), R.raw.beep51, 1);
  }

  //连接
  public  void connect() {

    RfidPower.PDATYPE PT = RfidPower.PDATYPE.valueOf(6);
    if(mRpower == null){
      mRpower = new RfidPower(PT);
    }

    //String s = mRpower.GetDevPath();
    boolean blen = mRpower.PowerUp();
    boolean connect = isConnect();
    if(connect){
      Toast.makeText(MyApplication.getContext(), "连接成功", Toast.LENGTH_SHORT).show();
    }else{
      Toast.makeText(MyApplication.getContext(), "连接失败", Toast.LENGTH_SHORT).show();
    }
  }

  //是否连接
  public boolean isConnect(){
    Reader.READER_ERR er = mReader.InitReader_Notype(address, antportc);//"一天线"
    if (er == Reader.READER_ERR.MT_OK_ERR) {
      return true;
    } else {
      return false;
    }
  }

  //断开连接
  public  void disConnect() {
    if (mReader != null){
      mReader.CloseReader();
    }
    boolean blen = mRpower.PowerDown();
    Toast.makeText(MyApplication.getContext(), "断开读写器，下电：" + String.valueOf(blen), Toast.LENGTH_SHORT).show();
  }

  //读取数据
  public void readData(){

    if(!isConnect()){
      connect();
    }

    mHandler.postDelayed(runnable,500);

  }

  private Runnable runnable = new Runnable() {
    public void run() {

      String[] tag = null;

      int[] tagcnt = new int[1];

      tagcnt[0] = 0;//表示读取的不同的标签个数

      synchronized (this) {
        Reader.READER_ERR er = mReader.TagInventory_Raw
                (rParams.uants, rParams.uants.length,
                        (short) rParams.readtime, tagcnt);

        //etLog.append("read:" + er.toString() + " cnt:" + String.valueOf(tagcnt[0]) + "\r\n");

        if (er == Reader.READER_ERR.MT_OK_ERR) {
          if (tagcnt[0] > 0) {

            soundPool.play(1, 1, 1, 0, 0, 1);
            tag = new String[tagcnt[0]];//创建一个String数组

            for (int i = 0; i < tagcnt[0]; i++) {
              Reader.TAGINFO tfs = mReader.new TAGINFO();

              er = mReader.GetNextTag(tfs);

              if (er == Reader.READER_ERR.MT_HARDWARE_ALERT_ERR_BY_TOO_MANY_RESET) {
                disConnect();
              }

              if (er == Reader.READER_ERR.MT_OK_ERR) {
                tag[i] = Reader.bytes_Hexstr(tfs.EpcId);

                Toast.makeText(MyApplication.getContext(), tag[i], Toast.LENGTH_SHORT).show();
              }
            }
          }

        } else {
          if (er == Reader.READER_ERR.MT_HARDWARE_ALERT_ERR_BY_TOO_MANY_RESET) {
            disConnect();
          } else{
           mHandler.postDelayed(this, rParams.sleep);
          }
          return;

        }
      }

      /*if (tagcnt[0] > 0) {
        etResult.setText("");
        showlist();
      }*/

     // mReader.postDelayed(this, rParams.sleep);
    }
  };
}
