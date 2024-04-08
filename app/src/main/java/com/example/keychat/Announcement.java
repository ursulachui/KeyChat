package com.example.keychat;

public class Announcement {
    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    private final String content;
    private final String sender;

    public Announcement(String content, String sender){
        this.sender = sender;
        this.content = content;
    }
}
