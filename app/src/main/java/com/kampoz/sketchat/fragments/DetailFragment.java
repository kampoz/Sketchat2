package com.kampoz.sketchat.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kampoz.sketchat.R;

/**
 * Created by wasili on 2017-04-18.
 */

public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_detail, container, false);
        return view;
    }


    public void setText(String txt) {
//        TextView view = (TextView) getView().findViewById(R.id.detailsText);
//        view.setText(txt);
    }
}
