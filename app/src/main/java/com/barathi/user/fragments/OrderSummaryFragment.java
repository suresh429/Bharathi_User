package com.barathi.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.barathi.user.R;
import com.barathi.user.SessionManager;
import com.barathi.user.databinding.BottomSheetratingBinding;
import com.barathi.user.databinding.FragmentOrderSummaryBinding;
import com.barathi.user.model.Address;
import com.barathi.user.model.OrderDetailRoot;
import com.barathi.user.model.RestResponse;
import com.barathi.user.retrofit.Const;
import com.barathi.user.retrofit.RetrofitBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderSummaryFragment extends Fragment {
    FragmentOrderSummaryBinding binding;
    private OrderDetailRoot.Data data;
    private BottomSheetDialog bottomSheetDialog;
    private final String status;

    public OrderSummaryFragment(String status) {

        this.status = status;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_summary, container, false);
        String datastring = null;
        if(getArguments() != null) {
            datastring = getArguments().getString("orderDetail");
        }
        if(datastring != null && !datastring.equals("") && !datastring.isEmpty()) {
            data = new Gson().fromJson(datastring, OrderDetailRoot.Data.class);
            if(data != null) {
                setData();
            }
        }

        initView();
        initListnear();
        return binding.getRoot();
    }

    private void initListnear() {
        binding.ratingbar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> openBottomSheet(rating));
    }

    private void openBottomSheet(float rating) {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        BottomSheetratingBinding sheetratingBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottom_sheetrating, null, false);
        bottomSheetDialog.setContentView(sheetratingBinding.getRoot());
        bottomSheetDialog.show();
        sheetratingBinding.ratingbar.setRating(rating);
        sheetratingBinding.imgClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        sheetratingBinding.tvSubmit.setOnClickListener(v -> {
            String s = sheetratingBinding.etDes.getText().toString();
            int i = (int) sheetratingBinding.ratingbar.getRating();
            if(!s.equals("")) {
                submitRating(s, i);
            } else {
                sheetratingBinding.etDes.setError("Required");
            }
        });
    }

    private void submitRating(String s, int i) {
        Call<RestResponse> call = RetrofitBuilder.create().setRating(Const.DEV_KEY, new SessionManager(getContext()).getUser().getData().getToken(),
                i, data.getOrderDetails().get(0).getOrderId(), s);
        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if(response.code() == 200 && (Objects.requireNonNull(response.body()).getStatus() == 200 || Objects.requireNonNull(response.body()).getStatus() == 400)) {
                    Toast.makeText(getContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }
                bottomSheetDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                bottomSheetDialog.dismiss();
            }
        });

    }

    private void setData() {
        OrderDetailRoot.OrderDetailsItem model = data.getOrderDetails().get(0);
        if(model != null) {
            if(model.getReview() != null) {
                binding.ratingbar.setRating(model.getReview().getRating());
                binding.ratingbar.setActivated(model.getReview().getRating() == 0);
            }

            binding.tvdate.setText(model.getOrderedAt());
            binding.tvOrderId.setText(model.getOrderId());
            binding.tvPaymentType.setText(model.getPaymentType());
            binding.tvOrderItems.setText(String.valueOf(model.getTotalItem()));
            binding.tvSheepingCharges.setText(getActivity().getString(R.string.currency) + model.getShippingCharge());
            binding.tvCoupenAmount.setText(getActivity().getString(R.string.currency) + model.getCouponDiscount());
            binding.tvSubAmount.setText(getActivity().getString(R.string.currency) + model.getSubTotal());
            binding.tvTotalAmount.setText(getActivity().getString(R.string.currency) + model.getTotalAmount());
            if(status.equals("Completed")) {
                binding.lytrating.setVisibility(View.VISIBLE);

            } else {
                binding.lytrating.setVisibility(View.GONE);
            }
        }

        OrderDetailRoot.DeliveryDetailsItem address = data.getDeliveryDetails().get(0);
        if(address != null) {
            String addressStr = address.getAddress();
            if(addressStr != null) {
                try {
                    Address.Datum addressObj = new Gson().fromJson(addressStr, Address.Datum.class);
                    if(addressObj != null) {
                        binding.tvUserName.setText(addressObj.getFirstName().concat(" " + addressObj.getLastName()));
                        binding.tvAddress.setText(addressObj.getHomeNo().concat(" " + addressObj.getSociety() + " " + addressObj.getStreet() + " " +
                                addressObj.getArea() + " " + addressObj.getCity() + " " + addressObj.getPincode()));
                        binding.tvMobile.setText(addressObj.getMobileNumber());

                    }
                } catch(JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
            binding.tvStatus.setText(address.getStatus());
            binding.tvEmail.setText(new SessionManager(getContext()).getUser().getData().getEmail());

            if(Objects.equals(address.getStatus(), "Completed")) {
                binding.tvStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_green));
            }
        }

    }

    private void initView() {
//ll
    }


}