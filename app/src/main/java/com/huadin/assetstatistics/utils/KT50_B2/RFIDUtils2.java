package com.huadin.assetstatistics.utils.KT50_B2;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.BatchScanActivity;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.ReaderParams;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.uhf.api.cls.Reader;
import com.uhf.structures.St_Inv_Data;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static freemarker.template.EmptyMap.instance;

/**
 * Created by 华电 on 2017/8/10.
 */

public class RFIDUtils2 {

  private static St_Inv_Data[] stInvData;
  private static HashSet<String> hashSet;
  private final SoundPool soundPool;
  private final SVProgressHUD dialog;
  private volatile  static RFIDUtils2 instance;//多线程访问
  private String scan;
  private Context context;
  private ExecutorService fixedThreadPool;

  public RFIDUtils2(Context context){

    //初始化声音线程池
    soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
    soundPool.load(MyApplication.getContext(), R.raw.beep51, 1);
    dialog = new SVProgressHUD(context);

    fixedThreadPool = Executors.newFixedThreadPool(3);

    hashSet = new HashSet<>();

    if(stInvData == null){
      stInvData = new St_Inv_Data[1024];
    }
  }

  public static RFIDUtils2 getInstance(Context context){

    if (instance==null){
      synchronized (RFIDUtils.class){
        if (instance==null){
          instance = new RFIDUtils2(context);
        }
      }
    }
    return instance;
  }

  public  void connect(){
    new ConnectAsynctask().execute();
  }

  private static class InventoryRunnable implements Runnable {
    @Override
    public void run() {
      MyApplication.getLinkage().Inventory(2000, 0, 0);
    }

  }

  class ConnectAsynctask extends AsyncTask<Void,Void,Integer> {
    @Override
    protected Integer doInBackground(Void... voids) {
      return ModuleManager.initLibSO();
    }

    @Override
    protected void onPostExecute(Integer num) {
      super.onPostExecute(num);
      if(num==0){
        MyApplication.connectSuccess = true;
        Toast.makeText(MyApplication.getContext(),"连接成功", Toast.LENGTH_SHORT).show();
      }else{
        MyApplication.connectSuccess = false;
        dialog.showErrorWithStatus("连接失败");
      }
    }
  }

  public  void readData(){
    /*int result = MyApplication.getLinage().open_serial("dev/ttyMT2");
    if(result != 0){

    }*/

    fixedThreadPool.execute(new InventoryRunnable());
    fixedThreadPool.execute(new ReceiveDataRunnable());

    //new Thread().start();

   // new Thread().start();
  }

  private  void receiveDataThread() {
    hashSet.clear();
    try {
        while (MyApplication.canScan) {
          int num = MyApplication.getLinkage().GetInvData(stInvData, 1);
          if ((num > 0) && (stInvData != null)) {
            String strEPCTemp = "";

            for (int i = 0; i < num; i++) {
              if (stInvData[i].nLength > 0 && stInvData[i].nLength < 66) {
                strEPCTemp = MyApplication.getLinkage().b2hexs(stInvData[i].INV_Data,
                        stInvData[i].nLength);
              }
              if(TextUtils.isEmpty(strEPCTemp)||!strEPCTemp.matches("\\d+")||strEPCTemp.length()<8){
                continue;
              }

              strEPCTemp = strEPCTemp.substring(strEPCTemp.length()-8);
              if(hashSet.add(strEPCTemp)){
                soundPool.play(1, 1, 1, 0, 0, 1);
                EventBus.getDefault().post(hashSet);
              }

            }
          }
          Thread.sleep(50);
        }
    } catch (Exception e) {
      MyApplication.canScan = false;
      MyApplication.connectSuccess = false;
      MyApplication.getLinkage().CancelOperation();
      ModuleManager.destroyLibSO();
    }
  }

  private class ReceiveDataRunnable implements Runnable {
    @Override
    public void run() {
      receiveDataThread();
    }
  }
}
