package com.nayan.projects.eshoppingcart.service.impl;

import org.springframework.stereotype.Service;

import com.nayan.projects.eshoppingcart.model.OrderRequest;
import com.nayan.projects.eshoppingcart.model.SaveCard;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.service.CardService;

@Service
public class CardServiceImpl implements CardService{

	@Override
	public String getCardType(String cardNumber) {
		if (cardNumber == null || cardNumber.isEmpty()) {
            return "Invalid Card Number";
        }

        // Remove all spaces or dashes to normalize the input
        cardNumber = cardNumber.replaceAll("\\s+", "").replaceAll("-", "");

        // Visa: starts with 4, length 13 or 16
        if (cardNumber.startsWith("4")) {
            return "Visa";
        }

        // MasterCard: starts with 51-55, length 16
        if (cardNumber.matches("^5[1-5].*")) {
            return "MasterCard";
        }

        // American Express: starts with 34 or 37, length 15
        if (cardNumber.matches("^3[47].*")) {
            return "American Express";
        }

        // Discover: starts with 6011, 622126-622925, 644-649, or 65, length 16
        if (cardNumber.matches("^6011.*") || cardNumber.matches("^65.*") ||
            cardNumber.matches("^64[4-9].*") || cardNumber.matches("^6221[2-9].*") ||
            cardNumber.matches("^622[2-8].*") || cardNumber.matches("^6229[01].*") || 
            cardNumber.matches("^62292[0-5].*")) {
            return "Discover";
        }

        // Diners Club: starts with 300-305, 36, or 38, length 14
        if (cardNumber.matches("^3(?:0[0-5]|[68]).*")) {
            return "Diners Club";
        }

        // JCB: starts with 3528-3589, length 16
        if (cardNumber.matches("^35[28-89].*")) {
            return "JCB";
        }

        // RuPay: typically starts with 60, 65, 81, or 82, length 16 or 19
        if (cardNumber.matches("^(60|65|81|82).*")) {
            return "RuPay";
        }

        return "Unknown Card Type"; // Default case if no patterns match
	}

	@Override
	public SaveCard saveCard(UserDtls user, OrderRequest orderRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
