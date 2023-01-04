package com.example.appsale.presentation.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.OrderHistory;
import com.example.appsale.data.model.Product;
import com.example.appsale.data.remote.dto.OrderHistoryDTO;
import com.example.appsale.data.remote.dto.ProductDTO;
import com.example.appsale.data.repository.OrderHistoryRepository;
import com.example.appsale.data.repository.ProductRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryViewModel extends ViewModel {
    private MutableLiveData<AppResource<List<Product>>> listProducts = new MutableLiveData<>();
    private MutableLiveData<AppResource<List<OrderHistory>>> listOrderHistory = new MutableLiveData<>();
    private MutableLiveData<AppResource<OrderHistory>> orderHistory = new MutableLiveData<>();
    private ProductRepository productRepository;
    private OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryViewModel(Context context) {
        productRepository = new ProductRepository(context);
        orderHistoryRepository = new OrderHistoryRepository(context);
    }

    public LiveData<AppResource<List<Product>>> getListProducts() {
        return listProducts;
    }

    public LiveData<AppResource<List<OrderHistory>>> getListOrderHistory() {
        return listOrderHistory;
    }

    public LiveData<AppResource<OrderHistory>> getOrderHistory() {
        return orderHistory;
    }

    public void fetchOrderHistory() {
//        orderHistory.setValue(new AppResource.Loading<>(null));
//        Call<AppResource<OrderHistoryDTO>> callOrderHistory = orderHistoryRepository.getOrderHistoryDTO();
//        callOrderHistory.enqueue(new Callback<AppResource<OrderHistoryDTO>>() {
//            @Override
//            public void onResponse(Call<AppResource<OrderHistoryDTO>> call, Response<AppResource<OrderHistoryDTO>> response) {
//                OrderHistoryDTO orderHistoryDTO = response.body().data;
//                List<Product> productList = new ArrayList<>();
//                for (ProductDTO productDTO: orderHistoryDTO.getProducts()) {
//                    productList.add(new Product(
//                            productDTO.getId(),
//                            productDTO.getName(),
//                            productDTO.getAddress(),
//                            productDTO.getPrice(),
//                            productDTO.getImg(),
//                            productDTO.getQuantity(),
//                            productDTO.getGallery()
//                    ));
//                }
//                orderHistory.setValue(new AppResource.Success<>(new OrderHistory(orderHistoryDTO.getId(),productList, orderHistoryDTO.getIdUser(), orderHistoryDTO.getPrice(), orderHistoryDTO.getStatus(), orderHistoryDTO.getDateCreated(), orderHistoryDTO.getV())));
//            }
//
//            @Override
//            public void onFailure(Call<AppResource<OrderHistoryDTO>> call, Throwable t) {
//                orderHistory.setValue(new AppResource.Error<>(t.getMessage()));
//            }
//        });

        listOrderHistory.setValue(new AppResource.Loading(null));
        Call<AppResource<List<OrderHistoryDTO>>> callListOrderHistory = orderHistoryRepository.getListOrderHistoryDTO();
        callListOrderHistory.enqueue(new Callback<AppResource<List<OrderHistoryDTO>>>() {
            @Override
            public void onResponse(Call<AppResource<List<OrderHistoryDTO>>> call, Response<AppResource<List<OrderHistoryDTO>>> response) {
                if (response.isSuccessful()) {
                    List<OrderHistoryDTO> orderHistoryDTOS = response.body().data;
                    List<OrderHistory> orderHistoryList = new ArrayList<>();
                    for (OrderHistoryDTO orderHistoryDTO: orderHistoryDTOS) {
                        orderHistoryList.add(new OrderHistory(
                                orderHistoryDTO.getId(),
                                orderHistoryDTO.getProducts(),
                                orderHistoryDTO.getIdUser(),
                                orderHistoryDTO.getPrice(),
                                orderHistoryDTO.getStatus(),
                                orderHistoryDTO.getDateCreated(),
                                orderHistoryDTO.getV()
                        ));
                    }
                    listOrderHistory.setValue(new AppResource.Success<>(orderHistoryList));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        listOrderHistory.setValue(new AppResource.Error<>(message));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AppResource<List<OrderHistoryDTO>>> call, Throwable t) {
                listOrderHistory.setValue(new AppResource.Error<>(t.getMessage()));
            }
        });
    }
}
