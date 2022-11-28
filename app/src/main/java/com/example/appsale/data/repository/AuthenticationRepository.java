package com.example.appsale.data.repository;

import android.content.Context;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.remote.ApiService;
import com.example.appsale.data.remote.RetrofitClient;
import com.example.appsale.data.remote.dto.UserDTO;

import java.util.HashMap;

import retrofit2.Call;

public class AuthenticationRepository {
    private ApiService apiService;

        public AuthenticationRepository(Context context) {
        apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public Call<AppResource<UserDTO>> signIn(String email, String password) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        return apiService.signIn(body);
    }

    public Call<AppResource<UserDTO>> register(String email, String password, String name, String phone, String address) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("name", name);
        body.put("phone", phone);
        body.put("address", address);

        return apiService.register(body);
    }
}
