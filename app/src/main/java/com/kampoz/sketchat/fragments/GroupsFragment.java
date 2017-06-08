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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.activity.SplashActivity;
import com.kampoz.sketchat.adapter.GroupsAdapter;
import com.kampoz.sketchat.dao.GroupDao;
import com.kampoz.sketchat.dialog.AddGroupDialogFragment;
import com.kampoz.sketchat.dialog.EditGroupDialogFragment;
import com.kampoz.sketchat.realm.GroupRealm;
import java.util.ArrayList;

import io.realm.Realm;

public class GroupsFragment extends Fragment implements
    GroupsAdapter.OnGroupItemSelectedListener,
    EditGroupDialogFragment.EditGroupDialogFragmentListener,
    AddGroupDialogFragment.AddGroupDialogFragmentListener {

  public interface FragmentListener {
    void onGroupItemSelected(long position);
  }

  private FragmentListener listener;
  private GroupsAdapter adapter;
  //private FloatingActionButton fabDeleteGroups;
  //private FloatingActionButton fabCancel;
  private Toolbar toolbar;
  private boolean areEditButtonsShown = false;
  private EditGroupDialogFragment editGroupDialog;
  private AddGroupDialogFragment addGroupDialog;
  ArrayList<GroupRealm> groupsList = new ArrayList<>();
  GroupRealm groupRealm;
  private Context context;
  private GroupDao groupDao;
  private String tag1 = "realm instance";
  private String tagGlobalInstances = "Realm global inst. GF";

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
    Log.d(tag1,"------------GroupsFragment onCreateView()-------------");
    groupDao = new GroupDao();
    /*** pobranie danych z Realm i przekazanie ich do adaptera */
    groupsList.clear();
    groupsList.addAll(groupDao.getAllfromGroupRealmSorted());
    /*** drugi sposob pobrania wszystkiego z GroupRealm */
    //groupsList.addAll(Realm.getDefaultInstance().where(GroupRealm.class).findAll());
    adapter = new GroupsAdapter(groupsList, recyclerView);
    adapter.setOnGroupItemSelectedListener(this);
    recyclerView.setAdapter(adapter);
    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_groups, toolbar.getMenu());
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
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        groupsList.clear();
        groupsList.addAll(groupDao.searchELementsByName(newText));
        adapter.notifyDataSetChanged();
        return false;
      }
    });
    searchView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                      Toast.makeText(getContext(), "searchView Listener",
                                          Toast.LENGTH_SHORT).show();
                                    }
                                  }
    );
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);
    Log.d(tagGlobalInstances, "Realm.getGlobalInstanceCount(): " + String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
  }

  /**
   * II sposób połaczenia Fragmentu z Activity, I sposób jest w GroupsAndSubjectsActivity - przez
   * setter met. setSubjectsFragment linijka: ((SubjectsFragment)this.currentFragment).setListener(this);
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    Log.d(tagGlobalInstances, "onAttach() Realm.getGlobalInstanceCount(): " + String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
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
    if (id == R.id.action_edit_groups) {
      areEditButtonsShown = !areEditButtonsShown;
      showEditButtonsAndFabs(areEditButtonsShown);
      item.setTitle(areEditButtonsShown ? "Back" : "Edit");
      return true;
    }
    if (id == R.id.action_new_group) {
      FragmentManager fragmentManager = getFragmentManager();
      addGroupDialog = new AddGroupDialogFragment();
      addGroupDialog.setListener(this);
      addGroupDialog.setContext(context);
      addGroupDialog.setCancelable(false);
      addGroupDialog.show(fragmentManager, "tag");
      return true;
    }
    if (id == R.id.action_renew) {
      groupsList.clear();
      groupsList.addAll(groupDao.getAllfromGroupRealmSorted());
      adapter.notifyDataSetChanged();
      Toast.makeText(getContext(), "Groups renew", Toast.LENGTH_SHORT).show();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  /** Zamyka ref. realma, która jest tworzona w konstruktorze RealmDao */
  @Override
  public void onDetach() {
    super.onDetach();
    Log.d(tagGlobalInstances, "onDetach() Realm.getGlobalInstanceCount(): " + String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));

  }

  @Override
  public void onResume() {
    super.onResume();
    if(groupDao == null) {
      groupDao = new GroupDao();
    }

    groupsList.clear();
    groupsList.addAll(groupDao.getAllfromGroupRealmSorted());
    adapter.notifyDataSetChanged();
    Log.d(tag1,"------------GroupsFragment onResume()-------------");
    Log.d(tagGlobalInstances, "onResume() Realm.getGlobalInstanceCount(): " + String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
  }

  @Override
  public void onPause() {
    super.onPause();
    groupDao.closeRealmInstance();
    groupDao = null;
    Log.d(tag1,"------------GroupsFragment onPause()-------------");
    Log.d(tagGlobalInstances, "onPause() Realm.getGlobalInstanceCount(): " + String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
  }


  /************** INTERFACES: *********************/
  /*** 1) From interface GroupsAdapter.OnGroupItemSelectedListener (2 methods)**/
  @Override
  public void onItemSelect(long groupId) {
    listener.onGroupItemSelected(groupId);
  }

  @Override
  public void onEditItem(GroupRealm groupRealm) {
    FragmentManager fragmentManager = getFragmentManager();
    editGroupDialog = new EditGroupDialogFragment();
    editGroupDialog.setEditGroupDialogFragmentListener(this);
    editGroupDialog.setGroupRealmToEdit(groupRealm);
    editGroupDialog.setContext(context);
    editGroupDialog.setCancelable(false);
    editGroupDialog.show(fragmentManager, "tag");
  }
  /** End of interfece GroupsAdapter.OnGroupItemSelectedListener **/

  /***
   * 2) From interface AddGroupDialogFragment.AddGroupDialogFragmentListener (1 method)
   */
  @Override
  public void onOKClickInAddGroup(String groupName) {
    groupsList.clear();
    groupsList.addAll(groupDao.getAllfromGroupRealmSorted());
    adapter.notifyDataSetChanged();
    Toast.makeText(getContext(), "Group added: " + groupName, Toast.LENGTH_SHORT).show();
  }
  /** End of interface'u AddGroupDialogFragment.AddGroupDialogFragmentListener  **/

  /***
   * 3) From interface EditGroupDialogFragment.EditGroupDialogFragmentListener (3 methods)
   **/
  @Override
  public void onDeleteGroupClickInEdit(String groupName) {
    editGroupDialog.dismiss();
    groupsList.clear();
    groupsList.addAll(groupDao.getAllfromGroupRealmSorted());
    adapter.notifyDataSetChanged();
    Toast.makeText(getContext(), "Group deleted: " + groupName, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onCancelClickInEdit() {
    editGroupDialog.dismiss();
  }

  @Override
  public void onOKClickInEdit() {
    editGroupDialog.dismiss();
    adapter.notifyDataSetChanged();
  }
  /** End of interfece EditGroupDialogFragment.EditGroupDialogFragmentListener */
}
