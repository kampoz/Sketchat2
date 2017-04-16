package com.kampoz.sketchat.model;

import java.util.ArrayList;

/**
 * Created by wasili on 2017-04-16.
 */

public class GroupModel {
    private String groupName;
    private ArrayList<UserModel> usersArrayList;

    public GroupModel(String groupName){
        this.groupName = groupName;
    }

    public GroupModel(String groupName, ArrayList<UserModel> usersArrayList){
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<UserModel> getUsersArrayList() {
        return usersArrayList;
    }

    public void setUsersArrayList(ArrayList<UserModel> usersArrayList) {
        this.usersArrayList = usersArrayList;
    }
}
