package com.nayan.projects.eshoppingcart.service;

public interface UPIService {

	public String createUPIDeepLink(String upiId, String payeeName, String amount, String transactionNote);
}
