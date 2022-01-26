package com.barathi.user.activities;

import static com.barathi.user.retrofit.Const.MOBILE_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.barathi.user.R;
import com.barathi.user.databinding.ActivityLoginBinding;
import com.barathi.user.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    String mobile,terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_sign_up);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

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

            Intent intent = new Intent(SignUpActivity.this, OTPActivity.class);
            intent.putExtra(MOBILE_KEY, mobile);
            startActivity(intent);

        });

    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }*/
}