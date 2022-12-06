package com.example.appsale.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appsale.R;
import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.Product;
import com.example.appsale.data.model.User;
import com.example.appsale.databinding.ActivityHomeBinding;
import com.example.appsale.presentation.view.adapter.ProductAdapter;
import com.example.appsale.presentation.viewmodel.HomeViewModel;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding homeBinding;
    ProductAdapter productAdapter;
    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new HomeViewModel(HomeActivity.this);
            }
        }).get(HomeViewModel.class);

        initData();
        observerData();
        event();
    }

    private void initData() {
        productAdapter = new ProductAdapter();
        homeBinding.recyclerViewProduct.setAdapter(productAdapter);
        homeBinding.recyclerViewProduct.setHasFixedSize(true);

        // Toolbar
        setSupportActionBar(homeBinding.toolbarHome);
        homeViewModel.fetchProducts();
    }

    private void event() {
        homeViewModel.getListProducts().observe(this, new Observer<AppResource<List<Product>>>() {
            @Override
            public void onChanged(AppResource<List<Product>> listAppResource) {
                switch (listAppResource.status) {
                    case ERROR:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        Toast.makeText(HomeActivity.this, listAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        productAdapter.updateListProduct(listAppResource.data);
                        break;
                }
            }
        });
    }

    private void observerData() {

    }
}