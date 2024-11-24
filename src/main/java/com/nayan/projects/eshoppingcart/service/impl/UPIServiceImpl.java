package com.nayan.projects.eshoppingcart.service.impl;

import org.springframework.stereotype.Service;

import com.nayan.projects.eshoppingcart.service.UPIService;

@Service
public class UPIServiceImpl implements UPIService {

	@Override
	public String createUPIDeepLink(String upiId, String payeeName, String amount, String transactionNote) {
		return String.format("upi://pay?pa=%s&pn=%s&am=%s&cu=INR&tn=%s", upiId, payeeName, amount, transactionNote);
	}

}
