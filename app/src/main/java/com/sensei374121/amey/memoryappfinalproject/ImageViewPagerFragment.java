package com.sensei374121.amey.memoryappfinalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageViewPagerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String imgLink;

    public ImageViewPagerFragment() {
        // Required empty public constructor
    }

    public static ImageViewPagerFragment newInstance(String param1) {
        ImageViewPagerFragment fragment = new ImageViewPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            imgLink = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_image_view_pager, container, false);

        //TextView txtView = (TextView) rootView.findViewById(R.id.pager_image);
        ImageView displayimage = (ImageView) rootView.findViewById(R.id.pager_image);

        if(displayimage != null){
           Picasso.with(getContext()).load(imgLink).into(displayimage);

        }


        return rootView;
    }

}
