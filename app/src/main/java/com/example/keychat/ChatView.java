package com.example.keychat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatView extends AppCompatActivity {

    private RecyclerView log;
    private EditText enterMsg;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);


        log = findViewById(R.id.log);
        enterMsg = findViewById(R.id.msg);
        send = findViewById(R.id.send);

        MessageViewAdapter mva = new MessageViewAdapter();
        mva.addToLog(new Message("Now chatting with " + name, "system"));
        log.setAdapter(mva);
        log.setLayoutManager(new LinearLayoutManager(this));

        //TODO: Pull logs from db and display

        send.setOnClickListener(v -> {
            String s = enterMsg.getText().toString();
            if(!s.isEmpty()) {
                mva.addToLog(new Message(s, "Me"));
                enterMsg.setText("");

            }
        });
    }
}