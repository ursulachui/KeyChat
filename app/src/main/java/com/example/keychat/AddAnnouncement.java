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

        sendButton.setOnClickListener(v -> {
            Announcement a = new Announcement(contentText.getText().toString(), "Employee");
            finish();
        });
    }
}