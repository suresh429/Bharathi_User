package com.barathi.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.barathi.user.R;
import com.barathi.user.adapters.OrderItemsAdapter;
import com.barathi.user.databinding.FragmentOrderItemsBinding;
import com.barathi.user.model.OrderDetailRoot;
import com.google.gson.Gson;


public class OrderItemsFragment extends Fragment {
    FragmentOrderItemsBinding binding;
    private OrderDetailRoot.Data data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_items, container, false);
        String datastring = getArguments().getString("orderDetail");
        if(!datastring.equals("") && !datastring.isEmpty()) {
            data = new Gson().fromJson(datastring, OrderDetailRoot.Data.class);
            if(data != null) {
                setData();
            }
        }


        return binding.getRoot();
    }

    private void setData() {
        binding.tvItemCount.setText(String.valueOf(data.getItemDetails().size()).concat(" items"));
        binding.tvtotalPrice.setText(getString(R.string.currency).concat(" " + data.getOrderDetails().get(0).getTotalAmount()));
        if(!data.getItemDetails().isEmpty()) {
            OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter(data.getItemDetails());
            binding.rvOrderItems.setAdapter(orderItemsAdapter);
        }
    }




}