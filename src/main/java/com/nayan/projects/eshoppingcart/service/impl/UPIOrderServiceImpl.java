package com.nayan.projects.eshoppingcart.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nayan.projects.eshoppingcart.service.UPIOrderService;
import com.nayan.projects.eshoppingcart.service.UPIService;


@Service
public class UPIOrderServiceImpl implements UPIOrderService {
	
	private static final String UPIID = "**********@******";
	private static final String NAME = "**** ***";

	@Autowired
	public UPIService upiService;
	
	public String UPIOrder(String orderId, Double totalAmmount) {
		
		String url = upiService.createUPIDeepLink(UPIID, NAME, totalAmmount.toString(), orderId);
		
		return url;
	}
}
