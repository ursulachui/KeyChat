package com.example.keychat;

import java.util.Calendar;
import java.util.Date;

public class Message {
    private String msg;
    private String time;
    private String author;

    public Message(String msg, String author) {
        this.msg = msg;
        this.author = author;
        Date date = Calendar.getInstance().getTime();
        this.time = date.toString();
    }

    public Message(String msg, String author, String time) {
        this(msg, author);
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }
}
