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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.adapter.ConversationAdapter;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;

public class ConversationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ConversationAdapter adapter;
    private EditText etToWriteMessage;
    private Button bSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.activity_conversation_toolbar_title);
        setSupportActionBar(toolbar);

        etToWriteMessage = (EditText)findViewById(R.id.etToWriteMessage);
        bSend = (Button)findViewById(R.id.bSend);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvConversation);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRandomValuesGenerator generator = new MyRandomValuesGenerator();

        adapter = new ConversationAdapter(generator.generateMessagesArrayList(30), recyclerView);
        recyclerView.setAdapter(adapter);

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*oprogramowac button bSend*/
            }
        });
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
