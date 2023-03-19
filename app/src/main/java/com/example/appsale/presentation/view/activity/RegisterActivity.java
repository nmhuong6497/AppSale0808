package com.example.appsale.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Toast;

import com.example.appsale.R;
import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.User;
import com.example.appsale.databinding.ActivityRegisterBinding;
import com.example.appsale.presentation.viewmodel.RegisterViewModel;
import com.example.appsale.utils.SpannedUtil;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new RegisterViewModel(RegisterActivity.this);
            }
        }).get(RegisterViewModel.class);

        observer();
        event();
    }

    private void observer() {
        registerViewModel.getUserResource().observe(this, new Observer<AppResource<User>>() {
            @Override
            public void onChanged(AppResource<User> userAppResource) {
                switch (userAppResource.status) {
                    case ERROR:
                        binding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, userAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        binding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        binding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        finish();
                        break;
                }
            }
        });
    }

    private void event() {
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.textEditEmail.getText().toString();
                String password = binding.textEditPassword.getText().toString();
                String name = binding.textEditName.getText().toString();
                String phone = binding.textEditPhone.getText().toString();
                String address = binding.textEditAddress.getText().toString();

                if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Bạn chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerViewModel.register(email, password, name, phone, address);
            }
        });

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("Bạn đã có tài khoản?");
        spannableStringBuilder.append(SpannedUtil.setClickColorLink(" Đăng nhập", this, new SpannedUtil.OnListenClick() {
            @Override
            public void onClick() {
                finish();
            }
        }));
        binding.textViewLogin.setText(spannableStringBuilder);
        binding.textViewLogin.setHighlightColor(Color.TRANSPARENT);
        binding.textViewLogin.setMovementMethod(LinkMovementMethod.getInstance());
    }
}