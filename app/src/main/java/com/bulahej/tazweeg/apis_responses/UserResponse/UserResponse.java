package com.bulahej.tazweeg.apis_responses.UserResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserResponse {

    @SerializedName("Status")
    @Expose
    private Integer Status;

    @SerializedName("Message")
    @Expose
    private String message;

    //phone is used when user is trying to signup but have already signed up and could not activate its account
    @SerializedName("Phone")
    @Expose
    private String Phone;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    @SerializedName(value="name",  alternate={"Consultant", "user" , "Data", "User"})
    @Expose
    private User user;


    @SerializedName("MatchMemberList")
    @Expose
    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        this.Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}