package com.example.keychat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ContactsView extends AppCompatActivity {

    public interface OnContactClickListener {
        public void onClick(Contact item);
    }
    private RecyclerView contactsView;
    private FloatingActionButton addContactBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_view);

        contactsView = findViewById(R.id.contacts_list);
        addContactBtn = findViewById(R.id.add_contact);
        ContactViewAdapter cva = new ContactViewAdapter(new ContactViewAdapter.OnContactClickListener() {
            @Override
            public void onClick(Contact item) {
                Intent intent = new Intent(ContactsView.this, ChatView.class);
                intent.putExtra("name", item.getName());
                ContactsView.this.startActivity(intent);
            }
        });

        contactsView.setAdapter(cva);
        contactsView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Contact> contacts = new ArrayList<>();
        cva.addContact(new Contact("Alice", new Random().nextInt(100000)));
        cva.addContact(new Contact("Bob", new Random().nextInt(100000)));

        Intent intent = getIntent();
        if(intent.hasExtra("username")){
            //TODO: Check if user exists
            String s = intent.getStringExtra("username");
            int id = new Random().nextInt(100000);
            cva.addContact(new Contact(s, id));
        } else if(intent.hasExtra("login")) {
            Toaster.toast("Signed in as " + intent.getStringExtra("login"), ContactsView.this);
            Log.d("SESSION_KEY", Encryptor.getSession_key());
            Log.d("TGT", Encryptor.getTgt());
        }

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactsView.this, AddContact.class);
                ContactsView.this.startActivity(i);
            }
        });
    }
}