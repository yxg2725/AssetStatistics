package com.huadin.assetstatistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.huadin.assetstatistics.activity.MainActivity;

/**
 * Created by 华电 on 2017/7/19.
 */

public class BaseFragment extends Fragment {

  public FragmentActivity mActivity;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = getActivity();
  }

}
