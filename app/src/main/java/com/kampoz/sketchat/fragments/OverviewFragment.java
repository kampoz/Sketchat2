package com.kampoz.sketchat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kampoz.sketchat.R;

/**
 * Created by wasili on 2017-04-18.
 */

public class OverviewFragment extends Fragment {

    private OverviewFragmentActivityListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // przypisujemy layout do fragmentu
        View view = inflater.inflate(R.layout.fragment_overview, container,
                false);
        /*
        // definiujemy listener dla poszczególnych elementów (buttonów)
        View.OnClickListener clickListener = new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button1:
                        updateDetail("Szczegółowe informacje o elemencie pierwszym.");
                        break;
                    case R.id.button2:
                        updateDetail("Szczegółowe informacje o elemencie drugim.");
                        break;
                    default:
                        break;
                }
            }
        };

        // przypisujemy elementom clickListener
        Button button1 = (Button) view.findViewById(R.id.button1);
        Button button2 = (Button) view.findViewById(R.id.button2);

        button1.setOnClickListener(clickListener);
        button2.setOnClickListener(clickListener);
        */
        return view;
    }

    // interfejs, który będzie implementować aktywność
    public interface OverviewFragmentActivityListener {
        public void onItemSelected(String msg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OverviewFragmentActivityListener) {
            listener = (OverviewFragmentActivityListener) context;
        } else {
            throw new ClassCastException( context.toString() + " musi implementować interfejs " +
                    "OverviewFragment.OverviewFragmentActivityListener");
        }
    }

    // metoda wysyła dane do aktywności
    public void updateDetail(String msg) {
        listener.onItemSelected(msg);
    }
}
