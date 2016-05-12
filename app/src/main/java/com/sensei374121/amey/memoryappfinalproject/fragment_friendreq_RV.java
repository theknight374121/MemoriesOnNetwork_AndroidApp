package com.sensei374121.amey.memoryappfinalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class fragment_friendreq_RV extends Fragment {

    private static String ARG_PARAM1 ;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_FriendRequest adapterFriendReq;
    private static final String FIREBASE_URL = "https://memoryapp.firebaseio.com/";
    Firebase ref,postref;

    private String UID_str,username,user_mail,profilepic;


    public fragment_friendreq_RV() {
        // Required empty public constructor
    }

    public static fragment_friendreq_RV newInstance(String param1) {
        fragment_friendreq_RV fragment = new fragment_friendreq_RV();
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
        rootview = inflater.inflate(R.layout.fragment_friendreq__rv, container, false);

        //setting up the firebase adapter
        recyclerView = (RecyclerView) rootview.findViewById(R.id.friendreq_frag_RV);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //setting up the firebase ref

        ref = new Firebase(FIREBASE_URL);
        postref = ref.child("USERS").child(UID_str).child("FRIEND-REQUESTS");

        //YOU LEFT HERE. IMPLEMENT ADAPTER
        adapterFriendReq = new Adapter_FriendRequest(POJO_FriendRequests.class, R.layout.row_frndrequest_layout, Adapter_FriendRequest.FriendRequestViewHolder.class, postref, getActivity());

        //set the animators
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapterFriendReq);
        scaleInAnimationAdapter.setDuration(1000);
        recyclerView.setAdapter(scaleInAnimationAdapter);

        if (UID_str==null){
            UID_str=Activity_Login.UID;
        }

        Firebase newref = ref.child("USERS").child(UID_str).child("BIO");
        newref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                POJO_UserBIO post = snapshot.getValue(POJO_UserBIO.class);
                username = post.getName();
                Snackbar.make(rootview,username,Snackbar.LENGTH_SHORT).show();
                user_mail = post.getEmail();
                profilepic= post.getProfilepic();
                // }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Snackbar.make(rootview,"event cancelled",Snackbar.LENGTH_SHORT).show();
            }
        });

        adapterFriendReq.setOnItemClickListener(new Adapter_FriendRequest.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Snackbar.make(rootview,"You clicked on it will open detail later",Snackbar.LENGTH_SHORT).show();

                POJO_FriendRequests pojo_friendRequests1 = adapterFriendReq.getItem(position);
                String sentFromUID = pojo_friendRequests1.getSendFrndReqUID();
                mListener.onFrndRequestClicked(sentFromUID);

                //TODO implement the fragment detail call here
            }

            @Override
            public void onAcceptFriendClicked(View view, int position) {
                //you clicked on accept friend request
                //Snackbar.make(rootview,"you clicked on accept friend",Snackbar.LENGTH_SHORT).show();
                POJO_FriendRequests pojo_friendRequests = adapterFriendReq.getItem(position);
                String sentFromName = pojo_friendRequests.getName();
                String sentFromEmail = pojo_friendRequests.getEmail();
                String sentFromprofilepic = pojo_friendRequests.getProfilepic();
                String sentFromUID = pojo_friendRequests.getSendFrndReqUID();
                String recvUID = pojo_friendRequests.getRecvFrndReqUID();

                //this piece of code is used to add contact to our list
                POJO_ContactsRVDisplay contactsRVDisplay = new POJO_ContactsRVDisplay(sentFromName,sentFromEmail,sentFromUID,sentFromprofilepic);
                HashMap sendMap = contactsRVDisplay.createContactHash();

                Firebase tempref = ref.child("USERS").child(UID_str).child("CONTACTS").child(sentFromUID);
                tempref.updateChildren(sendMap);
                Snackbar.make(rootview,"Contact added",Snackbar.LENGTH_SHORT).show();

                //this piece of code is to add our contact to his list.
                POJO_ContactsRVDisplay contactsRVDisplay1 = new POJO_ContactsRVDisplay(username, user_mail, UID_str,profilepic);
                HashMap sendMap1 = contactsRVDisplay1.createContactHash();

                Firebase  tmpref2 = ref.child("USERS").child(sentFromUID).child("CONTACTS").child(UID_str);
                tmpref2.updateChildren(sendMap1);

                //this piece of code is to remove the friend request now.
                Firebase tmpref3 = ref.child("USERS").child(UID_str).child("FRIEND-REQUESTS").child(sentFromUID);
                tmpref3.removeValue();
            }

            @Override
            public void onRejectFriendClicked(View view, int position) {
                //this piece of code is to remove the friend request now.
                //get the UID of who sent it
                POJO_FriendRequests pojo_friendRequests = adapterFriendReq.getItem(position);
                String sentFromUID = pojo_friendRequests.getSendFrndReqUID();

                //get the reference to it and delete it
                Firebase tmpref3 = ref.child("USERS").child(UID_str).child("FRIEND-REQUESTS").child(sentFromUID);
                tmpref3.removeValue();
                Snackbar.make(rootview,"you clicked on reject friend",Snackbar.LENGTH_SHORT).show();
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
        void onFrndRequestClicked(String userID);
    }
}
