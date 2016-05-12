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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Activity_ViewProfile extends AppCompatActivity implements fragment_profileView.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{

    Fragment fragment;
    private String UID ;
    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";
    Firebase ref;
    public static final String SAVE_FRAGMENT = "SAVED_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting up firebase reference and listeners
        ref = new Firebase(FIREBASE_URL);

        UID= Activity_Login.UID;
        //Toast.makeText(Activity_ViewProfile.this, "UID is:"+UID, Toast.LENGTH_SHORT).show();

        if (savedInstanceState != null){
            fragment=getSupportFragmentManager().findFragmentByTag(SAVE_FRAGMENT);
        }else{
            fragment = fragment_profileView.newInstance(UID,getApplicationContext());
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profileact_maincontainer, fragment,SAVE_FRAGMENT)
                .commit();

        getnavbardetails();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_memories) {
            startActivity(new Intent(getApplicationContext(),Activity_Memories.class));

        } else if (id == R.id.search_users) {
            startActivity(new Intent(getApplicationContext(),Activity_SearchUsers.class));

        } else if (id == R.id.friend_requests) {
            startActivity(new Intent(getApplicationContext(),Activity_FriendRequestRV.class));

        } else if (id == R.id.my_contacts) {
            startActivity(new Intent(getApplicationContext(),Activity_ContactsRV.class));

        } else if (id == R.id.my_profile) {
            startActivity(new Intent(getApplicationContext(),Activity_ViewProfile.class));

        }else  if (id == R.id.homepage){
            startActivity(new Intent(getApplicationContext(),Activity_Homepage.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onCHangeProfileClicked(String UID) {

    }
    public void getnavbardetails(){
        Firebase newref = new Firebase(FIREBASE_URL).child("USERS").child(UID).child("BIO");
        newref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                POJO_UserBIO userBIO = dataSnapshot.getValue(POJO_UserBIO.class);
                String name = userBIO.getName();
                String email = userBIO.getEmail();
                String profilepic = userBIO.getProfilepic();
                if (name != null && email != null && profilepic != null) {
                    TextView name_txt, email_txt;
                    ImageView pic;
                    name_txt = (TextView) findViewById(R.id.navbar_name);
                    email_txt = (TextView) findViewById(R.id.navbar_email);
                    pic = (ImageView) findViewById(R.id.navbar_imageView);
                    if (name_txt != null && email_txt != null && pic != null) {
                        name_txt.setText(name);
                        email_txt.setText(email);
                        Picasso.with(getApplicationContext()).load(profilepic).into(pic);
                    } else {
                        Log.d("ERROR", "there was an error in loading the nav bar due to on screen items");
                    }
                } else {
                    Log.d("ERROR", "there was an eror fetchin gitemes form firebase");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }
}
