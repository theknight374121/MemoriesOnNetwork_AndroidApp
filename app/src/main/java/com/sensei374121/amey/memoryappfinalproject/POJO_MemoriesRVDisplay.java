package com.sensei374121.amey.memoryappfinalproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amey on 26-04-2016.
 * This file is the POJO file for short memories view on recycler view
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class POJO_MemoriesRVDisplay {
    String mem_name;
    String mem_place_street_address;
    List images;

    public POJO_MemoriesRVDisplay() {
    }

    public List getImages() {
        return images;
    }

    public void setImages(List images) {
        this.images = images;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_place_street_address() {
        return mem_place_street_address;
    }

    public void setMem_place_street_address(String mem_place_street_address) {
        this.mem_place_street_address = mem_place_street_address;
    }

}
