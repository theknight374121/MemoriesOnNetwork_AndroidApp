package com.sensei374121.amey.memoryappfinalproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Amey on 27-04-2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class POJO_FriendRequests implements Serializable {

    String name,email,sendFrndReqUID,recvFrndReqUID,profilepic;

    public POJO_FriendRequests() {
    }

    public POJO_FriendRequests(String name, String email, String sendFrndReqUID, String recvFrndReqUID, String profilepic) {
        this.name = name;
        this.email = email;
        this.sendFrndReqUID = sendFrndReqUID;
        this.recvFrndReqUID = recvFrndReqUID;
        this.profilepic = profilepic;
    }

    public HashMap createFrndRequest(){
        HashMap hashMap = new HashMap();

        hashMap.put("name", this.name);
        hashMap.put("email", this.email);
        hashMap.put("sendFrndReqUID", this.sendFrndReqUID);
        hashMap.put("recvFrndReqUID", this.recvFrndReqUID);
        hashMap.put("profilepic", this.profilepic);

        return hashMap;
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

    public String getSendFrndReqUID() {
        return sendFrndReqUID;
    }

    public void setSendFrndReqUID(String sendFrndReqUID) {
        this.sendFrndReqUID = sendFrndReqUID;
    }

    public String getRecvFrndReqUID() {
        return recvFrndReqUID;
    }

    public void setRecvFrndReqUID(String recvFrndReqUID) {
        this.recvFrndReqUID = recvFrndReqUID;
    }



}
