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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.List;

public class Activity_Memories extends AppCompatActivity implements fragment_memorylist_RV.OnFragmentInteractionListener, fragment_memory_detail.OnFragmentInteractionListener {

    private String UID ;
    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";
    public static final String SAVE_FRAGMENT = "SAVED_FRAGMENT";
    Firebase ref;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__memories);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //Setting up firebase reference and listeners
        ref = new Firebase(FIREBASE_URL);

        UID=Activity_Login.UID;
        //Toast.makeText(Activity_Memories.this, "UID is:"+UID, Toast.LENGTH_SHORT).show();

        if (savedInstanceState != null){
            fragment = getSupportFragmentManager().findFragmentByTag(SAVE_FRAGMENT);
        }else{
            fragment = fragment_memorylist_RV.newInstance(UID);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_maincontainer, fragment,SAVE_FRAGMENT)
                .commit();


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    //this function opens the particular memeory clicked
    @Override
    public void onMemoryDetailClicked(String memkey) {
        fragment = fragment_memory_detail.newInstance(memkey,null);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_maincontainer,fragment,SAVE_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    //this function opens the fragment that contains the viewpager code
    @Override
    public void openFragment(List<String> mylist) {
        fragment = fragment_open_viewpagercode.newInstance(mylist);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_maincontainer,fragment,SAVE_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
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

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.my_memories) {
//            startActivity(new Intent(getApplicationContext(),Activity_Memories.class));
//
//        } else if (id == R.id.search_users) {
//            startActivity(new Intent(getApplicationContext(),Activity_SearchUsers.class));
//
//        } else if (id == R.id.friend_requests) {
//            startActivity(new Intent(getApplicationContext(),Activity_FriendRequestRV.class));
//
//        } else if (id == R.id.my_contacts) {
//            startActivity(new Intent(getApplicationContext(),Activity_ContactsRV.class));
//
//        } else if (id == R.id.my_profile) {
//            startActivity(new Intent(getApplicationContext(),Activity_ViewProfile.class));
//
//        }else  if (id == R.id.homepage){
//            startActivity(new Intent(getApplicationContext(),Activity_Homepage.class));
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//
}
