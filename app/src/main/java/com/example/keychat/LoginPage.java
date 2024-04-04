package com.example.keychat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.button);

        Socket socket = ServerConnection.getServerConnection();
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
               socket.emit("connected",  "Device " + Build.MODEL + " connected");
               Toaster.toast("Connected to server", LoginPage.this);
            }
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Toaster.toast("Error connecting to server", LoginPage.this);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                socket.emit("get_login", email + "," + password, new Ack() {
                    @Override
                    public void call(Object... args) {
                        if((int) args[0] == 100) {
                            Toaster.toast("Login Success", LoginPage.this);
                            socket.emit("register", email, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    String encryptedPayload = (String) args[0];
                                    String decryptedPayload = null;
                                    Log.d("KEY", encryptedPayload);
                                    try {
                                        Encryptor.setKey(password);
                                        decryptedPayload = Encryptor.decrypt(encryptedPayload);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    Log.d("KEY", decryptedPayload);
                                }
                            });
                        } else {
                            Toaster.toast("Login failed", LoginPage.this);
                        }
                    }
                });
            }
        });

//        socket.on("register", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//
//            }
//        })

        socket.on("tgt_get", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String tgt = (String) args[0];
                Toaster.toast(tgt, LoginPage.this);
            }
        });

        socket.on("ticket_get", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });
    }
}