package com.project.ict502.model;

public class Cart {
    private int itemId;
    private int orderId;
    private int quantity;

    public Cart(int itemId, int orderId, int quantity) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
