package com.sensei374121.amey.memoryappfinalproject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amey on 26-04-2016.
 */
public class POJO_MemoryDetail implements Serializable {
    private String mem_name;
    private String mem_place_name;
    private String mem_place_street_address;
    private String latitude;
    private String longitude;
    private String description;
    private String share;
    private List<String> images;

    public POJO_MemoryDetail() {
    }

    public POJO_MemoryDetail(String mem_name, String mem_place_name, String mem_place_street_address, String latitude, String longitude, String description, String share, List<String> images) {
        this.mem_name = mem_name;
        this.mem_place_name = mem_place_name;
        this.mem_place_street_address = mem_place_street_address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.share = share;
        this.images = images;
    }

    public HashMap createHash(){
        HashMap hashMap = new HashMap<>();
        hashMap.put("mem_name", this.mem_name);
        hashMap.put("mem_place_name", this.mem_place_name);
        hashMap.put("mem_place_street_address", this.mem_place_street_address);
        hashMap.put("latitude",this.latitude);
        hashMap.put("longitude", this.longitude);
        hashMap.put("description", this.description);
        hashMap.put("share", this.share);
        hashMap.put("images", this.images);

        return hashMap;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_place_name() {
        return mem_place_name;
    }

    public void setMem_place_name(String mem_place_name) {
        this.mem_place_name = mem_place_name;
    }

    public String getMem_place_street_address() {
        return mem_place_street_address;
    }

    public void setMem_place_street_address(String mem_place_street_address) {
        this.mem_place_street_address = mem_place_street_address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
