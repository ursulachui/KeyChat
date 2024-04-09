package com.example.keychat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatView extends AppCompatActivity {

    private RecyclerView log;
    private EditText enterMsg;
    private Button send;
    private String chatID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Socket socket = ServerConnection.getServerConnection();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        setTitle(name);

        JSONArray participants = new JSONArray();
        participants.put(UserInfo.getUserID());
        participants.put(id);

        socket.emit("get_chat_from_participants", participants);

        log = findViewById(R.id.log);
        enterMsg = findViewById(R.id.msg);
        send = findViewById(R.id.send);

        MessageViewAdapter mva = new MessageViewAdapter();
        mva.addToLog(new Message("Now chatting with " + name, "system"));
        log.setAdapter(mva);
        log.setLayoutManager(new LinearLayoutManager(this));

        send.setOnClickListener(v -> {
            String s = enterMsg.getText().toString();
            if (!s.isEmpty()) {
                socket.emit("add_chat_message", chatID, UserInfo.getUserID(), s);
            }
        });

        socket.on("message_added", args -> {
            socket.emit("get_chat_from_participants", participants);
        });

        socket.on("chat_data", args -> {
            JSONObject chat = (JSONObject) args[0];
            try {
                chatID = chat.getString("_id");
                JSONArray messages = chat.getJSONArray("messages");
                runOnUiThread(() -> {
                    mva.clearLog();
                    mva.addToLog(new Message("Now chatting with " + name, "system"));
                });
                for (int i = 0; i < messages.length(); i++) {
                    JSONObject message = messages.getJSONObject(i);
                    Message m = new Message(message.getString("content"), message.getString("senderId"), message.getString("sentAt"));
                    runOnUiThread(() -> {
                        mva.addToLog(m);
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        socket.on("chat_not_found", args -> {
           Toaster.toast("Error", ChatView.this);
        });
    }
}