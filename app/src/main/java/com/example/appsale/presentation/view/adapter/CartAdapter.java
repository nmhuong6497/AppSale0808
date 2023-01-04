package com.example.appsale.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsale.R;
import com.example.appsale.common.AppConstant;
import com.example.appsale.data.model.Cart;
import com.example.appsale.data.model.Product;
import com.example.appsale.databinding.LayoutItemCartBinding;
import com.example.appsale.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    Cart cart;
    List<Product> listProductsOfCart;
    Context context;

    public CartAdapter() {
        listProductsOfCart = new ArrayList<>();
    }

    public void updateCart(List<Product> data) {
        if (listProductsOfCart != null && listProductsOfCart.size() > 0) {
            listProductsOfCart.clear();
        }
        listProductsOfCart.addAll(data);
        notifyDataSetChanged();
    }

    public List<Product> getListCarts() {
        return listProductsOfCart;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CartAdapter.CartViewHolder(LayoutItemCartBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.bind(context, listProductsOfCart.get(position));
    }

    @Override
    public int getItemCount() {
        return listProductsOfCart.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        LayoutItemCartBinding binding;

        public CartViewHolder(@NonNull LayoutItemCartBinding view) {
            super(view.getRoot());
            binding = view;
        }

        public void bind(Context context, Product product) {
            binding.textViewNameCart.setText(product.getName());
            binding.textViewPriceCart.setText(String.format("%s VND", StringUtil.formatCurrency(product.getPrice())));
            binding.quantityCart.setText(String.valueOf(product.getQuantity()));
            binding.textViewTotalPriceProductCart.setText(String.format("%s VND", StringUtil.formatCurrency(product.getQuantity() * product.getPrice())));
            Glide.with(context)
                    .load(AppConstant.BASE_URL + product.getImg())
                    .placeholder(R.drawable.ic_logo)
                    .into(binding.imageViewCart);
        }
    }
}
