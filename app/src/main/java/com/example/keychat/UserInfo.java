package com.example.keychat;

public class UserInfo {
    private static String username;
    private static String userID;

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        UserInfo.userID = userID;
    }


    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername(){
        return  username;
    }
}
