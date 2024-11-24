package com.nayan.projects.eshoppingcart.service;

import java.util.List;

import com.nayan.projects.eshoppingcart.model.Cart;

public interface CartService {

	public Cart saveCart(Integer userId, Integer productId);
	
	public List<Cart> getCartsByUser(Integer userId);
	
	public Integer getCartIntemsCount(Integer userId);

	public Boolean updateQuantity(String symbol, Integer cid);

	public void removeProductFromCart(Integer userId, Integer productId);
}
