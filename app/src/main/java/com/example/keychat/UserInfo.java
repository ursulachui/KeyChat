package com.example.keychat;

public class UserInfo {
    private static String username;

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername(){
        return  username;
    }
}
