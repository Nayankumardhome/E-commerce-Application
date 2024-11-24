package com.nayan.projects.eshoppingcart.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nayan.projects.eshoppingcart.model.Cart;
import com.nayan.projects.eshoppingcart.model.OrderAddress;
import com.nayan.projects.eshoppingcart.model.OrderRequest;
import com.nayan.projects.eshoppingcart.model.OrderSummary;
import com.nayan.projects.eshoppingcart.model.ProductOrder;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.repository.CartRepository;
import com.nayan.projects.eshoppingcart.repository.OrderAddressRepository;
import com.nayan.projects.eshoppingcart.repository.OrderSummaryRepository;
import com.nayan.projects.eshoppingcart.service.CODOrderService;
import com.nayan.projects.eshoppingcart.service.CardService;
import com.nayan.projects.eshoppingcart.service.UPIService;

@Service
public class CODOrderServiceImpl implements CODOrderService{
	
//	@Autowired
//	private CartRepository cartRepository;
//	
//	@Autowired
//	private OrderSummaryRepository summaryRepository;
//	
//	
//	private OrderAddressRepository addressRepository;
//	
//	@Autowired
//	private CardService cardService;
//	
//	@Autowired
//	private UPIService upiService;

	@Override
	public Boolean CODOrder(UserDtls user, OrderRequest orderRequest) throws Exception {
//		
//	    List<Cart> carts = cartRepository.findByUserId(user.getId());
//
//	    Double totalDiscount = 0.0;
//	    Double totalPrice = 0.0;
//
//	    List<ProductOrder> productOrders = new ArrayList<>();
//
//	    for (Cart cart : carts) {
//	        ProductOrder productOrder = new ProductOrder();
//	        productOrder.setProduct(cart.getProduct());
//	        productOrder.setQuantity(cart.getQuantity());
//	        
//	        Double price = cart.getProduct().getPrice() * cart.getQuantity();
//	        totalPrice += price;
//	        Double discount = cart.getProduct().getDiscount();
//	        totalDiscount += discount;
//	        productOrders.add(productOrder);
//	    }
//
//	    Double finalAmount = totalPrice - totalDiscount;
//
//	    if ("COD".equals(orderRequest.getPaymentType())) {
//	        OrderSummary orderSummary = new OrderSummary();
//	        orderSummary.setOrderId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
//	        orderSummary.setUser(user);
//	        orderSummary.setOrderDate(LocalDate.now());
//	        OrderAddress address = new OrderAddress();
//			address.setFirstName(orderRequest.getFirstName());
//			address.setLastName(orderRequest.getLastName());
//			address.setEmail(orderRequest.getEmail());
//			address.setMobileNumber(orderRequest.getMobileNumber());
//			address.setAddress(orderRequest.getAddress());
//			address.setCity(orderRequest.getCity());
//			address.setState(orderRequest.getState());
//			address.setPincode(orderRequest.getPincode());
//			orderSummary.setDeliveryAddress(address);
//	        orderSummary.setTotalPrice(totalPrice);
//	        orderSummary.setDiscount(totalDiscount);
//	        orderSummary.setFinalAmount(finalAmount);
//	        orderSummary.setProductOrders(productOrders);
//
//	        OrderAddress orderAddress = addressRepository.save(address);
//	        OrderSummary summary = summaryRepository.save(orderSummary);
//
//	        cartRepository.deleteByUserId(user.getId());
//	        return ((orderAddress != null) && (summary != null));
//	    }
		return false;
	}

	
}
