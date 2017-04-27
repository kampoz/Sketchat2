package com.kampoz.sketchat.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.activity.GroupsAndSubjectsActivity;
import com.kampoz.sketchat.adapter.GroupsListAdapter;
import com.kampoz.sketchat.dialog.AddGroupDialogFragment;
import com.kampoz.sketchat.dialog.EditGroupDialogFragment;
import com.kampoz.sketchat.realm.GroupRealm;

import java.util.ArrayList;

public class GroupsFragment extends Fragment implements
        GroupsListAdapter.OnGroupItemSelectedListener,
        EditGroupDialogFragment.EditGroupDialogFragmentListener,
        AddGroupDialogFragment.AddGroupDialogFragmentListener{

    private GroupsFragmentListener listener;
    private GroupsListAdapter adapter;
    //private FloatingActionButton fabDeleteGroups;
    private FloatingActionButton fabCancel;
    private Toolbar toolbar;
    private boolean areRadioButtonsShown = false;
    private Context context;
    private EditGroupDialogFragment editGroupDialog;
    private AddGroupDialogFragment addGroupDialog;
    ArrayList<GroupRealm> groupsList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvGroupsList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        toolbar = (Toolbar) view.findViewById(R.id.groups_bar);
        toolbar.setTitle("Groups");



            //pobranie danych z Realm i przekazanie ich do adaptera
        GroupRealm groupRealm = new GroupRealm();
        groupsList.clear();
        groupsList.addAll(groupRealm.getAllfromGroupRealmSorted());
            //drugi sposob pobrania wszystkiego z GroupRealm
        //groupsList.addAll(Realm.getDefaultInstance().where(GroupRealm.class).findAll());

        adapter = new GroupsListAdapter(groupsList, recyclerView);
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

        MenuItem item = toolbar.getMenu().findItem(R.id.search);
        SearchView searchView = new SearchView(getActivity());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Toast.makeText(getContext(), "searchView Listener", Toast.LENGTH_SHORT).show();
                                          }
                                      }
        );

//        SearchView searchView = new SearchView(((GroupsAndSubjectsActivity)context).getSupportActionBar().getThemedContext());
//        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
//        MenuItemCompat.setActionView(item, searchView);

//        SearchManager searchManager =
//                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getActivity().getComponentName()));
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
        Toast.makeText(getContext(), "Edit group: "+groupRealm.getGroupName(), Toast.LENGTH_SHORT).show();
        FragmentManager fragmentManager = getFragmentManager();
        editGroupDialog = new EditGroupDialogFragment();
        editGroupDialog.setEditGroupDialogFragmentListener(this);
        editGroupDialog.setGroupRealmToEdit(groupRealm);
        editGroupDialog.setContext(context);
        editGroupDialog.setCancelable(false);
        editGroupDialog.show(fragmentManager, "tag");
    }

    @Override
    public void onDeleteGroupClick(String groupName) {
        editGroupDialog.dismiss();
        GroupRealm groupRealm = new GroupRealm();
        groupsList.clear();
        groupsList.addAll(groupRealm.getAllfromGroupRealmSorted());
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Group deleted: "+groupName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelClick() {
        editGroupDialog.dismiss();
    }

    @Override
    public void onOKclick() {
        editGroupDialog.dismiss();
        adapter.notifyDataSetChanged();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_groups) {
            areRadioButtonsShown = !areRadioButtonsShown;
            showEditButtonsAndFabs(areRadioButtonsShown);
            item.setTitle(areRadioButtonsShown?"Back":"Edit");
            return true;
        }
        if(id==R.id.action_new_group){
            FragmentManager fragmentManager = getFragmentManager();
            addGroupDialog = new AddGroupDialogFragment();
            addGroupDialog.setListener(this);
            addGroupDialog.setContext(context);
            addGroupDialog.setCancelable(false);
            addGroupDialog.show(fragmentManager, "tag");
            return true;
        }
        if (id == R.id.action_renew) {
            GroupRealm groupRealm = new GroupRealm();
            groupsList.clear();
            groupsList.addAll(groupRealm.getAllfromGroupRealmSorted());
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Groups renew", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCancelClickInAddGroup() {

    }

    @Override
    public void onOKClickInAddGroup(String groupName) {
        GroupRealm groupRealm = new GroupRealm();
        groupsList.clear();
        groupsList.addAll(groupRealm.getAllfromGroupRealmSorted());
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Group added: "+groupName, Toast.LENGTH_SHORT).show();
    }
}
