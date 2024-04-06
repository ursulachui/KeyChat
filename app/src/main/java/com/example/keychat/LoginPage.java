package com.example.keychat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.system.Os;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginPage extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private Button login;
    private TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.button);
        status = findViewById(R.id.server_status);

        Socket socket = ServerConnection.getServerConnection();
        socket.on(Socket.EVENT_CONNECT, args -> runOnUiThread(() -> {
               status.setText("Connected to server");
               status.setTextColor(Color.GREEN);
        }));

        socket.on(Socket.EVENT_CONNECT_ERROR, args -> runOnUiThread(() -> {
            status.setText("Error connecting to server");
            status.setTextColor(Color.RED);
        }));

        login.setOnClickListener(v -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            socket.emit("get_login", email + "," + password, new Ack() {
                @Override
                public void call(Object... args) {
                    if((int) args[0] == 100) {
                        socket.emit("register", email, (Ack) args1 -> {
                            String encryptedPayload = (String) args1[0];
                            String decryptedPayload = null;
                            Log.d("KEY", encryptedPayload);
                            try {
                                Encryptor.setKey(password);
                                decryptedPayload = Encryptor.decrypt(encryptedPayload);
                                JSONObject payload = new JSONObject(decryptedPayload);
                                Encryptor.setSession_key(payload.getString("session_key"));
                                Encryptor.setTgt(payload.getString("tgt"));
                                Intent i = new Intent(LoginPage.this, ContactsView.class);
                                i.putExtra("login", email);
                                LoginPage.this.startActivity(i);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            Log.d("KEY", decryptedPayload);
                        });
                    } else {
                        Toaster.toast("Login failed", LoginPage.this);
                    }
                }
            });
        });
    }
}