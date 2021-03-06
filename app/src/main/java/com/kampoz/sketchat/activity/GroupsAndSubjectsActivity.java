package com.kampoz.sketchat.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.kampoz.sketchat.BuildConfig;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.dao.GroupDao;
import com.kampoz.sketchat.dao.SubjectDao;
import com.kampoz.sketchat.dao.UserRealmLocalDao;
import com.kampoz.sketchat.dao.UserRealmSyncDao;
import com.kampoz.sketchat.fragments.GroupsFragment;
import com.kampoz.sketchat.fragments.SubjectsFragment;
import com.kampoz.sketchat.helper.MyConnectionChecker;
import com.kampoz.sketchat.realm.GroupRealm;
import com.kampoz.sketchat.realm.SubjectRealm;
import com.kampoz.sketchat.realm.UserRealmLocal;
import io.realm.Realm;
import java.util.ArrayList;

public class GroupsAndSubjectsActivity extends AppCompatActivity implements
        GroupsFragment.FragmentListener,
        SubjectsFragment.FragmentListener {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private boolean isLand = false;
    private Fragment currentFragment = null;
    private Toolbar toolbar;
    //private boolean areRadioButtonsShown = false;
    //private FloatingActionButton fabDeleteGroups;
    // for Land only
    private SubjectsFragment subjectsFragment;
    private GroupsFragment groupsFragment;
    private MyConnectionChecker myConnectionChecker;
    private long mCurrentGroupId = 0;
    private String tag = "cz G&SA";
    private String backStackTag = "backStack entries: ";
    private String tagGlobalInstances = "Realm global inst. G&FA";
    private boolean isThreadActive = true;
    private String threadTag = "G&SA thread";
    private GroupDao groupDao;
    private SubjectDao subjectDao;
    UserRealmLocalDao userDaoLocal;
    UserRealmSyncDao userDaoSync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_and_subjects);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.activity_groups_list_toolbar_title);
        setSupportActionBar(toolbar);
        this.isLand = getResources().getBoolean(R.bool.isLand);
        currentFragment = fragmentManager.findFragmentById(R.id.fl_subjects_and_groups_container);
        setGroupsFragment();
        myConnectionChecker = new MyConnectionChecker();

        //Realm.getDefaultInstance();
        Realm.setDefaultConfiguration(SplashActivity.publicSyncConfiguration);

        groupDao = new GroupDao();
        subjectDao = new SubjectDao();
        userDaoLocal = new UserRealmLocalDao();
        userDaoSync = new UserRealmSyncDao();

        Log.d("Cykl życia", "...onCreate()...");
        Log.d(backStackTag, "...onCreate()..getSupportFragmentManager().getBackStackEntryCount()" + fragmentManager.getBackStackEntryCount());
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            Log.d(backStackTag, "Found fragment id: " + fragmentManager.getBackStackEntryAt(entry).getId());
        }

        if (savedInstanceState != null) {
            mCurrentGroupId = savedInstanceState.getLong("GROUP_ID");
            if (mCurrentGroupId != 0) {
                onGroupItemSelected(mCurrentGroupId);
            }
        }

        GettingActualGroupsThread thread = new GettingActualGroupsThread();
        thread.start();

        //SyncConfiguration syncConfiguration = Realm.get
        /*Log.d("SyncConfiguration",
            "..1)getRealmFileName() " + syncConfiguration.getRealmFileName());
        Log.d("SyncConfiguration",
            "..2)getRealmDirectory() " + syncConfiguration.getRealmDirectory().toString());
        Log.d("SyncConfiguration", "..3)getPath() " + syncConfiguration.getPath());
        Log.d("SyncConfiguration", "..4)getUser() " + syncConfiguration.getUser());
        Log.d("SyncConfiguration", "..5)getServerUrl() " + syncConfiguration.getServerUrl());
        Log.d("SyncConfiguration",
            "..6)getRealmObjectClasses() " + syncConfiguration.getRealmObjectClasses());*/
    }

    @Override
    protected void onDestroy() {
        isThreadActive = false;
        groupDao.closeRealmInstance();
        subjectDao.closeRealmInstance();
        userDaoLocal.closeRealmInstance();
        userDaoSync.closeRealmInstance();

        Log.d(tag, "...onDestroy()...");
        Log.d(tagGlobalInstances, "onDestroy(); Realm.getGlobalInstanceCount()" + String.valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
        Log.d(tagGlobalInstances, "-------------- closing app -------------");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(tag, "...onStop()...");
        Log.d(tagGlobalInstances, "onStop(); Realm.getGlobalInstanceCount()" + String.valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "...onStart()...");
        Log.d(tagGlobalInstances, "onStart(); Realm.getGlobalInstanceCount()" + String.valueOf(
            Realm.getGlobalInstanceCount(
                SplashActivity.publicSyncConfiguration)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tag, "...onResume()...");
        Log.d(tagGlobalInstances, "onResume() ; Realm.getGlobalInstanceCount()" + String.valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(tag, "...onPause()...");
        Log.d(tagGlobalInstances, "onPause(); Realm.getGlobalInstanceCount()" + String.valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
    }

    @Override
    public void onBackPressed() {
        mCurrentGroupId = 0;
        if (currentFragment instanceof SubjectsFragment){
            currentFragment = groupsFragment;
        }
        Log.d(tagGlobalInstances, "onBackPressed(); Realm.getGlobalInstanceCount()" + String.valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(tag, "...onRestart()...");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("GROUP_ID", mCurrentGroupId);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_groups_and_subjects, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_register) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                    this);
            alertDialogBuilder.setMessage("Action Register");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        if (id == R.id.action_login) {
//            Intent startSettingsActivityIntent = new Intent(this, SettingsActivity.class);
//            this.startActivity(startSettingsActivityIntent);
//            this.finish();
        }
        if (id == R.id.action_logout) {
            userDaoLocal.logoutCurrentUser();
            Intent startRegisterIntent = new Intent(this, LoginAndRegisterActivity.class);
            this.startActivity(startRegisterIntent);
            this.finish();
            Toast.makeText(this, "User logout", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.action_draw_activity) {
            if (!myConnectionChecker.isOnline(this)) {
                Toast.makeText(this, "No connection", Toast.LENGTH_LONG).show();
            } else {
                Intent startDrawActivityIntent = new Intent(this, DrawActivity.class);
                this.startActivity(startDrawActivityIntent);
            }
        }
        if (id == R.id.action_about) {
            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                    this);
            alertDialogBuilder.setMessage(versionName + " " + versionCode
                    + "\nCopyright \u00a9 2017\nKamil Poznakowski\nkampoznak@gmail.com");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setGroupsFragment() {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        this.currentFragment = new GroupsFragment();
        groupsFragment = (GroupsFragment) currentFragment;
        fragmentTransaction.replace(R.id.fl_subjects_and_groups_container, this.currentFragment, "GROUPS_FRAGMENT");
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Metoda ta zamienia fragmenty we FrameLayoucie, a ponadto: ma I sposób uzyskanie referencji do
     * Fragmentu - w tym przypadku Subjectfragmentu II sposób jest w GroupsFragent met.
     * onAttach(Context context)
     */
    private void setSubjectsFragment(long groupId) {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        this.currentFragment = new SubjectsFragment();
        ((SubjectsFragment) this.currentFragment).setGroupId(groupId);
        ((SubjectsFragment) this.currentFragment).setListener(this);
        fragmentTransaction.replace(R.id.fl_subjects_and_groups_container, this.currentFragment, "SUBJECT_FRAGMENT");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*** Interfaces methods overwrited: **/
    /***
     * interface GroupsFragment.FragmentListener
     **/
    @Override
    public void onGroupItemSelected(long groupId) {
        mCurrentGroupId = groupId;
        setSubjectsFragment(groupId);
        this.fragmentManager.executePendingTransactions();
    }

    @Override
    public void addUserToGroup(final GroupRealm groupRealm){
        final Builder alert = new AlertDialog.Builder(GroupsAndSubjectsActivity.this);
        alert.setMessage("Do you want to join group "+ groupRealm.getGroupName()+" ?");
        alert.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserRealmLocal userRealmLocal = userDaoLocal.getCurrentLoginUser();
                userDaoSync.addingUserToGroupAndGroupToUser(userRealmLocal.getId(), groupRealm.getId());
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    /** end of interface methods */

    /*** interface SubjectsFragment.FragmentListener **/
    @Override
    public void onSubjectItemSelected(long currentSubjectid) {
        // Zrobić metodę odpalającą Activity z rysowaniem synchronicznym - zdefiniowac w tej klasie i odpalić tutaj
        Toast.makeText(this, "You selected subject with id: " + currentSubjectid, Toast.LENGTH_LONG).show();
        Intent startDrawActivityIntent = new Intent(this, DrawActivity.class);
        startDrawActivityIntent.putExtra("currentSubjectid", currentSubjectid);
        this.startActivity(startDrawActivityIntent);
        //this.finish();
    }

    /**
     * end of interface methods
     */

    class GettingActualGroupsThread extends Thread {
        public void run() {

            while (isThreadActive) {
                if(currentFragment instanceof GroupsFragment) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            GroupDao groupDao = new GroupDao();
                            ArrayList<GroupRealm> threadGroupsList = new ArrayList<>();
                            threadGroupsList.addAll(groupDao.getAllfromGroupRealmSorted());
                            ((GroupsFragment)currentFragment).getGroupsList().clear();
                            ((GroupsFragment)currentFragment).getGroupsList().addAll(threadGroupsList);
                            //if (((GroupsFragment)currentFragment).getAdapter()!=null){
                                ((GroupsFragment)currentFragment).getAdapter().notifyDataSetChanged();
                            //}
                            groupDao.closeRealmInstance();
                            //groupDao = null;
                        }
                    });
                    Log.i(threadTag, "... pobranie grup ...");
                } else if (currentFragment instanceof SubjectsFragment){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GroupDao groupDao = new GroupDao();
                            ArrayList<SubjectRealm> threadSubjectsList = new ArrayList<>();
                            threadSubjectsList.addAll(groupDao.getSubjectsFromGroupSorted(((SubjectsFragment)currentFragment).getGroupId()));
                            ((SubjectsFragment)currentFragment).getSubjectsList().clear();
                            ((SubjectsFragment)currentFragment).getSubjectsList().addAll(threadSubjectsList);
                            ((SubjectsFragment)currentFragment).getAdapter().notifyDataSetChanged();
                            groupDao.closeRealmInstance();
                            //groupDao = null;
                        }
                    });
                    Log.i(threadTag, "... pobranie subjectów ...");
                }
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Log.i(threadTag, "... wątek odświeżający dane działa ...");
            }
        }
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public SubjectDao getSubjectDao() {
        return subjectDao;
    }

    public void setSubjectDao(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }
}
