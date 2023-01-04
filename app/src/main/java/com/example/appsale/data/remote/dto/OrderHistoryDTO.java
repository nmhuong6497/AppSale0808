package com.example.appsale.data.remote.dto;

import com.example.appsale.data.model.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistoryDTO {
    @SerializedName("_id")
    @Expose
    private String id;
    private List<ProductDTO> products;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    private Integer price;
    private Boolean status;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "OrderHistoryDTO{" +
                "id='" + id + '\'' +
                ", products=" + products +
                ", idUser='" + idUser + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", dateCreated='" + dateCreated + '\'' +
                ", v=" + v +
                '}';
    }
}
