package com.sensei374121.amey.memoryappfinalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class fragment_contacts_RV extends Fragment {

    private static String ARG_PARAM1 ;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_Contacts adapterContacts;
    private static final String FIREBASE_URL = "https://memoryapp.firebaseio.com/";
    Firebase ref,postref;

    private String UID_str,username,user_mail;


    public fragment_contacts_RV() {
        // Required empty public constructor
    }

    public static fragment_contacts_RV newInstance(String param1) {
        fragment_contacts_RV fragment = new fragment_contacts_RV();
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
        final View rootview;
        rootview = inflater.inflate(R.layout.fragment_contacts_rv, container, false);

        //setting up the firebase adapter
        recyclerView = (RecyclerView) rootview.findViewById(R.id.contacts_frag_RV);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        //setting up the firebase ref

        ref = new Firebase(FIREBASE_URL);
        postref = ref.child("USERS").child(UID_str).child("CONTACTS");

        //YOU LEFT HERE. IMPLEMENT ADAPTER
        adapterContacts = new Adapter_Contacts(POJO_ContactsRVDisplay.class, R.layout.row_contacts_layout, Adapter_Contacts.ContactsViewHolder.class, postref, getActivity());

        //set the animators
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapterContacts);
        scaleInAnimationAdapter.setDuration(1000);
        recyclerView.setAdapter(scaleInAnimationAdapter);

        if (UID_str==null){
            UID_str=Activity_Login.UID;
        }

        Firebase refER = new Firebase(FIREBASE_URL);
        refER.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    Log.d("USER", "LOGGED IN");
                } else {
                    Log.d("USER", "LOGGED out");
                }
            }
        });

        adapterContacts.setOnItemClickListener(new Adapter_Contacts.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                //Snackbar.make(rootview, "You clicked on it will open detail later", Snackbar.LENGTH_SHORT).show();
                POJO_ContactsRVDisplay pojo_contactsRVDisplay = adapterContacts.getItem(position);
                String UID = pojo_contactsRVDisplay.getUID();
                mListener.onContactClicked(UID);
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
        void onContactClicked(String contactUID);
    }
}
