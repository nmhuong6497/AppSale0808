package com.example.appsale.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.Cart;
import com.example.appsale.data.model.Product;
import com.example.appsale.data.repository.CartRepository;
import com.example.appsale.databinding.ActivityCartBinding;
import com.example.appsale.presentation.view.adapter.CartAdapter;
import com.example.appsale.presentation.view.adapter.ProductAdapter;
import com.example.appsale.presentation.viewmodel.HomeViewModel;
import com.example.appsale.utils.StringUtil;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding cartBinding;
    CartAdapter cartAdapter;
    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(cartBinding.getRoot());

        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new HomeViewModel(CartActivity.this);
            }
        }).get(HomeViewModel.class);

        initData();
        event();
    }

    private void event() {
        homeViewModel.getCart().observe(this, new Observer<AppResource<Cart>>() {
            @Override
            public void onChanged(AppResource<Cart> cartAppResource) {
                switch (cartAppResource.status) {
                    case ERROR:
                        cartBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, cartAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        cartBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        cartBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        cartAdapter.updateCart(cartAppResource.data.getProducts());
                        cartBinding.textViewTotalPriceCart.setText(String.format("%s VND", StringUtil.formatCurrency(cartAppResource.data.getPrice())));
                        cartBinding.layoutTotalPriceCart.setVisibility(View.VISIBLE);
                        cartBinding.recyclerViewCart.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        cartAdapter.setOnItemClickCart(new CartAdapter.OnItemClickCart() {
            @Override
            public void onClick(int position, boolean isQuantityUp) {
                if (isQuantityUp) {
                    homeViewModel.updateCart(homeViewModel.getCart().getValue().data.getProducts().get(position).getId(), homeViewModel.getCart().getValue().data.getId(), homeViewModel.getCart().getValue().data.getProducts().get(position).getQuantity() + 1);
                    cartBinding.recyclerViewCart.setVisibility(View.GONE);
                    cartBinding.layoutTotalPriceCart.setVisibility(View.GONE);
                } else {
                    homeViewModel.updateCart(homeViewModel.getCart().getValue().data.getProducts().get(position).getId(), homeViewModel.getCart().getValue().data.getId(), homeViewModel.getCart().getValue().data.getProducts().get(position).getQuantity() - 1);
                    cartBinding.recyclerViewCart.setVisibility(View.GONE);
                    cartBinding.layoutTotalPriceCart.setVisibility(View.GONE);
                }

            }
        });

        cartBinding.buttonCreateOrderCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeViewModel.cartConform(homeViewModel.getCart().getValue().data.getId());
                cartBinding.layoutTotalPriceCart.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, "Tạo đơn thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initData() {
        cartAdapter = new CartAdapter();
        cartBinding.recyclerViewCart.setAdapter(cartAdapter);
        cartBinding.recyclerViewCart.setHasFixedSize(true);

        homeViewModel.fetchCart();
    }
}
