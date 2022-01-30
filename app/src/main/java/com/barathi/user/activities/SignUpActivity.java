package com.barathi.user.activities;

import static com.barathi.user.retrofit.Const.MOBILE_KEY;
import static com.barathi.user.retrofit.Const.OTP_KEY;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.barathi.user.R;
import com.barathi.user.SessionManager;
import com.barathi.user.adapters.CartAdapter;
import com.barathi.user.databinding.ActivityLoginBinding;
import com.barathi.user.databinding.ActivitySignUpBinding;
import com.barathi.user.fragments.CartFragment;
import com.barathi.user.model.User;
import com.barathi.user.retrofit.Const;
import com.barathi.user.retrofit.RetrofitBuilder;
import com.barathi.user.retrofit.RetrofitService;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    String mobile, terms;
    RetrofitService service;
    SessionManager sessionManager;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_sign_up);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        service = RetrofitBuilder.create();
        sessionManager = new SessionManager(this);

        if (getIntent() != null) {
            mobile = getIntent().getStringExtra(MOBILE_KEY);
            binding.mobileEdt.setText(mobile);
        }

        binding.btnSignup.setEnabled(false);
        binding.btnSignup.setAlpha(0.5f);
        binding.btnSignup.setClickable(false);
        terms = "";
        binding.tCChk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.btnSignup.setEnabled(true);
                binding.btnSignup.setAlpha(0.9f);
                binding.btnSignup.setClickable(true);
                terms = "terms";
            } else {
                binding.btnSignup.setEnabled(false);
                binding.btnSignup.setAlpha(0.5f);
                binding.btnSignup.setClickable(false);
                terms = "";
            }
        });


        // close button
        binding.closeImg.setOnClickListener(v -> {
            /*Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);*/
            onBackPressed();
        });

        // registration
        binding.btnSignup.setOnClickListener(v -> {

            if (!Objects.requireNonNull(binding.mobileEdt.getText()).toString().isEmpty()
                    || !Objects.requireNonNull(binding.firstNameEdt.getText()).toString().isEmpty()
                    || !Objects.requireNonNull(binding.lastNameEdt.getText()).toString().isEmpty()
                    || Objects.requireNonNull(binding.emailEdt.getText()).toString().isEmpty()) {
                registerUser();
            } else {
                Toast.makeText(this, "Please Enter All Fields .", Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void registerUser() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        String notificationToken = sessionManager.getStringValue(Const.NOTIFICATION_TOKEN);
        Call<User> call = service.registerUser(Const.DEV_KEY, Objects.requireNonNull(binding.mobileEdt.getText()).toString(),
                Objects.requireNonNull(binding.firstNameEdt.getText()).toString(), Objects.requireNonNull(binding.lastNameEdt.getText()).toString(), Objects.requireNonNull(binding.emailEdt.getText()).toString(),
                "normal", Objects.requireNonNull(binding.emailEdt.getText()).toString(), "1", notificationToken, "");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@Nullable Call<User> call, @Nullable Response<User> response) {
                binding.progressCircular.setVisibility(View.GONE);

                assert response != null;

                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getStatus() == 200) {
                    Log.d(TAG, "onResponse: " + new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                    sessionManager.saveUser(response.body());
                    sessionManager.saveBooleanValue(Const.IS_LOGIN, true);
                    sessionManager.saveStringValue(Const.USER_TOKEN, response.body().getData().getToken());

                   /* String[] separated = response.body().getMessage().split(".");
                    Log.d(TAG, "onResponse: " + separated[1].trim());*/

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                   /* intent.putExtra(MOBILE_KEY, mobile);
                    intent.putExtra(OTP_KEY, separated[1].trim());*/
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    String errorMsg = new GsonBuilder().setPrettyPrinting().create().toJson(Objects.requireNonNull(response.body()).getMessage());
                    Log.d(TAG, "onResponse Else: " + errorMsg);
                    Toast.makeText(SignUpActivity.this, "" + errorMsg, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@Nullable Call<User> call, @Nullable Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                binding.progressCircular.setVisibility(View.GONE);

            }
        });
    }


   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }*/
}