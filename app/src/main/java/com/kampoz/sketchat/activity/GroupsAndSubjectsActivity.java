package com.kampoz.sketchat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import com.kampoz.sketchat.BuildConfig;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.fragments.SubjectsFragment;
import com.kampoz.sketchat.fragments.GroupsFragment;
import com.kampoz.sketchat.helper.MyConnectionChecker;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;

public class GroupsAndSubjectsActivity extends AppCompatActivity implements
    GroupsFragment.FragmentListener,
    SubjectsFragment.FragmentListener {

  private boolean isLand = false;
  private final FragmentManager fragmentManager = getSupportFragmentManager();
  private Fragment currentFragment = null;
  private Toolbar toolbar;
  //private boolean areRadioButtonsShown = false;
  //private FloatingActionButton fabDeleteGroups;
  // for Land only
  private SubjectsFragment subjectsFragment;
  private GroupsFragment groupsFragment;
  private MyConnectionChecker myConnectionChecker;
  private int mCurrentGroupId = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_groups_and_subjects);
    toolbar = (Toolbar) findViewById(R.id.app_bar);
    toolbar.setTitle(R.string.activity_groups_list_toolbar_title);
    setSupportActionBar(toolbar);
    this.isLand = getResources().getBoolean(R.bool.isLand);
    setGroupsFragment();
    myConnectionChecker = new MyConnectionChecker();
    Log.d("Cykl życia", "...onCreate()...");

    if (savedInstanceState != null) {
      mCurrentGroupId = savedInstanceState.getInt("GROUP_ID");
      if (mCurrentGroupId != 0) {
        onGroupItemSelected(mCurrentGroupId);
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d("Cykl życia", "...onDestroy()...");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.d("Cykl życia", "...onStop()...");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d("Cykl życia", "...onStart()...");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d("Cykl życia", "...onResume()...");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d("Cykl życia", "...onPause()...");
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    mCurrentGroupId = 0;
    Log.d("Cykl życia", "...onBackPressed()...");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d("Cykl życia", "...onRestart()...");
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putInt("GROUP_ID", mCurrentGroupId);
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
    if (id == R.id.action_last_conversation) {
      Intent startLastConversationActivityIntent = new Intent(this, ConversationActivity.class);
      this.startActivity(startLastConversationActivityIntent);
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
    //this.currentFragment = new GroupsFragment();
    groupsFragment = new GroupsFragment();
    fragmentTransaction.replace(R.id.fl_subjects_and_groups_container, groupsFragment);
    fragmentTransaction.commit();
  }

  /**
   * Metoda ta zamienia fragmenty we FrameLayoucie, a ponadto: ma I sposób uzyskanie referencji do
   * Fragmentu - w tym przypadku Subjectfragmentu II sposób jest w GroupsFragent met.
   * onAttach(Context context)
   */
  private void setSubjectsFragment(int groupId) {
    FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
    this.currentFragment = new SubjectsFragment();
    ((SubjectsFragment) this.currentFragment).setGroupId(groupId);
    ((SubjectsFragment) this.currentFragment).setListener(this);
    fragmentTransaction.replace(R.id.fl_subjects_and_groups_container, this.currentFragment);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

  /*** Interfaces methods overwrited: **/
  /***
   * interface GroupsFragment.FragmentListener
   **/
  @Override
  public void onGroupItemSelected(int groupId) {
    mCurrentGroupId = groupId;
    setSubjectsFragment(groupId);
    this.fragmentManager.executePendingTransactions();
  }
  /** end of interface methods */

  /***
   * interface SubjectsFragment.FragmentListener
   **/
  @Override
  public void onSubjectItemSelected(long currentSubjectid) {
    // Zrobić metodę odpalającą Activity z rysowaniem synchronicznym - zdefiniowac w tej klasie i odpalić tutaj
    Toast.makeText(this, "You selected subject with id: " + currentSubjectid, Toast.LENGTH_LONG).show();
    Intent startDrawActivityIntent = new Intent(this, DrawActivity.class);
    startDrawActivityIntent.putExtra("currentSubjectid", currentSubjectid);
    this.startActivity(startDrawActivityIntent);
  }
  /** end of interface methods */
}
