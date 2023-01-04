package com.example.appsale.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsale.R;
import com.example.appsale.common.AppConstant;
import com.example.appsale.data.model.Product;
import com.example.appsale.data.remote.dto.ProductDTO;
import com.example.appsale.databinding.LayoutDetailOrderHistoryBinding;
import com.example.appsale.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DetailOrderHistoryAdapter extends RecyclerView.Adapter<DetailOrderHistoryAdapter.DetailOrderHistoryViewHolder> {
    List<ProductDTO> listProducts;
    Context context;
    private DetailOrderHistoryAdapter.OnItemClickProduct onItemClickProduct;

    public DetailOrderHistoryAdapter() {
        listProducts = new ArrayList<>();
    }

    public void updateListProduct(List<ProductDTO> data) {
        if (listProducts != null && listProducts.size() > 0) {
            listProducts.clear();
        }
        listProducts.addAll(data);
        notifyDataSetChanged();
    }

    public List<ProductDTO> getListProducts() {
        return listProducts;
    }

    @NonNull
    @Override
    public DetailOrderHistoryAdapter.DetailOrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DetailOrderHistoryAdapter.DetailOrderHistoryViewHolder(LayoutDetailOrderHistoryBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailOrderHistoryAdapter.DetailOrderHistoryViewHolder holder, int position) {
        holder.bind(context, listProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    class DetailOrderHistoryViewHolder extends RecyclerView.ViewHolder {

        LayoutDetailOrderHistoryBinding binding;

        public DetailOrderHistoryViewHolder(@NonNull LayoutDetailOrderHistoryBinding view) {
            super(view.getRoot());
            binding = view;
        }

        public void bind(Context context, ProductDTO product) {
            binding.textViewNameProduct.setText(product.getName());
            binding.textViewPriceProduct.setText(String.format("%s VND", StringUtil.formatCurrency(product.getPrice())));
            binding.textViewQuantity.setText("x" + product.getQuantity());
            binding.textViewTotalPrice.setText(String.format("%s VND", StringUtil.formatCurrency(product.getPrice() * product.getQuantity())));
            Glide.with(context)
                    .load(AppConstant.BASE_URL + product.getImg())
                    .placeholder(R.drawable.ic_logo)
                    .into(binding.imageView);
        }
    }

    public void setOnItemClickFood(DetailOrderHistoryAdapter.OnItemClickProduct onItemClickProduct) {
        this.onItemClickProduct = onItemClickProduct;
    }

    public interface OnItemClickProduct {
        void onClick(int position);
    }
}
