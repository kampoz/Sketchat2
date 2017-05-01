package com.kampoz.sketchat.activity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kampoz.sketchat.BuildConfig;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.fragments.SubjectsFragment;
import com.kampoz.sketchat.fragments.GroupsFragment;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;

public class GroupsAndSubjectsActivity extends AppCompatActivity implements
        GroupsFragment.FragmentListener,
        SubjectsFragment.FragmentListener{

    private boolean isLand = false;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment currentFragment = null;
    private Toolbar toolbar;
    private boolean areRadioButtonsShown = false;
    private FloatingActionButton fabDeleteGroups;

    MyRandomValuesGenerator generator = new MyRandomValuesGenerator();

    // for Land only
    private SubjectsFragment subjectsFragment;
    private GroupsFragment groupsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_and_subjects);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.activity_groups_list_toolbar_title);
        setSupportActionBar(toolbar);

        /**  Generowanie elementów do GroupRealm  */
        //generator.generateGroupsList(4);
        //generator.generateSubjectsList(5);

        this.isLand = getResources().getBoolean(R.bool.isLand);
        setGroupsFragment();
    }


        //z interfejsu GroupsFragment.FragmentListener
    @Override
    public void onGroupItemSelected(int position) {
            setSubjectsFragment();
            this.fragmentManager.executePendingTransactions();
    }

        //z interfejsu SubjectsFragment.FragmentListener
    @Override
    public void onSubjectItemSelected(int position) {

    }

    private void setGroupsFragment() {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        //this.currentFragment = new GroupsFragment();
        groupsFragment = new GroupsFragment();
        fragmentTransaction.replace(R.id.fl_subjects_and_groups_container, groupsFragment);
        fragmentTransaction.commit();
    }

    private void setSubjectsFragment() {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        this.currentFragment = new SubjectsFragment();
        fragmentTransaction.replace(R.id.fl_subjects_and_groups_container, this.currentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
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

        if (id == R.id.action_last_conversation) {
        }

        if (id == R.id.action_settings) {
        }


        if (id == R.id.action_about) {
            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(versionName+" "+versionCode+"\nCopyright \u00a9 2017\nKamil Poznakowski\nkampoznak@gmail.com");
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
}
