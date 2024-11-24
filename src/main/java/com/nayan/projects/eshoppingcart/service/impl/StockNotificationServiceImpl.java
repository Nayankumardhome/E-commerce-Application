package com.nayan.projects.eshoppingcart.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.model.ProductNotificationRequest;
import com.nayan.projects.eshoppingcart.repository.ProductNotificationRequestRepository;
import com.nayan.projects.eshoppingcart.repository.ProductRepository;
import com.nayan.projects.eshoppingcart.service.StockNotificationService;
import com.nayan.projects.eshoppingcart.util.CommanUtil;

import jakarta.mail.MessagingException;

@Service
public class StockNotificationServiceImpl implements StockNotificationService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductNotificationRequestRepository notificationRequestRepository;
	
	@Autowired
	private CommanUtil commanUtil;

	@Scheduled(cron = "0 0 * * * *")  // Runs every hour
	@Override
	public void checkStockAndNotify() {
		List<ProductNotificationRequest> notificationRequests = notificationRequestRepository.findAll();
		
		for(ProductNotificationRequest notification : notificationRequests) {

			Product product = productRepository.findById(notification.getProductId()).orElse(null);
			if((!ObjectUtils.isEmpty(product))&&(product.getStock() > 0)){
				try {
					commanUtil.notifyMe(notification.getUserId(),product);
				} catch (UnsupportedEncodingException | MessagingException e) {
					System.out.println("Something wrong on server.");
				}
				notificationRequestRepository.delete(notification);
			}
		}
		
	}

}
