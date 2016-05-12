package com.sensei374121.amey.memoryappfinalproject;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Activity_Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FIREBASE_URL = "https://memoryapp.firebaseio.com/";
    Firebase ref;
    private String UID;
    GoogleMap googleMap;
    LatLng displayLocation;
    List<String> frndUIDStack;


    //to check if proper version of google play services is present
    private static final int ERROR_DIALOG_REQUEST = 9001;
    //this is android marshmallow
    String PERMISSIONS_NEEDED[] = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting the UID from Login Page
        UID = Activity_Login.UID;
        //Toast.makeText(Activity_Homepage.this, "UID is:"+UID, Toast.LENGTH_SHORT).show();

        ref = new Firebase(FIREBASE_URL);

        frndUIDStack = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Homepage.this,Activity_NewMemory.class));
            }
        });

//        //this profram runs the maps
//        LatLng testLatLng = new La;
//        gotoLocation(testLatLng.latitude,testLatLng.longitude,10);
        runMapForOurOwnMemories();
        runMapforFrndsMemories();
        getnavbardetails();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                if(name != null && email != null && profilepic != null){
                    TextView name_txt, email_txt;
                    ImageView pic;
                    name_txt = (TextView) findViewById(R.id.navbar_name);
                    email_txt = (TextView) findViewById(R.id.navbar_email);
                    pic = (ImageView) findViewById(R.id.navbar_imageView);
                    if (name_txt != null && email_txt != null && pic != null){
                        name_txt.setText(name);
                        email_txt.setText(email);
                        Picasso.with(getApplicationContext()).load(profilepic).into(pic);
                    }else{
                        Log.d("ERROR", "there was an error in loading the nav bar due to on screen items");
                    }
                }else{
                    Log.d("ERROR", "there was an eror fetchin gitemes form firebase");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //this function is to get co ordinates of our own memories
    public LatLng runMapForOurOwnMemories(){
        if (checkPlayServices()){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = mapFragment.getMap();

            Firebase newref = ref.child("USERS").child(UID).child("MEMORIES");
            newref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    POJO_MemoryDetail memoryDetail = dataSnapshot.getValue(POJO_MemoryDetail.class);
                    String title = memoryDetail.getMem_name();
                    String placeName = memoryDetail.getMem_place_name();
                    Double latitude = Double.parseDouble(memoryDetail.getLatitude());
                    Double longitude = Double.parseDouble(memoryDetail.getLongitude());
                    MarkerOptions options = new MarkerOptions()
                            .title(title)
                            .snippet(placeName)      //this is what will be displayed when its clicked on it
                            .position(new LatLng(latitude,longitude))     //this is the position where the marker will be placed
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)); //this is used to give color to marker
                    googleMap.addMarker(options);
                    displayLocation = new LatLng(latitude, longitude);
                    gotoLocation(latitude,longitude,5);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //Nothing is going to happen in our case
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        return displayLocation;
    }

    //this function to get UID of our friends
    public void runMapforFrndsMemories(){

        Firebase newref = ref.child("USERS").child(UID).child("CONTACTS");
        newref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                POJO_ContactsRVDisplay contactsRVDisplay = dataSnapshot.getValue(POJO_ContactsRVDisplay.class);
                frndUIDStack.add(contactsRVDisplay.getUID());
                fetchFrndsMemoryLocation(contactsRVDisplay.getUID());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void fetchFrndsMemoryLocation(String uid) {
        Firebase newref = ref.child("USERS").child(uid).child("MEMORIES");
        newref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                POJO_MemoryDetail memoryDetail = dataSnapshot.getValue(POJO_MemoryDetail.class);
                if (memoryDetail.getShare().endsWith("Public")){
                    String title = memoryDetail.getMem_name();
                    String placeName = memoryDetail.getMem_place_name();
                    Double latitude = Double.parseDouble(memoryDetail.getLatitude());
                    Double longitude = Double.parseDouble(memoryDetail.getLongitude());
                    MarkerOptions options = new MarkerOptions()
                            .title(title)
                            .snippet(placeName)      //this is what will be displayed when its clicked on it
                            .position(new LatLng(latitude,longitude))     //this is the position where the marker will be placed
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)); //this is used to give color to marker
                    googleMap.addMarker(options);
                    displayLocation = new LatLng(latitude, longitude);
                    gotoLocation(latitude,longitude,5);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        } else  if (id == R.id.homepage){
            startActivity(new Intent(getApplicationContext(),Activity_Homepage.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latlng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, zoom);
        //if you are setting initial value use movecamera method
        //googleMap.moveCamera(update);

        //if you are changing locations on map then use animate camera. For eg, moving from one location to another, Zooming
        googleMap.animateCamera(update);
    }

    //this function is being used now
    public boolean checkPlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, ERROR_DIALOG_REQUEST)
                        .show();
            } else {
                Log.i("PLAY_SERVICES_ERROR", "This device is not supported");
                finish();
            }
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ref.unauth();
    }
}
