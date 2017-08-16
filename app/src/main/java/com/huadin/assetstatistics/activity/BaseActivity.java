package com.huadin.assetstatistics.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.fragment.OutAndEnterFragment;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DialogUtils;
import com.huadin.assetstatistics.utils.KT50_B2.RFIDUtils2;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.huadin.assetstatistics.utils.SharedPreferenceUtils;
import com.huadin.assetstatistics.utils.ToastUtils;

import static android.R.attr.keycode;
import static android.R.attr.tag;
import static android.view.KeyEvent.KEYCODE_F4;
import static android.view.KeyEvent.KEYCODE_F9;

/**
 * Created by admin on 2017/7/19.
 */

public class BaseActivity extends AppCompatActivity {
    long exittime = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initToolbar(Toolbar toolbar,String title, boolean b) {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(b);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("onKeyDown", "onKeyDown: "+ keyCode);
       // if(keyCode == KeyEvent.KEYCODE_F9 ){//KEYCODE_F4
        if(keyCode == KeyEvent.KEYCODE_F4 ){
            if(!MyApplication.connectSuccess ){
//                RFIDUtils.getInstance(this).connectAsync();
                RFIDUtils2.getInstance(this).connect();
            }else{
                Intent intent = new Intent(this, BatchScanActivity.class);
                startActivity(intent);
            }



            /*Fragment fragment = ((MainActivity) this).getSupportFragmentManager().findFragmentById(R.id.fl_container);
            String simpleName = fragment.getClass().getSimpleName();
            if(simpleName.equals("InventoryAssetsFragment")) {
                RFIDUtils.getInstance(this).readAsync("InventoryAssetsFragment");
            }else{
                SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(this);
                boolean isBatchScan = sharedPreferenceUtils.getBoolean(Contants.PATCH_SCAN, false);
                Log.i("isBatchScan", "isBatchScan: " + isBatchScan);

                if(!isBatchScan){//逐一扫描
                    String fromTag = ((OutAndEnterFragment) fragment).getFromTag();
                    RFIDUtils.getInstance(this).readAsync(fromTag);

                }else{//批量扫描
                    Intent intent = new Intent(this, BatchScanActivity.class);
                    startActivity(intent);
                }

            }*/


        }
        return super.onKeyDown(keyCode, event);
    }
}
