package com.sensei374121.amey.memoryappfinalproject;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Activity_Login extends AppCompatActivity {

    //setting up the firebase URL
    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";

    //setting up the universal UID
    public static String UID;

    View rootview;
    Firebase ref;
    Firebase.AuthResultHandler authResultHandler;
    String FIREBASE_TAG="FIREBASE_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //setting view for snackbar messages
        rootview = findViewById(R.id.login_root);

        //setting the firebase context
        Firebase.setAndroidContext(this);

        //setting up firebase references
        ref = new Firebase(FIREBASE_URL);

        //setting up the authentication handler
        authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                UID = authData.getUid();
                Snackbar.make(rootview, "User authenticated", Snackbar.LENGTH_SHORT).show();

                //navigate user to his home page
                startActivity(new Intent(getApplicationContext(),Activity_Homepage.class));
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                //Log.d(FIREBASE_TAG, firebaseError.toString());
                Snackbar.make(rootview,firebaseError.toString(),Snackbar.LENGTH_SHORT).show();
            }
        };

    }

    //handling the login authentication
    public void loginSubmitOnClick(View view) {
        //Snackbar.make(rootview,"You pressed login",Snackbar.LENGTH_SHORT).show();
        EditText login = (EditText) findViewById(R.id.email);
        EditText pwd = (EditText) findViewById(R.id.password);
        if (login != null && pwd != null){
            String login_str = String.valueOf(login.getText());
            String pwd_str = String.valueOf(pwd.getText());
            ref.authWithPassword(login_str,pwd_str,authResultHandler);
        }
    }

    //handling new users
    public void createAccountOnClick(View view) {
        startActivity(new Intent(getApplicationContext(),Activity_CreateUser.class));
    }
}
