package com.example.appsale.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appsale.R;
import com.example.appsale.common.AppConstant;
import com.example.appsale.data.local.AppCache;
import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.Cart;
import com.example.appsale.data.model.Product;
import com.example.appsale.databinding.ActivityHomeBinding;
import com.example.appsale.databinding.LayoutDialogHomeBinding;
import com.example.appsale.presentation.view.adapter.ProductAdapter;
import com.example.appsale.presentation.viewmodel.HomeViewModel;
import com.example.appsale.utils.StringUtil;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding homeBinding;
    ProductAdapter productAdapter;
    HomeViewModel homeViewModel;
    TextView tvCountCart;

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
        homeViewModel.fetchCart();

//        homeBinding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.item_cart:
//                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
//                        break;
//                    case R.id.item_history:
//                        startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
//                        break;
//                    case R.id.item_home:
//                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
//                        break;
//                }
//                return true;
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        final MenuItem menuItem = menu.findItem(R.id.item_menu_cart);
        View actionView = menuItem.getActionView();
        tvCountCart = actionView.findViewById(R.id.text_cart_badge);
        setupBadge(0);
        actionView.setOnClickListener(view -> onOptionsItemSelected(menuItem));
        return true;
    }

    private void setupBadge(int qualities) {
        if (qualities == 0) {
            tvCountCart.setVisibility(View.GONE);
        } else {
            tvCountCart.setVisibility(View.VISIBLE);
            tvCountCart.setText(String.valueOf(Math.min(qualities, 99)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_cart:
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                break;
            case R.id.item_menu_history_order:
                startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
                break;
            case R.id.item_log_out:
                dialogLogOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("LOG OUT!!!");
        builder.setMessage("Thoát tài khoản và quay về màn hình đăng nhập?");
        builder.setIcon(R.drawable.icon_log_out);
        builder.setCancelable(false);

        builder.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppCache appCache = AppCache.getInstance(HomeActivity.this);
                appCache.clearCache();
                startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                finish();
            }
        });

        builder.setNegativeButton("KHÔNG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
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

        homeViewModel.getCart().observe(this, new Observer<AppResource<Cart>>() {
            @Override
            public void onChanged(AppResource<Cart> cartAppResource) {
                switch (cartAppResource.status) {
                    case ERROR:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        break;
                    case LOADING:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        int countBadge = 0;
                        for (int i = 0; i < cartAppResource.data.getProducts().size(); i++) {
                            countBadge += cartAppResource.data.getProducts().get(i).getQuantity();
                        }
                        setupBadge(countBadge);
                        break;
                }
            }
        });

        productAdapter.setOnItemClickFood(new ProductAdapter.OnItemClickProduct() {
            int indexGallery = 0;

            @Override
            public void onClick(int position, String nameButton, Product product) {
                switch (nameButton) {
                    case "add":
                        homeViewModel.addCart(productAdapter.getListProducts().get(position).getId(), HomeActivity.this);
                        break;
                    case "detail":
                        Dialog dialog = new Dialog(HomeActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        LayoutDialogHomeBinding binding = LayoutDialogHomeBinding.inflate(LayoutInflater.from(HomeActivity.this));
                        dialog.setContentView(binding.getRoot());

                        Window window = dialog.getWindow();

                        if (window != null) {
                            window.setGravity(Gravity.CENTER);
                            window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                        }
                        binding.textViewNameDialog.setText(product.getName());
                        binding.textViewAddressDialog.setText(product.getAddress());
                        binding.textViewPriceDialog.setText(String.format("%s VND", StringUtil.formatCurrency(product.getPrice())));

                        Glide.with(HomeActivity.this)
                                .load(AppConstant.BASE_URL + product.getGallery().get(0))
                                .placeholder(R.drawable.ic_logo)
                                .into(binding.imageViewDialog);

                        binding.buttonFrontDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                indexGallery = indexGallery == product.getGallery().size() - 1 ? -1 : indexGallery;
                                Glide.with(HomeActivity.this)
                                        .load(AppConstant.BASE_URL + product.getGallery().get(++indexGallery))
                                        .placeholder(R.drawable.ic_logo)
                                        .into(binding.imageViewDialog);
                            }
                        });

                        binding.buttonBackDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                indexGallery = indexGallery == 0 ? product.getGallery().size() : indexGallery;
                                Glide.with(HomeActivity.this)
                                        .load(AppConstant.BASE_URL + product.getGallery().get(--indexGallery))
                                        .placeholder(R.drawable.ic_logo)
                                        .into(binding.imageViewDialog);
                            }
                        });

                        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                homeViewModel.addCart(productAdapter.getListProducts().get(position).getId(), HomeActivity.this);
                                dialog.dismiss();
                                indexGallery = 0;
                            }
                        });
                        dialog.show();
                        break;
                }
            }
        });
    }

    private void observerData() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        homeViewModel.fetchCart();
        homeViewModel.getCart().observe(this, new Observer<AppResource<Cart>>() {
            @Override
            public void onChanged(AppResource<Cart> cartAppResource) {
                switch (cartAppResource.status) {
                    case ERROR:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        if (cartAppResource.data == null) {
                            setupBadge(0);
                        }
                        break;
                    case LOADING:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        int countBadge = 0;
                        for (int i = 0; i < cartAppResource.data.getProducts().size(); i++) {
                            countBadge += cartAppResource.data.getProducts().get(i).getQuantity();
                        }
                        setupBadge(countBadge);
                        break;
                }
            }
        });
    }
}