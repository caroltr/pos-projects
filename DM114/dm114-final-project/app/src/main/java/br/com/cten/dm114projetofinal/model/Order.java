package br.com.cten.dm114projetofinal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    /*
    {
        "id": 5668600916475904,
        "email": "doralice@siecola.com.br",
        "freightPrice": 40,
        "orderItems": [
            {
                "id": 5649050225344512,
                "productId": 5707702298738688,
                "orderId": 5668600916475904
            },
            {
                "id": 5724160613416960,
                "productId": 5668600916475904,
                "orderId": 5668600916475904
            }
        ]
    }

    **/

    @SerializedName("id")
    private long id;

    @SerializedName("email")
    private String email;

    @SerializedName("freightPrice")
    private double freightPrice;

    @SerializedName("orderItems")
    private List<OrderItem> orderItems;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(double freightPrice) {
        this.freightPrice = freightPrice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
