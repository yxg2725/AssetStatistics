package com.huadin.assetstatistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.MainActivity;

/**
 * Created by 华电 on 2017/7/19.
 */

public class SettingFragment extends BaseFragment {


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_set,null);
    return view;
  }
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((MainActivity)mActivity).mToolbar.setTitle("设置");
  }
}