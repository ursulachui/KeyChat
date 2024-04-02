package com.example.keychat;

public class Contact {

    private String name;
    private int id;
    public Contact(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
