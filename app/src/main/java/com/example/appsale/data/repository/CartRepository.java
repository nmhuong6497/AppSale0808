package com.example.appsale.data.repository;

import android.content.Context;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.remote.ApiService;
import com.example.appsale.data.remote.RetrofitClient;
import com.example.appsale.data.remote.dto.CartDTO;
import com.example.appsale.data.remote.dto.ProductDTO;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class CartRepository {
    private ApiService apiService;

    public CartRepository(Context context) {
        apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public Call<AppResource<CartDTO>> getCart() {
        return apiService.getCart();
    }

    public Call<AppResource<CartDTO>> addCart(String idProduct) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id_product", idProduct);
        return apiService.addCart(map);
    }

    public Call<AppResource<CartDTO>> updateCart(String idProduct, String idCart, int quantity) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id_product", idProduct);
        map.put("id_cart", idCart);
        map.put("quantity", quantity);
        return apiService.updateCart(map);
    }

    public  Call<AppResource<CartDTO>> cartConform(String idCart) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id_cart", idCart);
        return apiService.cartConform(map);
    }
}
