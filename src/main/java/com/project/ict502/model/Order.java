package com.project.ict502.model;

import java.util.Date;

public class Order {
    private int orderId;
    private String orderStatus; // uncompleted, ongoing, complete
    private Date orderDate;
    private double totalPrice;
    private int customerId;

    public Order() {
        this.orderId = -1;
        this.orderStatus = null;
        this.orderDate = null;
        this.totalPrice = -1;
        this.customerId = -1;
    }

    public Order( String orderStatus, Date orderDate, double totalPrice, int customerId) {
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
    }

    public Order(int orderId, String orderStatus, Date orderDate, double totalPrice, int customerId) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
    }

    public Order(Order order){
        this.orderId = order.orderId;
        this.orderStatus = order.orderStatus;
        this.orderDate = order.orderDate;
        this.totalPrice = order.totalPrice;
        this.customerId = order.customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
