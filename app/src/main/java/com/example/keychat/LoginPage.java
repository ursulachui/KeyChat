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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
    private String password;
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
            password = passwordText.getText().toString();
            JSONObject loginInfo = new JSONObject();
            try {
                loginInfo.accumulate("email", email);
                loginInfo.accumulate("password_hash", password);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            socket.emit("login", loginInfo);
        });

        socket.on("login_success", args -> {
            try {
                JSONObject employeeInfo = ((JSONObject) args[0]).getJSONObject("employee");
                UserInfo.setUserID(employeeInfo.getString("_id"));
                UserInfo.setUsername(employeeInfo.getString("username"));
                UserInfo.setEmail(employeeInfo.getString("email"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            SecretKey passwordKey;
            try {
                passwordKey = Encryptor.getKeyFromPassword(password);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            SecretKey finalPasswordKey = passwordKey;
            socket.emit("register", UserInfo.getEmail(), passwordKey.getEncoded(), (Ack) args1 -> {
                byte[] encryptedSessionKey = (byte[]) args1[0];
                byte[] encryptedTgt = (byte[]) args1[1];
                byte[] decryptedSessionKey = null;
                byte[] decryptedTgt = null;
                Log.d("KEY", new String(encryptedSessionKey, StandardCharsets.UTF_8));
                try {
                    decryptedSessionKey = Encryptor.decrypt(encryptedSessionKey, finalPasswordKey);
                    decryptedTgt = Encryptor.decrypt(encryptedTgt, finalPasswordKey);
                    Intent i = new Intent(LoginPage.this, ContactsView.class);
                    i.putExtra("login", UserInfo.getEmail());
                    LoginPage.this.startActivity(i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Log.d("KEY", new String(decryptedSessionKey, StandardCharsets.UTF_8));
                Log.d("TGT", new String(decryptedTgt, StandardCharsets.UTF_8));
                String username = "text";
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
                    socket.emit("use_ticket", username, recipient, TicketHandler.getTicket(), (Ack) useticketargs -> {
                        byte[] useTicketResponse = (byte[]) useticketargs[0];
                        try {
                            String decryptedUseTicketResponse = new String(Encryptor.decrypt(useTicketResponse,TicketHandler.getShared_key()), StandardCharsets.UTF_8);
                            if (!decryptedUseTicketResponse.equals("100")) {
                                Log.d("ERROR response:", decryptedUseTicketResponse);
                            } else {
                                Log.d("Ticket Response", decryptedUseTicketResponse);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException();
                        }
                    });
                });
            });
            Toaster.toast("Login Success", LoginPage.this);
        });

        socket.on("login_failure", args -> Toaster.toast("Login Failed", LoginPage.this));

        socket.on("error", args -> {
            Toaster.toast("Login Error", LoginPage.this);
            JSONObject error = (JSONObject) args[0];
            try {
                Log.d("error", error.getString("error"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        register.setOnClickListener(v -> {
            Intent i = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(i);
        });
    }
}