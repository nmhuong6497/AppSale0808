package com.example.appsale.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
    OnItemClickCart onItemClickCart;

    public CartAdapter() {
        listProductsOfCart = new ArrayList<>();
    }

    public void updateCart(Cart data) {
        if (listProductsOfCart != null && listProductsOfCart.size() > 0) {
            listProductsOfCart.clear();
        }
        cart = data;
        listProductsOfCart.addAll(data.getProducts());
        notifyDataSetChanged();
    }

    public List<Product> getListProductOfCart() {
        return listProductsOfCart;
    }

    public Cart getCart() {
        return cart;
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

            binding.cardViewItemCart.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemClickCart != null) {
                        onItemClickCart.onLongClick(getAdapterPosition());
                    }
                    return false;
                }
            });
            binding.imageViewUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickCart != null) {
                        onItemClickCart.onClick(getAdapterPosition(), true);
                    }
                }
            });

            binding.imageViewDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickCart != null) {
                        onItemClickCart.onClick(getAdapterPosition(), false);
                    }
                }
            });
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

    public void setOnItemClickCart(CartAdapter.OnItemClickCart onItemClickCart) {
        this.onItemClickCart = onItemClickCart;
    }

    public interface OnItemClickCart {
        void onClick(int position, boolean isQuantityUp);
        void onLongClick(int position);
    }
}
