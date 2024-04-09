package com.example.keychat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnnouncementBoard extends AppCompatActivity {

    private TextView announcementContent;
    private TextView announcementTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcement_board);
        announcementContent = findViewById(R.id.textView7);
        announcementTitle = findViewById(R.id.textView2);
    }
}