package com.project.ict502.model;

import java.util.Date;

public class Payment {
    private int paymentId;
    private Date paymentDate;
    private String paymentStatus;
    private int orderId;

    public Payment(Date paymentDate, String paymentStatus, int orderId) {
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
    }

    public Payment(int paymentId, Date paymentDate, String paymentStatus, int orderId) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
