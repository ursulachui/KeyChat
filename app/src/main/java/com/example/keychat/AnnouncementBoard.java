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
        socket.emit("get_all_announcements", (Ack) args -> {

            JSONArray announcements;
            JSONArray announcementsToDisplay;
            try {
                announcements = (JSONArray) args[0];
                if (announcements.length() <= 2) {
                    announcementsToDisplay = announcements;
                } else {
                    announcementsToDisplay = new JSONArray(new JSONObject[]{announcements.getJSONObject(0),announcements.getJSONObject(1)});
                }
                for (int i = 0; i < announcementsToDisplay.length(); i++) {
                    JSONObject announcement = announcementsToDisplay.getJSONObject(i);
                    String title = announcement.getString("title");
                    String content = announcement.getString("content");
                    String createdAt = announcement.getString("createdAt");
                    String createdBy = announcement.getString("createdBy");
                    runOnUiThread(() -> {
                        announcementTitle.setText(title);
                        announcementContent.setText(content);
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        });
        setContentView(R.layout.activity_announcement_board);
        //announcementContent = findViewById(R.id.textView7);
        //announcementTitle = findViewById(R.id.textView2);

    }
}