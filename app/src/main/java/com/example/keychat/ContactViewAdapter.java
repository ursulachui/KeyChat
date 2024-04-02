package com.example.keychat;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactViewAdapter extends RecyclerView.Adapter<ContactViewAdapter.ViewHolder> {

    public interface OnContactClickListener {
        public void onClick(Contact item);
    }

    ArrayList<Contact> contacts = new ArrayList<>();
    private OnContactClickListener listener;

    public ContactViewAdapter(OnContactClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(contacts.get(position), listener);
        holder.name.setText(contacts.get(position).getName());
        holder.id.setText(Integer.toString(contacts.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void addContact(Contact c) {
        this.contacts.add(c);
        notifyItemInserted(getItemCount() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            id = itemView.findViewById(R.id.contact_id);
        }

        public void bind(Contact contact, OnContactClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(contact);
                }
            });
        }
    }
}
