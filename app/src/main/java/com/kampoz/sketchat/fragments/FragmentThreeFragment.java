package com.kampoz.sketchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kampoz.sketchat.R;

/**
 * Created by wasili on 2017-05-04.
 */

public class FragmentThreeFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_three, container, false);
    return view;
  }
}
