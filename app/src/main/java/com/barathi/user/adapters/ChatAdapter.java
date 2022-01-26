package com.barathi.user.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barathi.user.R;
import com.barathi.user.databinding.ItemChatBinding;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
//ll
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ItemChatBinding binding;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemChatBinding.bind(itemView);
        }
    }
}
