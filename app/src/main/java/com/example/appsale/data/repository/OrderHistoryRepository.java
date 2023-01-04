package com.example.appsale.data.repository;

import android.content.Context;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.remote.ApiService;
import com.example.appsale.data.remote.RetrofitClient;
import com.example.appsale.data.remote.dto.OrderHistoryDTO;


import java.util.List;

import retrofit2.Call;

public class OrderHistoryRepository {
    private ApiService apiService;

    public OrderHistoryRepository(Context context) {
        apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public Call<AppResource<List<OrderHistoryDTO>>> getListOrderHistoryDTO() {
        return apiService.getListOrderHistory();
    }

    public Call<AppResource<OrderHistoryDTO>> getOrderHistoryDTO() {
        return apiService.getOrderHistory();
    }
}
