package br.com.cten.dm114projetofinal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderItem implements Serializable{

    /*
    {
        "id": 5649050225344512,
        "productId": 5707702298738688,
        "orderId": 5668600916475904
    }
    **/

    @SerializedName("id")
    private long id;

    @SerializedName("productId")
    private String productId;

    @SerializedName("orderId")
    private double orderId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getOrderId() {
        return orderId;
    }

    public void setOrderId(double orderId) {
        this.orderId = orderId;
    }
}
