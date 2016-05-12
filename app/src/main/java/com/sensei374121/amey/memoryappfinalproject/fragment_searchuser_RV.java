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

public class fragment_searchuser_RV extends Fragment {

    private static String ARG_PARAM1 ;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_SearchList adapterSearchList;
    private static final String FIREBASE_URL = "https://memoryapp.firebaseio.com/";
    Firebase ref,postref;

    private String UID_str,username,user_mail,profilepic;
    private String mParam2;


    public fragment_searchuser_RV() {
        // Required empty public constructor
    }

    public static fragment_searchuser_RV newInstance(String param1) {
        fragment_searchuser_RV fragment = new fragment_searchuser_RV();
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
        rootview = inflater.inflate(R.layout.fragment_searchuser__rv, container, false);

        //setting up the firebase adapter
        recyclerView = (RecyclerView) rootview.findViewById(R.id.search_user_frag_RV);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //setting up the firebase ref

        ref = new Firebase(FIREBASE_URL);
        postref = ref.child("USERS-LIST");

        //YOU LEFT HERE. IMPLEMENT ADAPTER
        adapterSearchList = new Adapter_SearchList(POJO_UserBIO.class, R.layout.row_searchuser_layout, Adapter_SearchList.SearchListViewHolder.class, postref, getActivity());

        //set the animators
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapterSearchList);
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
                profilepic = post.getProfilepic();

                // }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Snackbar.make(rootview,"event cancelled",Snackbar.LENGTH_SHORT).show();
            }
        });

        adapterSearchList.setOnItemClickListener(new Adapter_SearchList.OnItemClickListener() {

            private String toSendUID,currUserUID,dispUserUID;
            private boolean valid=true;

            @Override
            public void onItemClick(View view, int position) {
                Snackbar.make(rootview,"You clicked on it will open detail later",Snackbar.LENGTH_SHORT).show();
                POJO_UserBIO userBIO = adapterSearchList.getItem(position);
                dispUserUID = userBIO.getUid();
                mListener.onSearchUserClicked(dispUserUID);
            }

            @Override
            public void onAddFriendClicked(View view, final int position) {
                POJO_UserBIO userBIO = adapterSearchList.getItem(position);
                toSendUID = userBIO.getUid();
                currUserUID = UID_str;
                POJO_FriendRequests friendRequests = new POJO_FriendRequests(username,user_mail,currUserUID,toSendUID,profilepic);
                HashMap sendmap = friendRequests.createFrndRequest();
                Firebase tmpref = ref.child("USERS").child(toSendUID).child("FRIEND-REQUESTS").child(currUserUID);
                tmpref.updateChildren(sendmap);
                Snackbar.make(rootview,"Friend List sent to UID:"+toSendUID,Snackbar.LENGTH_SHORT).show();
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
        void onSearchUserClicked(String userUID);
    }
}
