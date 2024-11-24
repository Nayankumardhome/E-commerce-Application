package com.nayan.projects.eshoppingcart.util;

public enum OrderStatus {

	IN_PROGRESS(1, "In Progress", "On the way"),
    ORDER_RECEIVED(2, "Order Received", "On the way"),
    PRODUCT_PACKED(3, "Product Packed", "On the way"),
    OUT_FOR_DELIVERY(4, "Out For Delivery", "On the way"),
    DELIVERED(5, "Delivered", "Delivered"),
    CANCELED(6, "Canceled", "Canceled"),
    RETURNED_IN_PROGRESS(7, "Returned In Progress", "On the way"),
    RETURNED_OUT_FOR_RECEIVING(8, "Returned Out For Receiving", "On the way"),
    RETURNED_RECEIVED(9, "Returned Received", "On the way"),
    RETURNED(10, "Returned", "Returned"),
    REPLACED_IN_PROGRESS(11, "Replaced In Progress", "On the way"),
    REPLACED_OUT_FOR_RECEIVING(12, "Replaced Out For Receiving", "On the way"),
    REPLACED_RECEIVED(13, "Replaced Received", "On the way"),
    REPLACED_ORDER_RECEIVED(14, "Replaced Order Received", "On the way"),
    REPLACED_PRODUCT_PACKED(15, "Replaced Product Packed", "On the way"),
    REPLACED_OUT_FOR_DELIVERY(16, "Replaced Out For Delivery", "On the way"),
    REPLACED(17, "Replaced", "Replaced");

    private Integer id;
    private String status;
    private String finalStatus;

    private OrderStatus(Integer id, String status, String finalStatus) {
        this.id = id;
        this.status = status;
        this.finalStatus = finalStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }
}
