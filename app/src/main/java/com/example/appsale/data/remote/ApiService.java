package com.example.appsale.data.remote;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.OrderHistory;
import com.example.appsale.data.remote.dto.CartDTO;
import com.example.appsale.data.remote.dto.OrderHistoryDTO;
import com.example.appsale.data.remote.dto.ProductDTO;
import com.example.appsale.data.remote.dto.UserDTO;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/user/sign-in")
    Call<AppResource<UserDTO>> signIn(@Body HashMap<String, Object> body);

    @POST("/user/sign-up")
    Call<AppResource<UserDTO>> register(@Body HashMap<String, Object> body);

    @GET("/product")
    Call<AppResource<List<ProductDTO>>> getProducts();

    @GET("/cart")
    Call<AppResource<CartDTO>> getCart();

    @POST("/cart/add")
    Call<AppResource<CartDTO>> addCart(@Body HashMap<String, Object> body);

    @POST("/order/history")
    Call<AppResource<List<OrderHistoryDTO>>> getListOrderHistory();

    @POST("/order/history")
    Call<AppResource<OrderHistoryDTO>> getOrderHistory();

}
