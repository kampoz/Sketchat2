package com.kampoz.sketchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

public class GroupsFragment extends Fragment implements GroupsListAdapter.OnGroupItemSelectedListener {

    private GroupsFragmentListener listener;
    private GroupsListAdapter adapter;
    private FloatingActionButton fabDeleteGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvGroupsList);
        fabDeleteGroups = (FloatingActionButton)view.findViewById(R.id.fabDeleteGroups);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        MyRandomValuesGenerator generator = new MyRandomValuesGenerator();

        adapter = new GroupsListAdapter(generator.generateGroupsList(30), recyclerView);
        adapter.setOnGroupItemSelectedListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemSelect(int position) {
        listener.onItemSelected(position);
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
            throw new ClassCastException(context.toString()+" must implements GroupsFragmentListener!!!!!");
        }
    }

    public void showRadioButtonsAndHideButtons(boolean areRadioButtonsShown){
        adapter.setAreRadioButtonsShown(areRadioButtonsShown);
        adapter.notifyDataSetChanged();
        if((fabDeleteGroups.getVisibility())==View.VISIBLE)
        fabDeleteGroups.setVisibility(View.INVISIBLE);
        else
            fabDeleteGroups.setVisibility(View.VISIBLE); ;
    }
}
