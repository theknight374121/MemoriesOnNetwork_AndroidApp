package com.sensei374121.amey.memoryappfinalproject;


import com.firebase.client.Firebase;
import com.firebase.client.Logger;

public class FirebaseApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //setting up the firebase so that it could be accessible overall
        Firebase.setAndroidContext(this);

        //setting up the debugger on firebase
        Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);

    }


}
