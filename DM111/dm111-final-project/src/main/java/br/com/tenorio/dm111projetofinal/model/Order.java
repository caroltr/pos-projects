package br.com.tenorio.dm111projetofinal.model;

import java.io.Serializable;

public class Order implements Serializable {

    private long id;
    private long userSalesId;
    private String userCpf;
    private String reason;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserSalesId() {
        return userSalesId;
    }

    public void setUserSalesId(long userSalesId) {
        this.userSalesId = userSalesId;
    }

    public String getUserCpf() {
        return userCpf;
    }

    public void setUserCpf(String userCpf) {
        this.userCpf = userCpf;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
