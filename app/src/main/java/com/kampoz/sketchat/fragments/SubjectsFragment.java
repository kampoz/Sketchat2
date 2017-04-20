package com.kampoz.sketchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.adapter.SubjectsListActivityAdapter;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;

/**
 * Created by wasili on 2017-04-18.
 */

public class SubjectsFragment extends Fragment {

    private GroupsFragment.GroupsFragmentListener listener;
    private SubjectsListActivityAdapter adapter;
    MyRandomValuesGenerator generator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_subjects, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvGroupsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        generator = new MyRandomValuesGenerator();
        adapter = new SubjectsListActivityAdapter(generator.generateSubjectsList(30), recyclerView);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public SubjectsListActivityAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(SubjectsListActivityAdapter adapter) {
        this.adapter = adapter;
    }

    //seedowanie tematów
    public void seedSubjectsAndReload(){
        generator = new MyRandomValuesGenerator();
        adapter.getSubjectsList().clear();
        adapter.getSubjectsList().addAll(generator.generateSubjectsList(30));
        adapter.notifyDataSetChanged();
    }


}
