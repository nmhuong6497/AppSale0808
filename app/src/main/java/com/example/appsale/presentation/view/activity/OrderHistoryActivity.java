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

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.OrderHistory;
import com.example.appsale.databinding.ActivityOrderHistoryBinding;
import com.example.appsale.presentation.view.adapter.OrderHistoryAdapter;
import com.example.appsale.presentation.viewmodel.OrderHistoryViewModel;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    ActivityOrderHistoryBinding orderHistoryBinding;
    OrderHistoryAdapter orderHistoryAdapter;
    OrderHistoryViewModel orderHistoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderHistoryBinding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(orderHistoryBinding.getRoot());

        orderHistoryViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new OrderHistoryViewModel(OrderHistoryActivity.this);
            }
        }).get(OrderHistoryViewModel.class);

        initData();
        event();
    }

    private void event() {
        orderHistoryViewModel.getListOrderHistory().observe(this, new Observer<AppResource<List<OrderHistory>>>() {
            @Override
            public void onChanged(AppResource<List<OrderHistory>> orderHistoryAppResource) {
                switch (orderHistoryAppResource.status) {
                    case ERROR:
                        orderHistoryBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        Toast.makeText(OrderHistoryActivity.this, orderHistoryAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        orderHistoryBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        orderHistoryBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        orderHistoryAdapter.updateListOrderHistory(orderHistoryAppResource.data);
                        break;
                }
            }
        });
        
        orderHistoryAdapter.setOnItemClickDetail(new OrderHistoryAdapter.OnItemClickProduct() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(OrderHistoryActivity.this, DetailOrderHistoryActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        orderHistoryAdapter = new OrderHistoryAdapter();
        orderHistoryBinding.recyclerViewListOderHistory.setAdapter(orderHistoryAdapter);
        orderHistoryBinding.recyclerViewListOderHistory.setHasFixedSize(true);

        orderHistoryViewModel.fetchOrderHistory();
    }
}