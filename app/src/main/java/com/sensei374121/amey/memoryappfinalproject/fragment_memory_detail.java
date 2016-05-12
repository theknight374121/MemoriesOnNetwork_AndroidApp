package com.sensei374121.amey.memoryappfinalproject;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by urvip on 4/27/2016.
 */
public class fragment_memory_detail extends Fragment {

    private static String ARG_SECTION_NUMBER, MEMORY_ID;
    private OnFragmentInteractionListener mListener;
    public List<String> mylist;
    private String UID_str ;
    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";
    Firebase ref;
    private String s_mem_name,s_mem_street_name,s_mem_addr,s_mem_lat,s_mem_long,s_mem_desc,s_share;
    private List mem_pics;
    TextView mem_name_txtview, mem_name_place,mem_description;
    GoogleMap googleMap;
    //this is for google api client
    private GoogleApiClient mlocationClient;
    private LocationListener mlocationListener;
    private Marker marker;
    //this is android marshmallow
    String PERMISSIONS_NEEDED[] = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    //to check if proper version of google play services is present
    private static final int ERROR_DIALOG_REQUEST = 9001;
    View rootView;


    public fragment_memory_detail(){    }

    public static fragment_memory_detail newInstance(String sectionNumber, String UID) {
        fragment_memory_detail fragment = new fragment_memory_detail();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("UID", UID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //save the state of the fragment
        setRetainInstance(true);

        //get the memory ID sent from below
        if (getArguments() != null) {
            MEMORY_ID = getArguments().getString(ARG_SECTION_NUMBER);
            UID_str = getArguments().getString("UID");
        }

        if (UID_str == null){
            //set the UID for this transaction
            UID_str = Activity_Login.UID;
        }

        //set the initial firebase reference
        ref = new Firebase(FIREBASE_URL);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_memory_detail, container, false);

        //this set of code will be need to populate the layout with the values we have
        mem_name_txtview = (TextView) rootView.findViewById(R.id.moviedetail_txtview_title);
        mem_name_place = (TextView) rootView.findViewById(R.id.moviedetail_txtview_place);
        mem_description = (TextView) rootView.findViewById(R.id.moviedetail_txtview_description);

        //fetch the required strings of the images
        Firebase newref = ref.child("USERS").child(UID_str).child("MEMORIES").child(MEMORY_ID);
        newref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                POJO_MemoryDetail post = snapshot.getValue(POJO_MemoryDetail.class);
                s_mem_name = post.getMem_name();
                s_mem_street_name = post.getMem_place_name();
                s_mem_addr = post.getMem_place_street_address();
                s_mem_lat = post.getLatitude();
                s_mem_long = post.getLongitude();
                s_mem_desc=post.getDescription();
                s_share=post.getShare();
                mem_pics=post.getImages();

                //converting string to double
                Double latitude = Double.parseDouble(s_mem_lat);
                Double longitude = Double.parseDouble(s_mem_long);

                //Aftr you get all the values then execute this
                //TODO make a function for this after wards
                if (mem_name_place != null && mem_name_txtview != null && mem_description != null){
                    mem_name_txtview.setText(s_mem_name);
                    mem_name_place.setText(s_mem_street_name);
                    mem_description.setText(s_mem_desc);
                }

//                //this code is to check if the google Map services are present in the phone
//                if (checkPlayServices()){
//                    SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
//                    googleMap = mapFragment.getMap();
//                    gotoLocation(latitude,longitude,15);
//                    LatLng latLng = new LatLng(latitude,longitude);
//                    MarkerOptions options = new MarkerOptions()
//                            .position(latLng);
//                    //this is to add the marker on the screen.
//                    googleMap.addMarker(options);
//                }else{
//                    Snackbar.make(rootView,"Map not supported on device. Install Google Play Services",Snackbar.LENGTH_SHORT).show();
//                }

                //this piece of code is required to fill in the horizontail scrollview with all the images we need
                final int noOfImgs = mem_pics.size();
                Log.d("SIZE", String.valueOf(noOfImgs));
                LinearLayout imgGrid = (LinearLayout) rootView.findViewById(R.id.moviedetail_linearlayout);
                ImageView imageView = (ImageView)rootView.findViewById(R.id.moviedetail_gridlayout_imgview);
                ViewGroup.LayoutParams layoutParams =  imageView.getLayoutParams();
                imageView.setVisibility(View.GONE);

                for(int i=0; i<noOfImgs; i++)
                {
                    ImageView img = new ImageView(getContext());
                    Picasso.with(getContext()).load((String) mem_pics.get(i)).into(img);
                    img.setLayoutParams(layoutParams);
                    imgGrid.addView(img);
                }

                imgGrid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.openFragment(mem_pics);
                    }
                });

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getContext(), "Event cancelled in fragment memory detail", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    //this function is being used now
    public boolean checkPlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, ERROR_DIALOG_REQUEST)
                        .show();
            } else {
                Log.i("PLAY_SERVICES_ERROR", "This device is not supported");
                getActivity().finish();
            }
            return false;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latlng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, zoom);

        //if you are setting initial value use movecamera method
        googleMap.moveCamera(update);

        //if you are changing locations on map then use animate camera. For eg, moving from one location to another, Zooming
        //googleMap.animateCamera(update);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void openFragment(List<String> mylist);
    }
}
