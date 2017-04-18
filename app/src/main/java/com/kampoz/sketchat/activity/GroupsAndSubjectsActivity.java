package com.kampoz.sketchat.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.fragments.SubjectsFragment;
import com.kampoz.sketchat.fragments.GroupsFragment;

public class GroupsAndSubjectsActivity extends AppCompatActivity implements
        GroupsFragment.GroupsFragmentListener {

    private boolean isLand = false;
    private final FragmentManager fragmentManager = getFragmentManager();
    private Fragment currentFragment = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments_one);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.activity_groups_list_toolbar_title);
        setSupportActionBar(toolbar);

        this.isLand = getResources().getBoolean(R.bool.isLand);

        // w trybie portrait dodajemy do kontenera GroupsFragment
        if (!this.isLand) {
            setOverviewFragment();
        }
    }

//    @Override
//    public void onItemSelected(String msg) {
//        SubjectsFragment subjectsFragment = (SubjectsFragment) getFragmentManager()
//                .findFragmentById(R.id.detailFragment);
//    }

    private void setOverviewFragment() {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        this.currentFragment = new GroupsFragment();
        fragmentTransaction.replace(R.id.fragment_container, this.currentFragment);
        fragmentTransaction.commit();
    }

    private void setDetailsFragment() {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        this.currentFragment = new SubjectsFragment();
        ft.replace(R.id.fragment_container, this.currentFragment);

        // dodajemy transakcję na stos
        // dzięki temu możemy wrócić przyciskiem BACK
        ft.addToBackStack(null);

        // zatwierdzamy transakcję
        ft.commit();
    }

    @Override
    public void onItemSelected() {

    }
}
