package com.barathi.user.activities;

import static com.barathi.user.retrofit.Const.MOBILE_KEY;
import static com.barathi.user.retrofit.Const.OTP_KEY;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.barathi.user.R;
import com.barathi.user.SessionManager;
import com.barathi.user.databinding.ActivityOtpBinding;
import com.barathi.user.databinding.ActivitySignUpBinding;
import com.barathi.user.model.User;
import com.barathi.user.retrofit.Const;
import com.barathi.user.retrofit.RetrofitBuilder;
import com.barathi.user.retrofit.RetrofitService;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import in.aabhasjindal.otptextview.OTPListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    String deviceId, refreshedToken, otpText, mobile;
    RetrofitService service;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_otp);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        service = RetrofitBuilder.create();
        sessionManager = new SessionManager(this);

        //progressIndicator.start();
        if (getIntent() != null) {
            mobile = getIntent().getStringExtra(MOBILE_KEY);
            otpText = getIntent().getStringExtra(OTP_KEY);

            char first = mobile.charAt(0);
            String substring = mobile.substring(Math.max(mobile.length() - 2, 0));
            binding.textView.setText("Please type the verification code sent to\n +91" + first + "XXXXXXX" + substring);
        }

        binding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                otpText = otp;
            }
        });

        binding.verifyBtn.setOnClickListener(v -> {
            if ((otpText == null || otpText.equals("")) || otpText.length() != 6) {
                Toast.makeText(OTPActivity.this, "Enter OTP to Verify", Toast.LENGTH_SHORT).show();
                return;
            } else {
               // otpVerify();
            }
        });
    }

  /*  private void otpVerify() {
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
                   *//* sessionManager.saveUser(response.body());
                    sessionManager.saveBooleanValue(Const.IS_LOGIN, true);
                    sessionManager.saveStringValue(Const.USER_TOKEN, response.body().getData().getToken());
*//*

                    String[] separated = response.body().getMessage().split(".");
                    Log.d(TAG, "onResponse: " + separated[1].trim());

                    Intent intent = new Intent(SignUpActivity.this, OTPActivity.class);
                    intent.putExtra(MOBILE_KEY, mobile);
                    intent.putExtra(OTP_KEY, separated[1].trim());
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
    }*/

}