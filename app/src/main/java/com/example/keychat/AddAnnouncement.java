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

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class AddAnnouncement extends AppCompatActivity {

    private EditText contentText;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_announcement);

        sendButton = findViewById(R.id.button2);
        contentText = findViewById(R.id.editTextTextMultiLine);

        Socket socket = ServerConnection.getServerConnection();

        sendButton.setOnClickListener(v -> {
            JSONObject announcement = new JSONObject();
            try {
                announcement.accumulate("title", "Title");
                announcement.accumulate("content", contentText.getText().toString());
                announcement.accumulate("createdBy", UserInfo.getUserID());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            socket.emit("create_announcement", announcement);
            finish();
        });

        socket.on("announcement_created", args -> {
           Toaster.toast("Created Announcement", AddAnnouncement.this);
        });
    }
}