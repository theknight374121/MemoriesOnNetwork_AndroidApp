package com.sensei374121.amey.memoryappfinalproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Amey on 25-04-2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class POJO_UserBIO implements Serializable{
    private String name;
    private String email;
    private String uid;
    private String profilepic;
    private String bgpic;
    private String aboutme;
    private String isGenderFemale;

    public POJO_UserBIO() {
    }

    public POJO_UserBIO(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getBgpic() {
        return bgpic;
    }

    public void setBgpic(String bgpic) {
        this.bgpic = bgpic;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getIsGenderFemale() {
        return isGenderFemale;
    }

    public void setIsGenderFemale(String isGenderFemale) {
        this.isGenderFemale = isGenderFemale;
    }

    public POJO_UserBIO(String name, String email, String uid, String profilepic) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.profilepic = profilepic;
    }

    public POJO_UserBIO(String name, String email, String uid, String profilepic, String bgpic, String aboutme, String isGenderFemale) {

        this.name = name;

        this.email = email;
        this.uid = uid;
        this.profilepic = profilepic;
        this.bgpic = bgpic;
        this.aboutme = aboutme;
        this.isGenderFemale = isGenderFemale;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashMap<String, ?> createUserBIOHashBasic(){
        HashMap userBio = new HashMap();
        userBio.put("name", this.name);
        userBio.put("email", this.email);
        return userBio;
    }

    public HashMap<String, ?> createUserBIOHashUserList(){
        HashMap userBio = new HashMap();
        userBio.put("name", this.name);
        userBio.put("email", this.email);
        userBio.put("uid", this.uid);
        return userBio;
    }

    public HashMap<String, ?> createHashBIOHashContact() {
        HashMap userBio = new HashMap();
        userBio.put("name", this.name);
        userBio.put("email", this.email);
        userBio.put("uid", this.uid);
        userBio.put("profilepic", this.profilepic);
        return userBio;
    }

    public HashMap<String, ?> createHashBIOHashComplete(){
        HashMap userBio = new HashMap();
        userBio.put("name", this.name);
        userBio.put("email", this.email);
        userBio.put("uid", this.uid);
        userBio.put("profilepic", this.profilepic);
        userBio.put("bgpic", this.bgpic);
        userBio.put("aboutme", this.aboutme);
        userBio.put("isGenderFemale", this.isGenderFemale);
        return userBio;
    }

}
