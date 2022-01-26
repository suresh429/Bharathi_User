package com.barathi.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.barathi.user.R;
import com.barathi.user.databinding.FragmentChatBinding;
import com.barathi.user.adapters.ChatAdapter;


public class ChatFragment extends Fragment {
    FragmentChatBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        ChatAdapter chatAdapter = new ChatAdapter();
        binding.rvChat.setAdapter(chatAdapter);
    }
}