package com.kampoz.sketchat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.adapter.ConversationActivityAdapter;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;
import com.kampoz.sketchat.model.MessageObject;

import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ConversationActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.activity_conversation_toolbar_title);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvConversation);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRandomValuesGenerator generator = new MyRandomValuesGenerator();
        adapter = new ConversationActivityAdapter(generator.generateMessagesArrayList(30), recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_register) {
            Intent startRegisterActivityIntent = new Intent(this, RegisterActivity.class);
            this.startActivity(startRegisterActivityIntent);
            this.finish();
        }

        if (id == R.id.action_login) {
//            Intent startLoginActivityIntent = new Intent(this, LoginActivity.class);
//            this.startActivity(startLoginActivityIntent);
//            this.finish();
        }

        if (id == R.id.action_groups_list) {
            Intent startGroupsListActivityIntent = new Intent(this, GroupsListActivity.class);
            this.startActivity(startGroupsListActivityIntent);
            this.finish();
        }

        if (id == R.id.action_settings) {
//            Intent startSettingsActivityIntent = new Intent(this, SettingsActivity.class);
//            this.startActivity(startSettingsActivityIntent);
//            this.finish();
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
