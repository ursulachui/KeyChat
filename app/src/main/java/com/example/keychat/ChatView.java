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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String chatID = intent.getStringExtra("chatID");
        setTitle(name);


        log = findViewById(R.id.log);
        enterMsg = findViewById(R.id.msg);
        send = findViewById(R.id.send);

        MessageViewAdapter mva = new MessageViewAdapter();
        mva.addToLog(new Message("Now chatting with " + name, "system"));
        mva.addToLog(new Message( "ChatID: " + chatID, "system"));
        log.setAdapter(mva);
        log.setLayoutManager(new LinearLayoutManager(this));

        Socket socket = ServerConnection.getServerConnection();
        socket.emit("get_chat", chatID, TicketHandler.getShared_key().getEncoded());

        send.setOnClickListener(v -> {
            String s = enterMsg.getText().toString();
            if(!s.isEmpty()) {
                socket.emit("add_chat_message", chatID, UserInfo.getUserID(), s);
            }
        });

        socket.on("message_added", args -> {
            runOnUiThread(() -> {
                mva.clearLog();
                mva.addToLog(new Message("Now chatting with " + name, "system"));
                mva.addToLog(new Message( "ChatID: " + chatID, "system"));
            });
                socket.emit("get_chat", chatID, TicketHandler.getShared_key().getEncoded());
        });

        socket.on("chat_retrieved", args -> {
            JSONObject chat = (JSONObject) args[0];
            try {
                String id = chat.getString("_id");
                JSONArray participants = chat.getJSONArray("participants");
                String createdAt = chat.getString("createdAt");
                JSONArray messages = chat.getJSONArray("messages");
                for(int i = 0; i < messages.length(); i++) {
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
    }
}