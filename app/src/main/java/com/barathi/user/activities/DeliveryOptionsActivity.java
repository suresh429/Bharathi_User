package com.barathi.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.barathi.user.R;
import com.barathi.user.SessionManager;
import com.barathi.user.adapters.DeliveryAddressOptionsAdapter;
import com.barathi.user.databinding.ActivityDeliveryOptionsBinding;
import com.barathi.user.model.Address;
import com.barathi.user.retrofit.RetrofitBuilder;
import com.barathi.user.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryOptionsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityDeliveryOptionsBinding binding;
    SessionManager sessionManager;
    private String token;
    private DeliveryAddressOptionsAdapter deliveryAddressOptionsAdapter;
    private String addressId;
    private boolean isEmpty = false;
    private String lat = "";
    private String lang = "";
    private Address.Datum addressObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_options);
        sessionManager = new SessionManager(this);
        token = sessionManager.getUser().getData().getToken();

        initView();


        getData();

        initListnear();
        binding.swipe.setOnRefreshListener(this);
    }


    private void getData() {
        if(!binding.swipe.isRefreshing()) {
            binding.pd.setVisibility(View.VISIBLE);

        }
        binding.lyt404.setVisibility(View.GONE);

        RetrofitService service = RetrofitBuilder.create();
        Call<Address> call = service.getAllAddress("gng!123", token);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if(response.code() == 200) {
                    if(response.body().getStatus() == 200 && !response.body().getData().isEmpty()) {
                        deliveryAddressOptionsAdapter.addData(response.body().getData());
                        binding.rvAddress.setAdapter(deliveryAddressOptionsAdapter);
                        binding.pd.setVisibility(View.GONE);
                        addressId = Objects.requireNonNull(response.body()).getData().get(0).getDeliveryAddressId();
                        addressObj = Objects.requireNonNull(response.body()).getData().get(0);
                        lat = Objects.requireNonNull(response.body()).getData().get(0).getLatitude();
                        lang = Objects.requireNonNull(response.body()).getData().get(0).getLongitude();
                        isEmpty = false;
                        binding.tvPay.setText("Proceed To Pay");
                    } else if (response.body().getData().isEmpty()) {
                        isEmpty = true;
                        binding.tvPay.setText("Add New Address");
                        Toast.makeText(DeliveryOptionsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                    binding.swipe.setRefreshing(false);
                    binding.pd.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
//ll
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void initListnear() {
        binding.tvPay.setOnClickListener(v -> {
            if(isEmpty) {
                startActivity(new Intent(this, AddDeliveryAddressActivity.class));
            } else {
                Intent intent = new Intent(this, PaymentSummaryActivity.class);
                intent.putExtra("addressId", addressId);
                intent.putExtra("address", new Gson().toJson(addressObj));
                intent.putExtra("lat", lat);
                intent.putExtra("lang", lang);
                startActivity(intent);
            }

        });
    }

    private void initView() {


        deliveryAddressOptionsAdapter = new DeliveryAddressOptionsAdapter();
        deliveryAddressOptionsAdapter.setOnAddressSelectListnear(address -> {
            addressId = address.getDeliveryAddressId();
            lat = address.getLatitude();
            lang = address.getLongitude();
            addressObj = address;
        });
    }

    @Override
    public void onRefresh() {
        getData();

        binding.lyt404.setVisibility(View.GONE);
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickadd(View view) {
        startActivity(new Intent(this, AddDeliveryAddressActivity.class));

    }
}