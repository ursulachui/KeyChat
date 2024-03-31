package com.example.keychat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.ViewHolder>{

    private ArrayList<Message> messages;
    public MessageViewAdapter() {
        messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.msgContent.setText(messages.get(position).getMsg());
        holder.date.setText(messages.get(position).getTime());
        holder.author.setText(messages.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addToLog(Message m) {
        this.messages.add(m);
        notifyItemInserted(getItemCount()-1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView msgContent;
        public TextView author;
        public TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msgContent = itemView.findViewById(R.id.msgContent);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.time);
        }
    }
}
