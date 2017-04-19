package com.kampoz.sketchat.fragments;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvGroupsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        
        MyRandomValuesGenerator generator = new MyRandomValuesGenerator();

        adapter = new GroupsListAdapter(generator.generateGroupsList(30), recyclerView);
        adapter.setOnGroupItemSelectedListener(new GroupsListAdapter.OnGroupItemSelectedListener() {
            @Override
            public void onItemSelect(int position) {
                listener.onItemSelected(position);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    // interfejs, który będzie implementować aktywność
    public interface GroupsFragmentListener {
        void onItemSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //if (context instanceof GroupsFragmentListener) {
        try{
            listener = (GroupsFragmentListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement GroupsFragmentListener!!!!!");
        }
//        } else {
//            throw new ClassCastException( context.toString() + " musi implementować interfejs " +
//                    "GroupsFragment.GroupsFragmentListener");
//        }
    }

    // metoda wysyła dane do aktywności
    public void updateDetail(String msg) {
        //listener.onItemSelected();
    }
}
