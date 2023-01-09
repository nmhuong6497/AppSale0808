package com.example.appsale.presentation.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.appsale.R;
import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.User;
import com.example.appsale.presentation.viewmodel.SignInViewModel;
import com.example.appsale.utils.SpannedUtil;

public class SignInActivity extends AppCompatActivity {

    SignInViewModel signInViewModel;
    EditText edtEmail, edtPassword;
    LinearLayout linearSignIn, loadingView, linearDefaultUser;
    TextView tvRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SignInViewModel(SignInActivity.this);
            }
        }).get(SignInViewModel.class);

        initData();
        observer();
        event();
    }

    private void initData() {
        loadingView = findViewById(R.id.layout_loading);
        linearSignIn = findViewById(R.id.sign_in);
        edtEmail = findViewById(R.id.textEditEmail);
        edtPassword = findViewById(R.id.textEditPassword);
        tvRegister = findViewById(R.id.text_view_register);
        linearDefaultUser = findViewById(R.id.layout_default_user);
    }

    private void observer() {
        signInViewModel.getUserResource().observe(this, new Observer<AppResource<User>>() {
            @Override
            public void onChanged(AppResource<User> userAppResource) {
                switch (userAppResource.status) {
                    case ERROR:
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, userAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        loadingView.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        loadingView.setVisibility(View.GONE);
                        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                        finish();
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void event() {
        linearDefaultUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtEmail.setText("nm.huong6497@gmail.com");
                edtPassword.setText("123456789");
            }
        });

        linearSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Bạn chưa nhập đủ thông tin!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                signInViewModel.signIn(email, password);
            }
        });

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("Don't have an account?");
        spannableStringBuilder.append(SpannedUtil.setClickColorLink("Register", this, new SpannedUtil.OnListenClick() {
            @Override
            public void onClick() {
                startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
            }
        }));
        tvRegister.setText(spannableStringBuilder);
        tvRegister.setHighlightColor(Color.TRANSPARENT);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }
}