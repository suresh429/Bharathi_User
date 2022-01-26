package com.barathi.user.activities;

import static com.barathi.user.retrofit.Const.MOBILE_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.barathi.user.R;
import com.barathi.user.databinding.ActivityOtpBinding;
import com.barathi.user.databinding.ActivitySignUpBinding;

import in.aabhasjindal.otptextview.OTPListener;

public class OTPActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    String deviceId, refreshedToken, otpText, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_otp);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);


        //progressIndicator.start();
        if (getIntent() != null) {
            mobile = getIntent().getStringExtra(MOBILE_KEY);

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
}