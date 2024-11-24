package com.nayan.projects.eshoppingcart.service;

import com.nayan.projects.eshoppingcart.model.OrderRequest;
import com.nayan.projects.eshoppingcart.model.SaveCard;
import com.nayan.projects.eshoppingcart.model.UserDtls;

public interface CardService {

	public String getCardType(String cardNumber);
	
	public SaveCard saveCard(UserDtls user, OrderRequest orderRequest);
}
