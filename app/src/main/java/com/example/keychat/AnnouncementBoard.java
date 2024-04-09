package com.example.keychat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AnnouncementBoard extends AppCompatActivity {

    private TextView announcementContent;
    private TextView announcementTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Socket socket = ServerConnection.getServerConnection();

        setContentView(R.layout.activity_announcement_board);
        announcementContent = findViewById(R.id.textView7);
        announcementTitle = findViewById(R.id.textView2);

        socket.emit("get_all_announcements");

        socket.on("all_announcements", args -> {
            JSONArray announcements;
            try {
                announcements = (JSONArray) args[0];
                JSONObject announcement = announcements.getJSONObject(announcements.length() -1);
                String title = announcement.getString("title");
                String content = announcement.getString("content");
                String createdAt = announcement.getString("createdAt");
                String createdBy = announcement.getString("createdBy");
                runOnUiThread(() -> {
                    announcementTitle.setText(title);
                    announcementContent.setText(content);
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

    }
}