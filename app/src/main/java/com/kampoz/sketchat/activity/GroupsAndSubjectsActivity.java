package com.kampoz.sketchat.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.fragments.SubjectsFragment;
import com.kampoz.sketchat.fragments.GroupsFragment;

public class GroupsAndSubjectsActivity extends AppCompatActivity implements
        GroupsFragment.GroupsFragmentListener {

    private boolean isLand = false;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment currentFragment = null;
    private Toolbar toolbar;

    // for Land only
    private SubjectsFragment subjectsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_and_subjects);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.activity_groups_list_toolbar_title);
        setSupportActionBar(toolbar);

        this.isLand = getResources().getBoolean(R.bool.isLand);

                // w trybie portrait dodajemy do kontenera GroupsFragment
        if (this.isLand) {
            subjectsFragment = (SubjectsFragment) getSupportFragmentManager().findFragmentById(R.id.fSubjectsFragment);
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
        this.currentFragment = new GroupsFragment();
        fragmentTransaction.replace(R.id.fragment_container, this.currentFragment);
        fragmentTransaction.commit();
    }

    private void setSubjectsFragment() {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        this.currentFragment = new SubjectsFragment();
        fragmentTransaction.replace(R.id.fragment_container, this.currentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
