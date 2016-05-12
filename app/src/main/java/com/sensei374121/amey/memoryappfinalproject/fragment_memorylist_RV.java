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

import com.firebase.client.Firebase;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class fragment_memorylist_RV extends Fragment {

    private static String UID_frag;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_MemoryList adapterMemoryList;
    private static final String FIREBASE_URL = "https://memoryapp.firebaseio.com/";
    Firebase ref,postref;

    public fragment_memorylist_RV() {
        // Required empty public constructor
    }

    public static fragment_memorylist_RV newInstance(String uid_rec) {
        fragment_memorylist_RV fragment = new fragment_memorylist_RV();
        Bundle args = new Bundle();

        //you can add arguments as needed
        UID_frag = uid_rec;

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview;
        rootview = inflater.inflate(R.layout.fragment_memorieslist, container, false);

        //setting up the recycler view
        recyclerView = (RecyclerView) rootview.findViewById(R.id.frag_memlist_RV);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //setting up the firebase ref
        ref = new Firebase(FIREBASE_URL);
        if (UID_frag==null){
            UID_frag=Activity_Login.UID;
        }
        postref = ref.child("USERS").child(UID_frag).child("MEMORIES");

        //YOU LEFT HERE. IMPLEMENT ADAPTER
        adapterMemoryList = new Adapter_MemoryList(POJO_MemoriesRVDisplay.class, R.layout.row_memory_layout, Adapter_MemoryList.MemoryViewHolder.class, postref, getActivity());

        //set the animators
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapterMemoryList);
        scaleInAnimationAdapter.setDuration(1000);
        recyclerView.setAdapter(scaleInAnimationAdapter);

        adapterMemoryList.setOnItemClickListener(new Adapter_MemoryList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //this code gives us the key we clicked on particular memory. This we need to pass to different objects.
                String memkey = adapterMemoryList.getRef(position).getKey();
                Snackbar.make(rootview,memkey,Snackbar.LENGTH_SHORT).show();
                mListener.onMemoryDetailClicked(memkey);
                //TODO implement the fragment detail call here
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
        void onMemoryDetailClicked(String memkey);
    }
}
