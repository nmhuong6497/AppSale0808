package com.example.appsale.presentation.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.Cart;
import com.example.appsale.data.model.Product;
import com.example.appsale.data.remote.dto.CartDTO;
import com.example.appsale.data.remote.dto.ProductDTO;
import com.example.appsale.data.repository.CartRepository;
import com.example.appsale.data.repository.ProductRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<AppResource<List<Product>>> listProducts = new MutableLiveData<>();
    private MutableLiveData<AppResource<Cart>> cart = new MutableLiveData<>();
    private ProductRepository productRepository;
    private CartRepository cartRepository;

    public HomeViewModel(Context context) {
        productRepository = new ProductRepository(context);
        cartRepository = new CartRepository(context);
    }

    public LiveData<AppResource<List<Product>>> getListProducts() {
        return listProducts;
    }

    public LiveData<AppResource<Cart>> getCart() {
        return cart;
    }

    public void fetchProducts() {
        listProducts.setValue(new AppResource.Loading<>(null));
        Call<AppResource<List<ProductDTO>>> callSignIn = productRepository.getProducts();
        callSignIn.enqueue(new Callback<AppResource<List<ProductDTO>>>() {
            @Override
            public void onResponse(Call<AppResource<List<ProductDTO>>> call, Response<AppResource<List<ProductDTO>>> response) {
                if (response.isSuccessful()) {
                    List<ProductDTO> listProductDTOs = response.body().data;
                    List<Product> listProduct = new ArrayList<>();
                    for (ProductDTO productDTO: listProductDTOs) {
                        listProduct.add(new Product(
                                productDTO.getId(),
                                productDTO.getName(),
                                productDTO.getAddress(),
                                productDTO.getPrice(),
                                productDTO.getImg(),
                                productDTO.getQuantity(),
                                productDTO.getGallery()
                        ));
                        
                    }
                    listProducts.setValue(new AppResource.Success<>(listProduct));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        listProducts.setValue(new AppResource.Error<>(message));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AppResource<List<ProductDTO>>> call, Throwable t) {
                listProducts.setValue(new AppResource.Error<>(t.getMessage()));
            }
        });
    }

    public void fetchCart() {
        cart.setValue(new AppResource.Loading(null));
        Call<AppResource<CartDTO>> callCart = cartRepository.getCart();
        callCart.enqueue(new Callback<AppResource<CartDTO>>() {
            @Override
            public void onResponse(Call<AppResource<CartDTO>> call, Response<AppResource<CartDTO>> response) {
                if (response.isSuccessful()) {
                    CartDTO cartDTO = response.body().data;
                    List<Product> listProduct = new ArrayList<>();
                    for (ProductDTO productDTO : cartDTO.getProducts()) {
                        listProduct.add(new Product(
                                productDTO.getId(),
                                productDTO.getName(),
                                productDTO.getAddress(),
                                productDTO.getPrice(),
                                productDTO.getImg(),
                                productDTO.getQuantity(),
                                productDTO.getGallery())
                        );
                    }
                    cart.setValue(new AppResource.Success<>(new Cart(cartDTO.getId(), listProduct, cartDTO.getIdUser(), cartDTO.getPrice())));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        cart.setValue(new AppResource.Error<>(message));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AppResource<CartDTO>> call, Throwable t) {
                cart.setValue(new AppResource.Error<>(t.getMessage()));
            }
        });
    }
}
