package com.sensei374121.amey.memoryappfinalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.List;

public class Activity_FriendRequestRV extends AppCompatActivity implements fragment_friendreq_RV.OnFragmentInteractionListener,fragment_UserDetail.OnFragmentInteractionListener, fragment_memory_detail.OnFragmentInteractionListener {

    private String UID ;
    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";
    Firebase ref;
    Fragment fragment;
    public static final String SAVE_FRAGMENT = "SAVED_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_rv);

        //Setting up firebase reference and listeners
        ref = new Firebase(FIREBASE_URL);

        UID= Activity_Login.UID;
        //Toast.makeText(Activity_FriendRequestRV.this, "UID is:"+UID, Toast.LENGTH_SHORT).show();

        if (savedInstanceState != null){
            fragment = getSupportFragmentManager().findFragmentByTag(SAVE_FRAGMENT);
        }else{
            fragment = fragment_friendreq_RV.newInstance(UID);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frndreqact_maincontainer, fragment,SAVE_FRAGMENT)
                .commit();

    }

    @Override
    public void onFrndRequestClicked(String uid) {
        //this will open up another activity
        fragment = fragment_UserDetail.newInstance(uid,"Public");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frndreqact_maincontainer, fragment,SAVE_FRAGMENT)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onMemoryDetailClicked(String memkey, String useruid) {
        fragment = fragment_memory_detail.newInstance(memkey,null);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frndreqact_maincontainer,fragment,SAVE_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openFragment(List<String> mylist) {
        fragment = fragment_open_viewpagercode.newInstance(mylist);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frndreqact_maincontainer,fragment,SAVE_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),Activity_UpdateProfile.class));
        }
        if (id==R.id.logout){
            ref.unauth();
            startActivity(new Intent(getApplicationContext(),Activity_Login.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
