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

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class AddContact extends AppCompatActivity {

    EditText username;
    Button addContactBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        addContactBtn = findViewById(R.id.add_contact);
        username = findViewById(R.id.username);

        Socket socket = ServerConnection.getServerConnection();
        addContactBtn.setOnClickListener(v -> {
            String s = username.getText().toString();
            socket.emit("add_contact_by_name", UserInfo.getUserID(), s);
        });

        socket.on("contact_added", args -> {
            Toaster.toast("added", AddContact.this);
            JSONObject contactJson = null;
            try {
                contactJson = ((JSONObject) args[0]).getJSONObject("employee");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            Intent i = new Intent(AddContact.this, ContactsView.class);
            try {
                i.putExtra("username",contactJson.getString("username"));
                i.putExtra("userid", contactJson.getString("_id"));
                AddContact.this.startActivity(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        socket.on("contact_not_found", args -> Toaster.toast("Contact does not exist", AddContact.this));
    }
}