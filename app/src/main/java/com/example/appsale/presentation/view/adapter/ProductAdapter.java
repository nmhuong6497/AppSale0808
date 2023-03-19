package com.example.appsale.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsale.R;
import com.example.appsale.common.AppConstant;
import com.example.appsale.data.model.Product;
import com.example.appsale.databinding.LayoutItemProductBinding;
import com.example.appsale.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<Product> listProducts;
    Context context;
    private OnItemClickProduct onItemClickProduct;

    public void setListProducts(List<Product> listProducts) {
        this.listProducts = listProducts;
        notifyDataSetChanged();
    }

    public ProductAdapter() {
        listProducts = new ArrayList<>();
    }

    public void updateListProduct(List<Product> data) {
        if (listProducts != null && listProducts.size() > 0) {
            listProducts.clear();
        }
        listProducts.addAll(data);
        notifyDataSetChanged();
    }

    public List<Product> getListProducts() {
        return listProducts;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProductViewHolder(LayoutItemProductBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(context, listProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        LayoutItemProductBinding binding;
        Product product;

        public ProductViewHolder(@NonNull LayoutItemProductBinding view) {
            super(view.getRoot());
            binding = view;

            binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickProduct != null) {
                        onItemClickProduct.onClick(getAdapterPosition(), "add", product);
                    }
                }
            });

            binding.buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickProduct != null) {
                        onItemClickProduct.onClick(getAdapterPosition(), "detail", product);
                    }
                }
            });
        }

        public void bind(Context context, Product product) {
            this.product = product;
            binding.textViewName.setText(product.getName());
            binding.textViewAddress.setText(product.getAddress());
            binding.textViewPrice.setText(String.format("%s VND", StringUtil.formatCurrency(product.getPrice())));
            Glide.with(context)
                    .load(AppConstant.BASE_URL + product.getImg())
                    .placeholder(R.drawable.ic_logo)
                    .into(binding.imageViewProduct);
        }
    }

    public void setOnItemClickFood(OnItemClickProduct onItemClickProduct) {
        this.onItemClickProduct = onItemClickProduct;
    }

    public interface OnItemClickProduct {
        void onClick(int position, String nameButton, Product product);
    }
}