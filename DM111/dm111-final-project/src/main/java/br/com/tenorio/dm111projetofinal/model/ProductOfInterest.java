package br.com.tenorio.dm111projetofinal.model;

import java.io.Serializable;

public class ProductOfInterest implements Serializable {

    private String userCpf;
    private long userSalesId;
    private long productId;
    private double price;

    public String getUserCpf() {
        return userCpf;
    }

    public void setUserCpf(String userCpf) {
        this.userCpf = userCpf;
    }

    public long getUserSalesId() {
        return userSalesId;
    }

    public void setUserSalesId(long userSalesId) {
        this.userSalesId = userSalesId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
