package com.huadin.assetstatistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.assetstatistics.activity.MainActivity;

/**
 * Created by 华电 on 2017/7/19.
 */

public class StorageFragment extends BaseFragment {


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    TextView textView = new TextView(mActivity);
    textView.setText("入库统计");
    textView.setTextSize(20);
    textView.setGravity(Gravity.CENTER);
    return textView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((MainActivity)mActivity).mToolbar.setTitle("入库统计");
  }

}
