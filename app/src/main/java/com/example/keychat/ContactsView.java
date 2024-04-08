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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ContactsView extends AppCompatActivity {

    private RecyclerView contactsView;
    private Button addContactBtn;
    private Button viewAnnounceBtn;
    private Button newAnnounceBtn;
    private Button profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_view);

        contactsView = findViewById(R.id.contacts_list);
        addContactBtn = findViewById(R.id.add_contact_btn);
        viewAnnounceBtn = findViewById(R.id.announcements_btn);
        newAnnounceBtn = findViewById(R.id.new_btn);
        profileBtn = findViewById(R.id.profile_btn);

        ContactViewAdapter cva = new ContactViewAdapter(item -> {
            Intent intent = new Intent(ContactsView.this, ChatView.class);
            intent.putExtra("name", item.getName());
            ContactsView.this.startActivity(intent);
        });

        contactsView.setAdapter(cva);
        contactsView.setLayoutManager(new LinearLayoutManager(this));

        Socket socket = ServerConnection.getServerConnection();
        socket.emit("get_user_contacts", UserInfo.getUserID());

        socket.on("user_contacts", args -> {
            JSONObject contacts = (JSONObject) args[0];
            try {
                String[] contactIDs = (String[]) contacts.get("contacts");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        ArrayList<Contact> contacts = new ArrayList<>();
        cva.addContact(new Contact("Alice", new Random().nextInt(100000)));
        cva.addContact(new Contact("Bob", new Random().nextInt(100000)));

        Intent intent = getIntent();
        if(intent.hasExtra("username")){
            //TODO: Check if user exists
            String s = intent.getStringExtra("username");
            int id = new Random().nextInt(100000);
            cva.addContact(new Contact(s, id));
        }
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