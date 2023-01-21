package com.example.appsale.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.Cart;
import com.example.appsale.data.repository.CartRepository;
import com.example.appsale.databinding.ActivityCartBinding;
import com.example.appsale.presentation.view.adapter.CartAdapter;
import com.example.appsale.presentation.viewmodel.HomeViewModel;
import com.example.appsale.utils.StringUtil;

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
                        Toast.makeText(CartActivity.this, cartAppResource.message, Toast.LENGTH_SHORT).show();
                        cartBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        cartBinding.layoutNullCart.setVisibility(View.VISIBLE);
                        cartBinding.layoutCart.setVisibility(View.GONE);
                        break;
                    case LOADING:
                        cartBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        cartBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        if (cartAppResource.data.getProducts().size() > 0) {
                            cartBinding.layoutNullCart.setVisibility(View.GONE);
                            cartBinding.layoutCart.setVisibility(View.VISIBLE);
                        } else {
                            cartBinding.layoutNullCart.setVisibility(View.VISIBLE);
                            cartBinding.layoutCart.setVisibility(View.GONE);
                        }
                        cartAdapter.updateCart(cartAppResource.data);
                        if (cartAppResource.data == null) {
                            startActivity(new Intent(CartActivity.this, HomeActivity.class));
                        }
                        cartBinding.textViewTotalPriceCart.setText(String.format("%s VND", StringUtil.formatCurrency(cartAppResource.data.getPrice())));
                        break;
                }
            }
        });

        cartAdapter.setOnItemClickCart(new CartAdapter.OnItemClickCart() {
            @Override
            public void onClick(int position, boolean isQuantityUp) {
                if (isQuantityUp) {
                    homeViewModel.updateCart(cartAdapter.getListProductOfCart().get(position).getId(), cartAdapter.getCart().getId(), cartAdapter.getListProductOfCart().get(position).getQuantity() + 1);
                } else {
                    homeViewModel.updateCart(cartAdapter.getListProductOfCart().get(position).getId(), cartAdapter.getCart().getId(), cartAdapter.getListProductOfCart().get(position).getQuantity() - 1);
                }
            }

            @Override
            public void onLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setMessage("Xóa món \"" + cartAdapter.getListProductOfCart().get(position).getName() + "\" ?");
                builder.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        homeViewModel.updateCart(cartAdapter.getListProductOfCart().get(position).getId(), cartAdapter.getCart().getId(), 0);
                    }
                });

                builder.setNegativeButton("KHÔNG", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });

        cartBinding.buttonCreateOrderCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeViewModel.cartConform(cartAdapter.getCart().getId(), CartActivity.this);
            }
        });

        cartBinding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
