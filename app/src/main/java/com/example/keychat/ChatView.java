package com.example.keychat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView log;
    private EditText enterMsg;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = findViewById(R.id.log);
        enterMsg = findViewById(R.id.msg);
        send = findViewById(R.id.send);

        MessageViewAdapter mva = new MessageViewAdapter();
        mva.addToLog(new Message("Initial Message", "system"));
        log.setAdapter(mva);
        log.setLayoutManager(new LinearLayoutManager(this));

        send.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ContactsView.class);
            MainActivity.this.startActivity(i);

            String s = enterMsg.getText().toString();
            if(!s.isEmpty()) {
                mva.addToLog(new Message(s, "Me"));
                enterMsg.setText("");

            }
        });
    }
}