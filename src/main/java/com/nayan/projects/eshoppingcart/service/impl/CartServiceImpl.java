package com.nayan.projects.eshoppingcart.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nayan.projects.eshoppingcart.model.Cart;
import com.nayan.projects.eshoppingcart.model.OrderSummary;
import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.repository.CartRepository;
import com.nayan.projects.eshoppingcart.repository.ProductRepository;
import com.nayan.projects.eshoppingcart.repository.UserRepository;
import com.nayan.projects.eshoppingcart.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Cart saveCart(Integer userId, Integer productId) {
		System.out.println("From CartServiceImpl: " + "UserId: " + userId + ", ProductId: " + productId);
	    
	    Optional<UserDtls> userOpt = userRepository.findById(userId);
	    Optional<Product> productOpt = productRepository.findById(productId);
	    
	    if (!userOpt.isPresent()) {
	        System.out.println("From CartServiceImpl: User Not Found with ID: " + userId);
	        throw new RuntimeException("User not found with ID: " + userId);
	    }
	    
	    if (!productOpt.isPresent()) {
	        System.out.println("Product not found with ID: " + productId);
	        throw new RuntimeException("Product not found with ID: " + productId);
	    }
	    
	    UserDtls user = userOpt.get();
	    Product product = productOpt.get();
		
		Cart oldCart = cartRepository.findByProductIdAndUserId(productId, userId);
		
		Cart cart = null;
		
		if(ObjectUtils.isEmpty(oldCart)) {
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setTotalPrice(product.getDiscountPrice());
			return cartRepository.save(cart);
		} else {
			oldCart.setQuantity(oldCart.getQuantity()+1);
			oldCart.setTotalPrice(oldCart.getQuantity() * oldCart.getProduct().getDiscountPrice());
			return cartRepository.save(oldCart);
		}
	}

	@Override
	public List<Cart> getCartsByUser(Integer userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		List<Cart> updatedCarts = new ArrayList<Cart>();
		Double totalPrice = 0.0;
		Double totalOrderPrice = 0.0;
		OrderSummary summary = new OrderSummary();
		for(Cart cart : carts) {
			totalPrice = (cart.getProduct().getDiscountPrice() * cart.getQuantity());
			cart.setTotalPrice(Double.parseDouble(String.format("%.2f", totalPrice)));
			totalOrderPrice += totalPrice;
			cart.setTotalOrderPrice(totalOrderPrice);
			updatedCarts.add(cart);
		}
		
		List<Object> cartOrder = new ArrayList<Object>();
		cartOrder.add(carts);
		
		return carts;
	}

	@Override
	public Integer getCartIntemsCount(Integer userId) {
		return cartRepository.countByUserId(userId);
	}

	@Override
	public Boolean updateQuantity(String symbol, Integer cid) {
		
		Cart cart = cartRepository.findById(cid).orElse(null);

	    if (cart == null) {
	        return false;
	    }

	    int updateQuantity = cart.getQuantity();

	    if (symbol.equalsIgnoreCase("minus")) {
	        updateQuantity -= 1;
	    } else if (symbol.equalsIgnoreCase("plus")) {
	        updateQuantity += 1;
	    }

	    if (updateQuantity <= 0) {
	        cartRepository.deleteById(cid);
	        return true;
	    }

	    cart.setQuantity(updateQuantity);
	    Cart updatedCart = cartRepository.save(cart);

	    return updatedCart != null;
	}

	@Override
	public void removeProductFromCart(Integer userId, Integer productId) {
	    List<Cart> carts = cartRepository.findByUserId(userId);
	    
	    if (carts != null) {
	        Iterator<Cart> iterator = carts.iterator();
	        while (iterator.hasNext()) {
	            Cart cart = iterator.next();
	            if (cart.getProduct().getId().equals(productId)) {
	                iterator.remove();
	            }
	        }
	        cartRepository.saveAll(carts);
	    }
	}

	

}
