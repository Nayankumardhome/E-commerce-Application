package com.nayan.projects.eshoppingcart.util;

public enum OrderResponse {
	
	ORDER_PLACED(1, "Order Placed"),
    ORDER_PENDING(2, "Order Pending"),
    ORDER_ERROR(3, "Order Error");

    private final Integer id;
    private final String status;

    private OrderResponse(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
