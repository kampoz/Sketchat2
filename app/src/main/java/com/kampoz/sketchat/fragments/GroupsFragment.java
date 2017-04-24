package com.kampoz.sketchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.adapter.GroupsListAdapter;
import com.kampoz.sketchat.dialog.EditGroupDialogFragment;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;
import com.kampoz.sketchat.realm.GroupRealm;

public class GroupsFragment extends Fragment implements GroupsListAdapter.OnGroupItemSelectedListener {

    private GroupsFragmentListener listener;
    private GroupsListAdapter adapter;
    //private FloatingActionButton fabDeleteGroups;
    private FloatingActionButton fabCancel;
    private Toolbar toolbar;
    private boolean areRadioButtonsShown = false;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvGroupsList);
        //fabDeleteGroups = (FloatingActionButton)view.findViewById(R.id.fabDeleteGroups);
        fabCancel = (FloatingActionButton)view.findViewById(R.id.fabCancel);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        MyRandomValuesGenerator generator = new MyRandomValuesGenerator();

        toolbar = (Toolbar) view.findViewById(R.id.groups_bar);
        toolbar.setTitle("Groups");

        adapter = new GroupsListAdapter(generator.generateGroupsList(30), recyclerView);
        adapter.setOnGroupItemSelectedListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_groups, toolbar.getMenu());

        Menu groupsMenu = toolbar.getMenu();
        for(int i=0; i<groupsMenu.size(); i++) {
            MenuItem item = groupsMenu.getItem(i);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onOptionsItemSelected(item);
                    return false;
                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onItemSelect(int position) {
        listener.onItemSelected(position);
    }

    @Override
    public void onEditItem(GroupRealm groupRealm) {
        Toast.makeText(getContext(), "Group "+groupRealm.getGroupName()+" edit", Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = getFragmentManager();
        EditGroupDialogFragment myDialog = new EditGroupDialogFragment();
        myDialog.setContext(context);
        myDialog.show(fragmentManager, "tag");
    }

    public interface GroupsFragmentListener {
        void onItemSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (GroupsFragmentListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implements GroupsFragmentListener!!!!!");
        }
    }

    public void showEditButtonsAndFabs(boolean areRadioButtonsShown){
        adapter.setAreEditButtonsShown(areRadioButtonsShown);
        adapter.notifyDataSetChanged();
        if((fabCancel.getVisibility())==View.VISIBLE) {
            fabCancel.setVisibility(View.INVISIBLE);
        }
        else
        {
            fabCancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_groups) {
            areRadioButtonsShown = !areRadioButtonsShown;
            showEditButtonsAndFabs(areRadioButtonsShown);
            //Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
