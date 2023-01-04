package com.example.appsale.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsale.data.model.OrderHistory;
import com.example.appsale.databinding.LayoutListOrderHistoryBinding;
import com.example.appsale.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    List<OrderHistory> orderHistoryList;
    Context context;
    private OrderHistoryAdapter.OnItemClickProduct onItemClickProduct;

    public OrderHistoryAdapter() {
        orderHistoryList = new ArrayList<>();
    }

    public void updateListOrderHistory(List<OrderHistory> data) {
        if (orderHistoryList != null && orderHistoryList.size() > 0) {
            orderHistoryList.clear();
        }
        orderHistoryList.addAll(data);
        notifyDataSetChanged();
    }

    public List<OrderHistory> getOrderHistoryList() {
        return orderHistoryList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new OrderHistoryAdapter.OrderHistoryViewHolder(LayoutListOrderHistoryBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderHistoryViewHolder holder, int position) {
        holder.bind(context, orderHistoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        LayoutListOrderHistoryBinding binding;

        public OrderHistoryViewHolder(@NonNull LayoutListOrderHistoryBinding view) {
            super(view.getRoot());
            binding = view;

            binding.buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickProduct != null) {
                        onItemClickProduct.onClick(getAdapterPosition());
                    }
                }
            });
        }

        public void bind(Context context, OrderHistory orderHistory) {
           binding.textViewDate.setText(orderHistory.getDateCreated());
           binding.textViewTotalOrderList.setText("Tổng tiền: " + String.format("%s VND", StringUtil.formatCurrency(orderHistory.getPrice())));
        }
    }

    public void setOnItemClickDetail(OrderHistoryAdapter.OnItemClickProduct onItemClickProduct) {
        this.onItemClickProduct = onItemClickProduct;
    }

    public interface OnItemClickProduct {
        void onClick(int position);
    }
}
