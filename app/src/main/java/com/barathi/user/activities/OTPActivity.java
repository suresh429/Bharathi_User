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
    String deviceId, refreshedToken, otpValue,enterOtpValue, mobile;
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
            otpValue = getIntent().getStringExtra(OTP_KEY);

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
                enterOtpValue = otp;
                Log.d("TAG", "onOTPComplete: "+otp);
            }
        });

        binding.verifyBtn.setOnClickListener(v -> {
            if ((otpValue == null || otpValue.equals("")) || otpValue.length() != 6) {
                Toast.makeText(OTPActivity.this, "Enter OTP to Verify", Toast.LENGTH_SHORT).show();
                return;
            } else if (!Objects.equals(enterOtpValue, otpValue)){
                Toast.makeText(OTPActivity.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                return;
            }else {
                Log.d("TAG", "onCreate: "+"valid");
                sessionManager.saveBooleanValue(Const.IS_LOGIN, true);
                Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


}