package com.kampoz.sketchat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.UserRealm;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class RegisterActivity extends AppCompatActivity {

    private static final String REALM_URL = "realm://" + "100.0.0.21" + ":9080/~/Test";
    private static final String AUTH_URL = "http://" + "100.0.0.21" + ":9080/auth";
    private static final String ID = "kampoz@kaseka.net";
    private static final String PASSWORD = "Murzyn1!";

    private Button bOKinRegistration;
    private EditText etUsernameInRegistration;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsernameInRegistration = (EditText)findViewById(R.id.etUsernameInRegistration);
        bOKinRegistration = (Button) findViewById(R.id.bOKinRegistration);

        final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
        SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL).build();
                Log.d("SyncConfiguration", syncConfiguration.getRealmFileName());
                Realm.setDefaultConfiguration(syncConfiguration);
                realm = Realm.getDefaultInstance();
            }

            @Override
            public void onError(ObjectServerError error) {
                Log.d("Error", "To wina wasyla");
            }
        });

        bOKinRegistration.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String userName = etUsernameInRegistration.getText().toString();
                if(TextUtils.isEmpty(userName)) {
                    etUsernameInRegistration.setError("Username cannot be empty");
                }
                else{

                    final UserRealm userRealm = new UserRealm();
                    userRealm.setName(userName);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(userRealm);
                        }
                    });
                    realm.close();
                    //intent
                }
            }
        });
    }


}
