package com.example.keychat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.socket.client.Socket;

public class RegisterPage extends AppCompatActivity {

    private EditText name = findViewById(R.id.editTextText);
    private EditText age = findViewById(R.id.editTextNumber);
    private EditText email = findViewById(R.id.editTextTextEmailAddress2);
    private EditText role = findViewById(R.id.editTextText2);
    private Button registerBtn =  findViewById(R.id.button2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
        registerBtn.setOnClickListener(v -> {
            String empName = name.getText().toString();
            String empAge = age.getText().toString();
            String empEmail = age.getText().toString();
            String empRole = age.getText().toString();
            Socket socket = ServerConnection.getServerConnection();
        });
    }
}