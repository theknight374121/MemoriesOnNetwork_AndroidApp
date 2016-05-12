package com.sensei374121.amey.memoryappfinalproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Amey on 27-04-2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class POJO_ContactsRVDisplay implements Serializable {
    String name,email,UID,profilepic;

    public POJO_ContactsRVDisplay() {
    }

    public HashMap createContactHash(){
        HashMap hashMap = new HashMap();

        hashMap.put("name", this.name);
        hashMap.put("email", this.email);
        hashMap.put("UID", this.UID);
        hashMap.put("profilepic", this.profilepic);

        return hashMap;
    }

    public POJO_ContactsRVDisplay(String name, String email, String UID, String profilepic) {
        this.name = name;
        this.email = email;
        this.UID = UID;
        this.profilepic = profilepic;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
