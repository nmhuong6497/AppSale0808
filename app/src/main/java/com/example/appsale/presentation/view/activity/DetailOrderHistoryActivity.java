package com.example.appsale.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appsale.R;
import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.OrderHistory;
import com.example.appsale.data.model.Product;
import com.example.appsale.data.remote.dto.ProductDTO;
import com.example.appsale.databinding.ActivityDetailOrderHistoryBinding;
import com.example.appsale.databinding.ActivityOrderHistoryBinding;
import com.example.appsale.presentation.view.adapter.DetailOrderHistoryAdapter;
import com.example.appsale.presentation.view.adapter.OrderHistoryAdapter;
import com.example.appsale.presentation.viewmodel.OrderHistoryViewModel;
import com.example.appsale.utils.StringUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DetailOrderHistoryActivity extends AppCompatActivity {
    ActivityDetailOrderHistoryBinding detailOrderHistoryBinding;
    DetailOrderHistoryAdapter detailOrderHistoryAdapter;
    OrderHistoryViewModel orderHistoryViewModel;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailOrderHistoryBinding = ActivityDetailOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(detailOrderHistoryBinding.getRoot());

        orderHistoryViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new OrderHistoryViewModel(DetailOrderHistoryActivity.this);
            }
        }).get(OrderHistoryViewModel.class);

        initData();

        position = getIntent().getIntExtra("position", 1000);

        event();
    }

    private void event() {
        orderHistoryViewModel.getListOrderHistory().observe(this, new Observer<AppResource<List<OrderHistory>>>() {
            @Override
            public void onChanged(AppResource<List<OrderHistory>> orderHistoryAppResource) {
                switch (orderHistoryAppResource.status) {
                    case ERROR:
                        detailOrderHistoryBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        detailOrderHistoryBinding.layoutTotalPriceOrderHistoryCart.setVisibility(View.GONE);
                        Toast.makeText(DetailOrderHistoryActivity.this, orderHistoryAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        detailOrderHistoryBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        detailOrderHistoryBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        detailOrderHistoryBinding.layoutTotalPriceOrderHistoryCart.setVisibility(View.VISIBLE);
                        Collections.reverse(orderHistoryAppResource.data);
                        detailOrderHistoryAdapter.updateListProduct(orderHistoryAppResource.data.get(position).getProducts());
                        detailOrderHistoryBinding.textViewTotalPriceCart.setText(String.format("%s VND", StringUtil.formatCurrency(orderHistoryAppResource.data.get(position).getPrice())));
                        break;
                }
            }
        });
    }

    private void initData() {
        detailOrderHistoryAdapter = new DetailOrderHistoryAdapter();
        detailOrderHistoryBinding.recyclerViewDetailOrderHistory.setAdapter(detailOrderHistoryAdapter);
        detailOrderHistoryBinding.recyclerViewDetailOrderHistory.setHasFixedSize(true);

        orderHistoryViewModel.fetchOrderHistory();
    }
}