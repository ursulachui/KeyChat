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
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginPage extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private Button login;
    private TextView status;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.button);
        status = findViewById(R.id.server_status);
        register = findViewById(R.id.new_account);

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
            String tgt;
            socket.emit("get_login", email + "," + password, (Ack) args -> {
                if((int) args[0] == 100) {
                    socket.emit("register", email, (Ack) args1 -> {
                        byte[] encryptedSessionKey = (byte[]) args1[0];
                        byte[] encryptedTgt = (byte[]) args1[1];
                        byte[] decryptedSessionKey = null;
                        byte[] decryptedTgt = null;
                        Log.d("KEY", new String(encryptedSessionKey, StandardCharsets.UTF_8));
                        try {
                            SecretKey passwordKey = Encryptor.getKeyFromString(password);
                            decryptedSessionKey = Encryptor.decrypt(encryptedSessionKey, passwordKey);
                            decryptedTgt = Encryptor.decrypt(encryptedTgt, passwordKey);
                            Intent i = new Intent(LoginPage.this, ContactsView.class);
                            i.putExtra("login", email);
                            LoginPage.this.startActivity(i);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("KEY", new String(decryptedSessionKey, StandardCharsets.UTF_8));
                        Log.d("TGT", new String(decryptedTgt, StandardCharsets.UTF_8));

                    });
                    String username = UserInfo.getUsername();
                    String recipient = "server";
                    socket.emit("get_ticket", username, recipient, Encryptor.getTgt(), (Ack) ticketArgs -> {
                       String recipientResponse = (String) ticketArgs[0];
                       byte[] encryptedSharedKey = (byte[]) ticketArgs[1];
                       byte[] encryptedTicket = (byte[]) ticketArgs[2];
                       byte[] decryptedSharedKey;
                       byte[] decryptedTicket;
                       try {
                           decryptedSharedKey = Encryptor.decrypt(encryptedSharedKey,Encryptor.getSession_key());
                           decryptedTicket = Encryptor.decrypt(encryptedTicket,Encryptor.getSession_key());
                       } catch (Exception e) {
                           throw new RuntimeException(e);
                       }
                       TicketHandler.setTicket(decryptedTicket);
                       TicketHandler.setShared_key(new SecretKeySpec(decryptedSharedKey,"AES"));

                    });
                } else {
                    Toaster.toast("Login failed", LoginPage.this);
                }
            });
        });

        register.setOnClickListener(v -> {
            Intent i = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(i);
        });
    }
}