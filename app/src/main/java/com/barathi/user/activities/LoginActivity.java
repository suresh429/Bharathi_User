package com.barathi.user.activities;

import static com.barathi.user.retrofit.Const.MOBILE_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.barathi.user.R;
import com.barathi.user.databinding.ActivityLoginBinding;
import com.barathi.user.databinding.ActivityMainBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


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
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.putExtra(MOBILE_KEY, Objects.requireNonNull(binding.edtMobile.getText()).toString());
                startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this, "Enter Mobile no.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}