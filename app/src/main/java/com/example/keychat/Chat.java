package com.example.keychat;

public class Chat {
    private Contact contact;
    private String chatID;

    public Chat(Contact contact, String chatID) {
        this.contact = contact;
        this.chatID = chatID;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
}
