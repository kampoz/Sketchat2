package com.kampoz.sketchat.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.adapter.GroupsListAdapter;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;

/**
 * Created by wasili on 2017-04-18.
 */

public class GroupsFragment extends Fragment {

    private GroupsFragmentListener listener;
    private GroupsListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // przypisujemy layout do fragmentu
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvGroupsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));



        MyRandomValuesGenerator generator = new MyRandomValuesGenerator();

        adapter = new GroupsListAdapter(generator.generateGroupsList(30), recyclerView);
        recyclerView.setAdapter(adapter);

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
    public interface GroupsFragmentListener {
        void onItemSelected();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupsFragmentListener) {
            listener = (GroupsFragmentListener) context;
        } else {
            throw new ClassCastException( context.toString() + " musi implementować interfejs " +
                    "GroupsFragment.GroupsFragmentListener");
        }
    }

    // metoda wysyła dane do aktywności
    public void updateDetail(String msg) {
        //listener.onItemSelected();
    }
}
