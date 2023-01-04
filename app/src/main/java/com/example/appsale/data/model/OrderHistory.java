package com.example.appsale.data.model;

import com.example.appsale.data.remote.dto.ProductDTO;

import java.util.List;

public class OrderHistory {
    private String id;
    private List<ProductDTO> products;
    private String idUser;
    private Integer price;
    private Boolean status;
    private String dateCreated;
    private Integer v;

    public OrderHistory(String id, List<ProductDTO> products, String idUser, Integer price, Boolean status, String dateCreated, Integer v) {
        this.id = id;
        this.products = products;
        this.idUser = idUser;
        this.price = price;
        this.status = status;
        this.dateCreated = dateCreated;
        this.v = v;
    }

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
        return "OrderHistory{" +
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
