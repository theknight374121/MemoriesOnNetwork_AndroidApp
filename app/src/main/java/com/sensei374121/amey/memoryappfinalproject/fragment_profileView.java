package com.sensei374121.amey.memoryappfinalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

public class fragment_profileView extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String UID_str;
    private OnFragmentInteractionListener mListener;
    public final String FIREBASE_URL="https://memoryapp.firebaseio.com/";
    Firebase ref;
    private String name_str,email_str,aboutme_str,profilepic,bgpic,gender_str;
    private View rootview;
    Context mcontext;
    TextView name,email,aboutme,gender;
    ImageView bgpic_imgview,profilepic_imgview,cordlayoutimage;

    public fragment_profileView() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static fragment_profileView newInstance(String param1, Context context) {
        fragment_profileView fragment = new fragment_profileView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UID_str = getArguments().getString(ARG_PARAM1);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_profile_view, container, false);

        if (UID_str==null){
            UID_str=Activity_Login.UID;
        }

        name = (TextView) rootview.findViewById(R.id.name);
        email = (TextView) rootview.findViewById(R.id.email);
        gender = (TextView) rootview.findViewById(R.id.gender);
        aboutme = (TextView) rootview.findViewById(R.id.aboutme);
        profilepic_imgview = (ImageView) rootview.findViewById(R.id.profileimage);
        cordlayoutimage = (ImageView) getActivity().findViewById(R.id.cordimage);

        //setting up the firebase reference
        ref = new Firebase(FIREBASE_URL);

        //fetching BIO of the current user.
        Firebase newref = ref.child("USERS").child(UID_str).child("BIO");
        newref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                POJO_UserBIO post = snapshot.getValue(POJO_UserBIO.class);
                name_str=post.getName();
                email_str=post.getEmail();
                aboutme_str=post.getAboutme();
                gender_str=post.getIsGenderFemale();
                profilepic=post.getProfilepic();
                bgpic=post.getBgpic();

                if (name != null && email != null && gender != null && aboutme != null && profilepic_imgview != null){
                    name.setText(name_str);
                    email.setText(email_str);
                    aboutme.setText(aboutme_str);
                    if (gender_str.equalsIgnoreCase("true")){
                        gender.setText("FEMALE");
                    }else {
                        gender.setText("MALE");
                    }
                    Picasso.with(getContext()).load(profilepic).into(profilepic_imgview);
                    Picasso.with(getContext()).load(bgpic).into(cordlayoutimage);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Snackbar.make(rootview,"event cancelled",Snackbar.LENGTH_SHORT).show();
            }
        });

        return rootview;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCHangeProfileClicked(String UID);
    }
}
