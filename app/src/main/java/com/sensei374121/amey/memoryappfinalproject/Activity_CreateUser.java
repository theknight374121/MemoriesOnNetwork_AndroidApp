package com.sensei374121.amey.memoryappfinalproject;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.realtime.util.StringListReader;

import java.util.HashMap;
import java.util.Map;

public class Activity_CreateUser extends AppCompatActivity {

    View rootview;

    //setting up the firebase URL
    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";

    Firebase ref;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        rootview = findViewById(R.id.createaccountview);

        //creating the url
        ref = new Firebase(FIREBASE_URL);

    }

    public void createButton(View view) {
        EditText email = (EditText) findViewById(R.id.createemails);
        EditText pwd = (EditText) findViewById(R.id.pwd);
        EditText pwdc = (EditText) findViewById(R.id.pwdc);
        EditText name = (EditText) findViewById(R.id.name);

        final String email_str = String.valueOf(email.getText());
        String pwd_str = String.valueOf(pwd.getText());
        final String name_str = String.valueOf(name.getText());
        Snackbar.make(rootview,name_str,Snackbar.LENGTH_SHORT).show();

        if (email != null && pwd != null && pwdc != null && name != null){


            ref.createUser(email_str, pwd_str, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> stringObjectMap) {
                    Snackbar.make(rootview,"User created successfully",Snackbar.LENGTH_SHORT).show();

                    //adding data to firebase cloud to users
                    UID = (String) stringObjectMap.get("uid");
                    POJO_UserBIO userBIO = new POJO_UserBIO(name_str,email_str,UID);

                    //adding this data to users detail list
                    //HashMap userBIOHashBasic = userBIO.createUserBIOHashBasic();
                    //ref.child("USERS").child(UID).child("BIO").updateChildren(userBIOHashBasic);

                    //adding this data to list of all users. Brief info
                    HashMap userBIOHashUL = userBIO.createUserBIOHashUserList();
                    ref.child("USERS-LIST").child(UID).updateChildren(userBIOHashUL);

                    //user was successfully added, sending back to login screen
                    startActivity(new Intent(Activity_CreateUser.this,Activity_Login.class));
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Snackbar.make(rootview, firebaseError.toString(), Snackbar.LENGTH_SHORT).show();
                }
            });

        }

    }
}
