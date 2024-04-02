package com.example.keychat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddContact extends AppCompatActivity {

    EditText username;
    Button addContactBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        addContactBtn = findViewById(R.id.add_contact);
        username = findViewById(R.id.username);

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = username.getText().toString();
                Intent intent = new Intent(AddContact.this, ContactsView.class);
                intent.putExtra("username", s);
                AddContact.this.startActivity(intent);
            }
        });
    }
}