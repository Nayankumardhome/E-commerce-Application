package com.nayan.projects.eshoppingcart.model;

public class OrderResponseDto {

	private Integer id;
    private String status;
    private String paymentUrl;
    
    public OrderResponseDto(Integer id, String status) {
		super();
		this.id = id;
		this.status = status;
	}

	public OrderResponseDto(Integer id, String status, String paymentUrl) {
        this.id = id;
        this.status = status;
        this.paymentUrl = paymentUrl;
    }

    // Getters and setters
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

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
