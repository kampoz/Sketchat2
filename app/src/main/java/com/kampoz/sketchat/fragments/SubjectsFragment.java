package com.kampoz.sketchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.adapter.SubjectsAdapter;
import com.kampoz.sketchat.dialog.AddSubjectDialogFragment;
import com.kampoz.sketchat.dialog.EditSubjectDialogFragment;
import com.kampoz.sketchat.realm.GroupRealm;
import com.kampoz.sketchat.realm.SubjectRealm;

import io.realm.Realm;
import io.realm.RealmList;
import java.security.acl.Group;
import java.util.ArrayList;

public class SubjectsFragment extends Fragment implements
    AddSubjectDialogFragment.AddSubjectDialogFragmentListener,
    SubjectsAdapter.OnSubjectItemSelectedListener,
    EditSubjectDialogFragment.EditSubjectDialogFragmentListener {

  public interface FragmentListener {
    void onSubjectItemSelected(long id);
  }

  private FragmentListener listener;
  private SubjectsAdapter adapter;
  //private MyRandomValuesGenerator generator;
  private Toolbar toolbar;
  private boolean areEditButtonsShown = false;
  private EditSubjectDialogFragment editSubjectDialog;
  private AddSubjectDialogFragment addSubjectDialog;
  private ArrayList<SubjectRealm> subjectsList = new ArrayList<>();
  private SubjectRealm subjectRealm = new SubjectRealm();
  private GroupRealm groupRealm = new GroupRealm();
  private int groupId;
  private Context context;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_subjects, container, false);
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvSubjectsList);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    toolbar = (Toolbar) view.findViewById(R.id.subjects_bar);
    toolbar.setTitle("Group: "+GroupRealm.getGroupNameForId(groupId));

    subjectRealm = new SubjectRealm();
    subjectsList.clear();
    subjectsList.addAll(GroupRealm.getSubjectsFromGroupSorted(groupId));

//    if(subjectsFromThisGroupList!=null)
//    subjectsList.addAll(groupRealm.getSubjectsForGroup(groupId));

    adapter = new SubjectsAdapter(subjectsList, recyclerView);
    adapter.setOnSubjectItemSelectedListener(this);
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
//    public void seedSubjectsAndReload(){
//        generator = new MyRandomValuesGenerator();
//        adapter.getSubjectsList().clear();
//        adapter.getSubjectsList().addAll(generator.generateSubjectsList(30));
//        adapter.notifyDataSetChanged();
//    }

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

    MenuItem item = toolbar.getMenu().findItem(R.id.search);
    SearchView searchView = new SearchView(getActivity());
    MenuItemCompat.setShowAsAction(item,
        MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
    MenuItemCompat.setActionView(item, searchView);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
//        subjectsList.clear();
//        subjectsList.addAll(GroupRealm.getSubjectsFromGroupSorted(groupId));
//        adapter.notifyDataSetChanged();
        return false;
      }
      @Override
      public boolean onQueryTextChange(String newText) {
        subjectRealm = new SubjectRealm();
        subjectsList.clear();
        subjectsList.addAll(subjectRealm.searchELementsByName(newText, groupId));
        adapter.notifyDataSetChanged();
        return false;
      }
    });

  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      listener = (FragmentListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implements FragmentListener!!!!!");
    }
  }

  public void showEditButtonsAndFabs(boolean areEditButtonsShown) {
    adapter.setAreEditButtonsShown(areEditButtonsShown);
    adapter.notifyDataSetChanged();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_edit_subject) {
      areEditButtonsShown = !areEditButtonsShown;
      showEditButtonsAndFabs(areEditButtonsShown);
      item.setTitle(areEditButtonsShown ? "Back" : "Edit");
      return true;
    }
    if (id == R.id.action_new_subject) {
      FragmentManager fragmentManager = getFragmentManager();
      addSubjectDialog = new AddSubjectDialogFragment();
      addSubjectDialog.setListener(this);
      addSubjectDialog.setContext(context);
      addSubjectDialog.setCancelable(false);
      addSubjectDialog.setIdOfGroupToAddSubject(groupId);
      addSubjectDialog.show(fragmentManager, "tag");
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  /*** Interfaces methods: ***/

  /*** 1) From interfece SubjectsAdapter.OnSubjectItemSelectedListener (2 methods)**/
  @Override
  public void onItemSelect(long id) {
    listener.onSubjectItemSelected(id);
  }

  @Override
  public void onEditItem(SubjectRealm subjectRealm) {
    Toast.makeText(getContext(), "Edit subject: " + subjectRealm.getSubject(), Toast.LENGTH_SHORT)
        .show();
    FragmentManager fragmentManager = getFragmentManager();
    editSubjectDialog = new EditSubjectDialogFragment();
    editSubjectDialog.setEditSubjectDialogFragmentListener(this);
    editSubjectDialog.setSubjectRealmToEdit(subjectRealm);
    editSubjectDialog.setContext(context);
    editSubjectDialog.setCancelable(false);
    editSubjectDialog.setGroupId(groupId);
    editSubjectDialog.show(fragmentManager, "edit subject");
  }
  /** End of interfece SubjectsAdapter.OnSubjectItemSelectedListener**/

  /*** 2) From interface AddSubjectDialogFragment.AddSubjectDialogFragmentListener (1 method)**/
  @Override
  public void onOKClickInAddSubject(String subjectName, int groupId) {
    subjectRealm = new SubjectRealm();
    subjectsList.clear();
    subjectsList.addAll(GroupRealm.getSubjectsFromGroupSorted(groupId));
    adapter.notifyDataSetChanged();
    Toast.makeText(getContext(), "Subject added: " + subjectName, Toast.LENGTH_SHORT).show();
  }
  /** End of interface AddSubjectDialogFragment.AddSubjectDialogFragmentListener */

  /*** 3) From Interface EditSubjectDialogFragment.EditSubjectDialogFragmentListener (3 methods) **/
  @Override
  public void onDeleteSubjectClickInEdit(String subjectName, int groupId) {
    editSubjectDialog.dismiss();
    subjectRealm = new SubjectRealm();
    subjectsList.clear();
    subjectsList.addAll(GroupRealm.getSubjectsFromGroupSorted(editSubjectDialog.getGroupId()));
    adapter.notifyDataSetChanged();
    Toast.makeText(getContext(), "Subject deleted: " + subjectName, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onCancelClickInEdit() {
    editSubjectDialog.dismiss();
  }

  @Override
  public void onOKClickInEdit() {
    editSubjectDialog.dismiss();
    subjectsList.clear();
    subjectsList.addAll(GroupRealm.getSubjectsFromGroupSorted(editSubjectDialog.getGroupId()));
    adapter.notifyDataSetChanged();
    Toast.makeText(getContext(), "Name changed", Toast.LENGTH_SHORT).show();
  }
  /** End of Interface EditSubjectDialogFragment.EditSubjectDialogFragmentListener**/

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
}
