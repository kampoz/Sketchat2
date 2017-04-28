package com.kampoz.sketchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.kampoz.sketchat.adapter.SubjectsAdapter;
import com.kampoz.sketchat.dialog.AddSubjectDialogFragment;
import com.kampoz.sketchat.dialog.EditSubjectDialogFragment;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;
import com.kampoz.sketchat.realm.SubjectRealm;

import java.util.ArrayList;

/*
 * Created by wasili on 2017-04-18.
 */

public class SubjectsFragment extends Fragment implements AddSubjectDialogFragment.AddSubjectDialogFragmentListener {

    private GroupsFragment.GroupsFragmentListener listener;
    private SubjectsAdapter adapter;
    private MyRandomValuesGenerator generator;
    private Toolbar toolbar;
    private boolean areEditButtonsShown = false;
    private EditSubjectDialogFragment editGroupDialog;
    private AddSubjectDialogFragment addSubjectDialog;
    ArrayList<SubjectRealm> subjectsList = new ArrayList<>();
    SubjectRealm groupRealm = new SubjectRealm();
    private Context context;
    SubjectRealm subjectRealm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_subjects, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvGroupsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        toolbar = (Toolbar) view.findViewById(R.id.subjects_bar);
        toolbar.setTitle("Subjects");

        generator = new MyRandomValuesGenerator();
        adapter = new SubjectsAdapter(generator.generateSubjectsList(30), recyclerView);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public SubjectsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(SubjectsAdapter adapter) {
        this.adapter = adapter;
    }

    //seedowanie tematów
    public void seedSubjectsAndReload(){
        generator = new MyRandomValuesGenerator();
        adapter.getSubjectsList().clear();
        adapter.getSubjectsList().addAll(generator.generateSubjectsList(30));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_subjects, toolbar.getMenu());

        Menu groupsMenu = toolbar.getMenu();
        for (int i = 0; i < groupsMenu.size(); i++) {
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

    public void showEditButtonsAndFabs(boolean areEditButtonsShown){
        adapter.setAreEditButtonsShown(areEditButtonsShown);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_subject) {
            areEditButtonsShown = !areEditButtonsShown;
            showEditButtonsAndFabs(areEditButtonsShown);
            item.setTitle(areEditButtonsShown ?"Back":"Edit");
            return true;
        }
        if(id==R.id.action_new_subject){
            FragmentManager fragmentManager = getFragmentManager();
            addSubjectDialog = new AddSubjectDialogFragment();
            addSubjectDialog.setListener(this);
            addSubjectDialog.setContext(context);
            addSubjectDialog.setCancelable(false);
            addSubjectDialog.show(fragmentManager, "tag");
            return true;
        }
        if (id == R.id.action_renew) {
            subjectRealm = new SubjectRealm();
            subjectsList.clear();
            subjectsList.addAll(groupRealm.getAllfromSubjectRealmSorted());
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Subjects renew", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCancelClickInAddSubject() {

    }

    @Override
    public void onOKClickInAddSubject(String groupName) {

    }
}
