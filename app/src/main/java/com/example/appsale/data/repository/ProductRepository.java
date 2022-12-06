package com.example.appsale.data.repository;

import android.content.Context;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.remote.ApiService;
import com.example.appsale.data.remote.RetrofitClient;
import com.example.appsale.data.remote.dto.ProductDTO;
import com.example.appsale.data.remote.dto.UserDTO;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class ProductRepository {
    private ApiService apiService;

    public ProductRepository(Context context) {
        apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public Call<AppResource<List<ProductDTO>>> getProducts() {
        return apiService.getProducts();
    }
}
