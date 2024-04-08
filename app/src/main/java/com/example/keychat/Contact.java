package com.example.keychat;

public class Contact {

    private String name;
    private String id;
    public Contact(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
