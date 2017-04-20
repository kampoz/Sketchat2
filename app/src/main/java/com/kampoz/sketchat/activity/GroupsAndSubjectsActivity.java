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
import android.view.View;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.fragments.SubjectsFragment;
import com.kampoz.sketchat.fragments.GroupsFragment;

public class GroupsAndSubjectsActivity extends AppCompatActivity implements
        GroupsFragment.GroupsFragmentListener {

    private boolean isLand = false;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment currentFragment = null;
    private Toolbar toolbar;
    private boolean areRadioButtonsShown = false;
    private FloatingActionButton fabDeleteGroups;

    // for Land only
    private SubjectsFragment subjectsFragment;
    private GroupsFragment groupsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_and_subjects);

        fabDeleteGroups = (FloatingActionButton)findViewById(R.id.fabDeleteGroups);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.activity_groups_list_toolbar_title);
        setSupportActionBar(toolbar);

        this.isLand = getResources().getBoolean(R.bool.isLand);

                // w trybie portrait dodajemy do kontenera GroupsFragment
        if (this.isLand) {
            subjectsFragment = (SubjectsFragment) getSupportFragmentManager().findFragmentById(R.id.fSubjectsFragment);
            groupsFragment = (GroupsFragment)getSupportFragmentManager().findFragmentById(R.id.fGroupsFragment);
        }else{
            setGroupsFragment();
        }
    }

    @Override
    public void onItemSelected(int position) {
        if(isLand) {
            subjectsFragment.seedSubjectsAndReload();
        }
        else{
            setSubjectsFragment();
            this.fragmentManager.executePendingTransactions();
        }
    }

    private void setGroupsFragment() {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        //this.currentFragment = new GroupsFragment();
        groupsFragment = new GroupsFragment();
        fragmentTransaction.replace(R.id.fragment_container, groupsFragment);
        fragmentTransaction.commit();
    }

    private void setSubjectsFragment() {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        this.currentFragment = new SubjectsFragment();
        fragmentTransaction.replace(R.id.fragment_container, this.currentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isLand){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_groups, menu);
        }
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

        if (id == R.id.action_delete_groups) {
            areRadioButtonsShown = !areRadioButtonsShown;
            groupsFragment.showRadioButtonsAndHideButtons(areRadioButtonsShown);
        }

        if (id == R.id.action_about) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Copyright \u00a9 2017\nKamil Poznakowski\nkampoznak@gmail.com");
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
