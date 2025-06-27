package com.example.finalwork.javabean;

public class Order {
    private String uuid;
    private double total_price;
    private long timestamp;

    public Order(String uuid, double total_price, long timestamp) {
        this.uuid = uuid;
        this.total_price = total_price;
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public double getTotal_price() {
        return total_price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
