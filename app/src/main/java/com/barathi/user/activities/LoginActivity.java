package com.barathi.user.activities;

import static com.barathi.user.retrofit.Const.MOBILE_KEY;
import static com.barathi.user.retrofit.Const.OTP_KEY;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.barathi.user.R;
import com.barathi.user.SessionManager;
import com.barathi.user.databinding.ActivityLoginBinding;
import com.barathi.user.databinding.ActivityMainBinding;
import com.barathi.user.model.User;
import com.barathi.user.retrofit.Const;
import com.barathi.user.retrofit.RetrofitBuilder;
import com.barathi.user.retrofit.RetrofitService;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    RetrofitService service;
    SessionManager sessionManager;
    String errorMsg;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        service = RetrofitBuilder.create();
        sessionManager = new SessionManager(this);


        binding.btnContinue.setAlpha(.5f);
        binding.btnContinue.setEnabled(false);
        binding.edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = String.valueOf(charSequence.length());

                if (value.equals("10")) {

                    binding.btnContinue.setEnabled(true);
                    binding.btnContinue.setAlpha(.9f);
                    binding.btnContinue.setText("CONTINUE");
                    binding.btnContinue.setClickable(true);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.edtMobile.getWindowToken(), 0);

                } else {
                    binding.btnContinue.setAlpha(.5f);
                    binding.btnContinue.setEnabled(false);
                    binding.btnContinue.setText("ENTER MOBILE NUMBER");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.edtMobile.getText()).toString().isEmpty()) {

                loginUser();

            } else {
                Toast.makeText(LoginActivity.this, "Enter Mobile no.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void loginUser() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        String notificationToken = sessionManager.getStringValue(Const.NOTIFICATION_TOKEN);
        Call<User> call = service.loginUser(Const.DEV_KEY, Objects.requireNonNull(binding.edtMobile.getText()).toString(), "1", notificationToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@Nullable Call<User> call, @Nullable Response<User> response) {
                binding.progressCircular.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                if (response.isSuccessful() && response.code() == 200) {
                    if (Objects.requireNonNull(response.body()).getStatus() == 200) {
                        sessionManager.saveUser(response.body());
                        /*  sessionManager.saveBooleanValue(Const.IS_LOGIN, true);*/
                        sessionManager.saveStringValue(Const.USER_TOKEN, response.body().getData().getToken());

                        String[] separated = response.body().getMessage().split("\\.");

                        Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                        intent.putExtra(MOBILE_KEY, binding.edtMobile.getText().toString());
                        intent.putExtra(OTP_KEY, separated[1].trim());
                        startActivity(intent);

                    } else if (Objects.requireNonNull(response.body()).getStatus() == 401){
                        errorMsg = new GsonBuilder().setPrettyPrinting().create().toJson(Objects.requireNonNull(response.body()).getMessage());
                        Log.d(TAG, "onResponse: "+errorMsg);
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        intent.putExtra(MOBILE_KEY, binding.edtMobile.getText().toString());
                        startActivity(intent);

                    }
                } else if (response.code() == 401) {
                    errorMsg = new GsonBuilder().setPrettyPrinting().create().toJson(Objects.requireNonNull(response.message()));
                    Toast.makeText(LoginActivity.this, "" + errorMsg, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@Nullable Call<User> call, @Nullable Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                binding.progressCircular.setVisibility(View.GONE);

            }
        });
    }

}