package com.nayan.projects.eshoppingcart.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNumber;

	private String address;

	private String city;

	private String state;

	private Integer pincode;
	
	private String paymentType;
    
    // Card details
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private Boolean saveCardFlag;
}
