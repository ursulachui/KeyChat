package com.example.keychat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ContactsView extends AppCompatActivity {

    private RecyclerView contactsView;
    private Button addContactBtn;
    private Button viewAnnounceBtn;
    private Button newAnnounceBtn;
    private Button profileBtn;

    private JSONArray employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_view);

        contactsView = findViewById(R.id.contacts_list);
        addContactBtn = findViewById(R.id.add_contact_btn);
        viewAnnounceBtn = findViewById(R.id.announcements_btn);
        newAnnounceBtn = findViewById(R.id.new_btn);
        profileBtn = findViewById(R.id.profile_btn);


        Socket socket = ServerConnection.getServerConnection();
        socket.emit("get_all_employees");

        ContactViewAdapter cva = new ContactViewAdapter(item -> {
            Intent intent = new Intent(ContactsView.this, ChatView.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("id", item.getId());
            ContactsView.this.startActivity(intent);
        });

        contactsView.setAdapter(cva);
        contactsView.setLayoutManager(new LinearLayoutManager(this));

        socket.on("all_employees", args -> {
            employees = (JSONArray) args[0];
            socket.emit("get_user_contacts", UserInfo.getUserID());
        });

        socket.on("user_contacts", args -> {
            JSONArray contacts = null;
            try {
                runOnUiThread(() -> {
                    cva.clearContacts();
                });
                contacts = ((JSONObject) args[0]).getJSONArray("contacts");
                for(int j = 0; j < contacts.length(); j++) {
                    String id = contacts.getJSONObject(j).getString("contactUserId");
                    for(int i = 0; i < employees.length(); i++) {
                        JSONObject employee = employees.getJSONObject(i);
                        if(employee.get("_id").equals(id)) {
                            runOnUiThread(() -> {
                                try {
                                    cva.addContact(new Contact(employee.getString("username"), employee.getString("_id")));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        socket.on("contact_added", args -> {
           socket.emit("get_user_contacts", UserInfo.getUserID());
        });

        Intent intent = getIntent();
        if(intent.hasExtra("login")) {
            Toaster.toast("Signed in as " + intent.getStringExtra("login"), ContactsView.this);
        }

        addContactBtn.setOnClickListener(v -> {
            Intent i = new Intent(ContactsView.this, AddContact.class);
            ContactsView.this.startActivity(i);
        });

        viewAnnounceBtn.setOnClickListener(v -> {
            Intent i = new Intent(ContactsView.this, AnnouncementBoard.class);
            ContactsView.this.startActivity(i);
        });

        newAnnounceBtn.setOnClickListener(v -> {
            Intent i = new Intent(ContactsView.this, AddAnnouncement.class);
            ContactsView.this.startActivity(i);
        });
        profileBtn.setOnClickListener(v -> {
            Intent i = new Intent(ContactsView.this, ProfilePage.class);
            ContactsView.this.startActivity(i);
        });
    }
}