package com.huadin.assetstatistics.utils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.AssetDetailActivity;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.ReaderParams;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.R.attr.absListViewStyle;
import static android.R.attr.tag;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by 华电 on 2017/7/24.
 */

public class RFIDUtils {


  private static String address = "/dev/ttyMT2";
  private static int antportc = 1;//天线口      1表示一天线
  private final SoundPool soundPool;
  private final ReaderParams rParams;
  public   RfidPower mRpower;

  public Handler mHandler = new Handler();
  public Set<String> hashSet = new HashSet<>();

  private volatile  static RFIDUtils instance;//多线程访问
  public  Reader mReader;
  private byte[] rdata;
  private byte[] rpaswd;
  private String stringTag;
  private final SVProgressHUD dialog;
  private  static Context context;
  //单例
  public static RFIDUtils getInstance(Context context){
    RFIDUtils.context = context;

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
    dialog = new SVProgressHUD(context);
  }

  //连接
  public  boolean connect() {

    //RfidPower.PDATYPE PT = RfidPower.PDATYPE.valueOf(4);
    RfidPower.PDATYPE PT=RfidPower.PDATYPE.ALPS_KT45Q;
    mRpower = new RfidPower(PT);

    String s = mRpower.GetDevPath();
    Log.i("RFIDUtils", "connect: " + s);
    boolean blen = mRpower.PowerUp();
    Log.i("RFIDUtils", "上电: " + blen);
    boolean connect = isConnect();
    return connect;
    /*if(connect){
      MyApplication.connectSuccess = true;
      Toast.makeText(MyApplication.getContext(), "连接成功", Toast.LENGTH_SHORT).show();
    }else{
      MyApplication.connectSuccess = false;
      Toast.makeText(MyApplication.getContext(), "连接失败", Toast.LENGTH_SHORT).show();
    }*/
  }

  public void connectAsync(){
    new ConnectAsyncTask().execute();
  }
  class ConnectAsyncTask extends AsyncTask<Void,Void,Boolean>{

    @Override
    protected Boolean doInBackground(Void... params) {

      return connect();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
      super.onPostExecute(aBoolean);

      if (aBoolean){

        MyApplication.connectSuccess = true;
        Toast.makeText(MyApplication.getContext(), "连接成功", Toast.LENGTH_SHORT).show();
      }else{
        MyApplication.connectSuccess = false;
        dialog.showErrorWithStatus("连接失败");
      }

    }
  }

  //是否连接
  public boolean isConnect(){
    Reader.READER_ERR er = mReader.InitReader_Notype(address, antportc);//"一天线"
    Log.i("RFIDUtils", "er: " + er);
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
  public void readData(Context context){
    this.context = context;

    if(!MyApplication.connectSuccess){
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

      if(tag.length > 0){
        Toast.makeText(MyApplication.getContext(), tag[0], Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(context, AssetDetailActivity.class);
        context.startActivity(intent);
      }

     // mReader.postDelayed(this, rParams.sleep);
    }
  };

public void readAsync(String tag){
  MyAsycnTask myAsycnTask = new MyAsycnTask();
  myAsycnTask.execute(tag);
}
  public boolean readOneByOne(String tag){
    if(!MyApplication.connectSuccess){
      connect();
    }

    try {
      rParams.opant=1;
      rdata = new byte[12];
      rpaswd = new byte[4];


      Reader.READER_ERR er= Reader.READER_ERR.MT_OK_ERR;
      int trycount=1;
      do{
        er=mReader.GetTagData(rParams.opant, (char)1,2,6, rdata, rpaswd,(short)rParams.optime);

        trycount--;
        if(trycount<1)
          break;
      }while(er!= Reader.READER_ERR.MT_OK_ERR);

      if(er== Reader.READER_ERR.MT_OK_ERR)
      {
        /*String val="";
        char[] out=null;

         out=new char[rdata.length*2];
          mReader.Hex2Str(rdata, rdata.length, out);
          val=String.valueOf(out);
        soundPool.play(1, 1, 1, 0, 0, 1);
        Toast.makeText(MyApplication.getContext(), "成功:"+val,
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(context, AssetDetailActivity.class);
        intent.putExtra("tag",tag);
        intent.putExtra("result",val);
        context.startActivity(intent);*/

        return true;
      }
      else{
        /*
        Toast.makeText(MyApplication.getContext(), "失败:"+er.toString(),
                Toast.LENGTH_SHORT).show();*/
        return false;
      }
    } catch (Exception e) {
      return false;
     /* Toast.makeText(MyApplication.getContext(),e.toString(),Toast.LENGTH_SHORT).show();*/
    }
  }


  class MyAsycnTask extends AsyncTask<String,Void,Boolean>{

    @Override
    protected Boolean doInBackground(String... params) {
      stringTag = params[0];
      return readOneByOne(stringTag);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
      if(aBoolean){
        String val="";
        char[] out=null;

        out=new char[rdata.length*2];
        mReader.Hex2Str(rdata, rdata.length, out);
        val=String.valueOf(out);
        soundPool.play(1, 1, 1, 0, 0, 1);
        Toast.makeText(MyApplication.getContext(), "成功:"+val,
                Toast.LENGTH_SHORT).show();

        val = val.substring(val.length()-5);

        Intent intent = new Intent(context, AssetDetailActivity.class);
        intent.putExtra("tag",stringTag);
        intent.putExtra("result",val);
        context.startActivity(intent);

      }else{
        dialog.showErrorWithStatus("读取失败");
      }
      super.onPostExecute(aBoolean);

    }
  }


  public void readBatch(){
    mHandler.postDelayed(runnable_MainActivity,0);
  }
  private Runnable runnable_MainActivity = new Runnable() {
    public void run() {

      String[] tag = null;

      int[] tagcnt = new int[1];

      tagcnt[0] = 0;

      synchronized (this) {
        Reader.READER_ERR er = mReader.TagInventory_Raw
                (rParams.uants, rParams.uants.length,
                        (short) rParams.readtime, tagcnt);

        Log.d("MYINFO", "read:" + er.toString() + " cnt:" + String.valueOf(tagcnt[0]));

        if (er == Reader.READER_ERR.MT_OK_ERR) {
          if (tagcnt[0] > 0) {
            //扫描了多少个
           // tv_once.setText(String.valueOf(tagcnt[0]));

            soundPool.play(1, 1, 1, 0, 0, 1);
            tag = new String[tagcnt[0]];

            for (int i = 0; i < tagcnt[0]; i++) {
              Reader.TAGINFO tfs = mReader.new TAGINFO();
              if (mRpower.GetType() == RfidPower.PDATYPE.SCAN_ALPS_ANDROID_CUIUS2) {
                try {
                  Thread.sleep(10);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
              er = mReader.GetNextTag(tfs);
              Log.d("MYINFO", "get tag index:" + String.valueOf(i) + " er:" + er.toString());
              if (er == Reader.READER_ERR.MT_HARDWARE_ALERT_ERR_BY_TOO_MANY_RESET) {

                /*tv_state.setText("error:" + String.valueOf(er.value()) + er.toString());
                myapp.needreconnect = true;
                button_stop.performClick();
                autostop = true;*/
              }


              if (er == Reader.READER_ERR.MT_OK_ERR) {
                tag[i] = Reader.bytes_Hexstr(tfs.EpcId);
                hashSet.add(tag[i]);
              } else
                break;
            }
          }

        } else {
         // tv_state.setText("error:" + String.valueOf(er.value()) + " " + er.toString());
          if (er == Reader.READER_ERR.MT_HARDWARE_ALERT_ERR_BY_TOO_MANY_RESET) {
            /*tv_state.setText("error:" + String.valueOf(er.value()) + er.toString());
            myapp.needreconnect = true;
            button_stop.performClick();
            autostop = true;*/
          } else
            mHandler.postDelayed(this, rParams.sleep);
          return;

        }
      }



      //*读大量标签不显示
      if (tagcnt[0] > 0) {
        Log.i("geshu", "run: " + hashSet.size());


      }

      mHandler.postDelayed(this, rParams.sleep);
    }
  };
}
