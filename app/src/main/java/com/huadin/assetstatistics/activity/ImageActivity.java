package com.huadin.assetstatistics.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.app.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ImageActivity extends AppCompatActivity {

  @BindView(R.id.pv)
  PhotoView pv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image);
    ButterKnife.bind(this);

    //隐藏状态栏
    pv.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

    Intent intent = getIntent();
    String imgPath = intent.getStringExtra("imgPath");
    MyApplication.showImageView(imgPath, pv);
  }
}
