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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;


public class fragment_UserDetail extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String userUID,userOption;
    private OnFragmentInteractionListener mListener;
    private  View rootview;
    TextView username,email,gender;
    ImageView profile,bg;
    private static final String FIREBASE_URL = "https://memoryapp.firebaseio.com/";
    Firebase ref;
    Query postref;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_MemoryList adapterMemoryList;

    public fragment_UserDetail() {
        // Required empty public constructor
    }

    public static fragment_UserDetail newInstance(String param1, String param2) {
        fragment_UserDetail fragment = new fragment_UserDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userUID = getArguments().getString(ARG_PARAM1);
            userOption = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
        ref = new Firebase(FIREBASE_URL);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_user_detail, container, false);
        username = (TextView) rootview.findViewById(R.id.user_name);
        email = (TextView) rootview.findViewById(R.id.user_email);
        gender = (TextView) rootview.findViewById(R.id.user_gender);
        profile = (ImageView) rootview.findViewById(R.id.user_profileimage);
        bg = (ImageView) rootview.findViewById(R.id.user_bgimage);

        if (username != null && email != null && gender != null && profile != null && bg != null){
            Firebase newref = ref.child("USERS").child(userUID).child("BIO");
            newref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    POJO_UserBIO post = snapshot.getValue(POJO_UserBIO.class);
                    username.setText(post.getName());
                    email.setText(post.getEmail());
                    if (post.getIsGenderFemale().equalsIgnoreCase("false")){
                        gender.setText("Male");
                    }else{
                        gender.setText("Female");
                    }
                    Picasso.with(getContext()).load(post.getProfilepic()).into(profile);
                    Picasso.with(getContext()).load(post.getBgpic()).into(bg);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Snackbar.make(rootview,"event cancelled",Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        //setting up the recycler view
        recyclerView = (RecyclerView) rootview.findViewById(R.id.frag_memlist_RV_userdetail);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //setting up the query based on user option
        if (userOption.equalsIgnoreCase("Friends")){
            postref = (Query) ref.child("USERS").child(userUID).child("MEMORIES").orderByChild("share").endAt("Public");
        }else{
            postref = (Query) ref.child("USERS").child(userUID).child("MEMORIES").orderByChild("share").equalTo("Public");
        }


        //YOU LEFT HERE. IMPLEMENT ADAPTER
        adapterMemoryList = new Adapter_MemoryList(POJO_MemoriesRVDisplay.class, R.layout.row_memory_layout, Adapter_MemoryList.MemoryViewHolder.class, postref, getActivity());
        recyclerView.setAdapter(adapterMemoryList);

        adapterMemoryList.setOnItemClickListener(new Adapter_MemoryList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //this code gives us the key we clicked on particular memory. This we need to pass to different objects.
                String memkey = adapterMemoryList.getRef(position).getKey();
                Snackbar.make(rootview,memkey,Snackbar.LENGTH_SHORT).show();
                mListener.onMemoryDetailClicked(memkey,userUID);
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
        void onMemoryDetailClicked(String memkey, String useruid);
    }
}
