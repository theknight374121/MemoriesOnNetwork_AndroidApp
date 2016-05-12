package com.sensei374121.amey.memoryappfinalproject;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class fragment_open_viewpagercode extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List image_list;
    View rootview;
    ViewPager.PageTransformer obj,obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9, obj10,finalchoice;
    MyImageFragmentPagerAdapter myImageFragmentPagerAdapter;


    public fragment_open_viewpagercode() {
        // Required empty public constructor
    }

    public static fragment_open_viewpagercode newInstance(List image_list) {
        fragment_open_viewpagercode fragment = new fragment_open_viewpagercode();
        Bundle args = new Bundle();

        args.putParcelableArrayList(ARG_PARAM1, (ArrayList<? extends Parcelable>) image_list);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            image_list = getArguments().getParcelableArrayList(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_open_viewpagercode, container, false);

        //this is to choose a random object
        obj = new FlipPageViewTransformer();    //this code is used from the internet
        obj1 = new AccordionTransformer();  //these are from the libraries
        obj2 = new FlipHorizontalTransformer();
        obj3 = new CubeInTransformer();
        obj4 = new CubeOutTransformer();
        obj5 = new DepthPageTransformer();
        obj6 = new RotateDownTransformer();
        obj7 = new RotateUpTransformer();
        obj8 = new StackTransformer();
        obj9 = new TabletTransformer();
        obj10 = new ZoomOutSlideTransformer();

        List<ViewPager.PageTransformer> transformerList = new ArrayList<>();
        transformerList.add(obj);
        transformerList.add(obj1);
        transformerList.add(obj2);
        transformerList.add(obj3);
        transformerList.add(obj4);
        transformerList.add(obj5);
        transformerList.add(obj6);
        transformerList.add(obj7);
        transformerList.add(obj8);
        transformerList.add(obj9);
        transformerList.add(obj10);

        Random random = new Random(System.currentTimeMillis());
        int choice = random.nextInt(11);
        Log.d("choice", String.valueOf(choice));

        finalchoice = transformerList.get(choice);

//        //this if for demo purposes
//        List noOfImages = new ArrayList();
//        noOfImages.add("URVI");
//        noOfImages.add("AMEY");
        ViewPager myViewPager;
        myViewPager = (ViewPager)rootview.findViewById(R.id.image_viewpager_view);
        myImageFragmentPagerAdapter = new MyImageFragmentPagerAdapter(getActivity().getSupportFragmentManager(),image_list);
        myViewPager.setAdapter(myImageFragmentPagerAdapter);
        myViewPager.setCurrentItem(0,true);

        //this value is choose randomly from above code
        myViewPager.setPageTransformer(false,finalchoice);
        return rootview;
    }

}
