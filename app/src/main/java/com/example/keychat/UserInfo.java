package com.example.keychat;

public class UserInfo {
    private static String username;
    private static String userID;
    private static String email;

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

    public static void setEmail(String newEmail) {
        email = newEmail;
    }

    public static String getEmail() {
        return email;
    }
}
