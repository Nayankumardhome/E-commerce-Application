package com.nayan.projects.eshoppingcart.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.model.ProductOrder;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommanUtil {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender mailSender;

	public Boolean sendMail(String reciepentEmail, String url) throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);

		messageHelper.setFrom("nayandhome@gmail.com", "Shopping Cart");
		messageHelper.setTo(reciepentEmail);

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
				+ "\">Change my password</a></p>";

		messageHelper.setSubject("Password Reset");
		messageHelper.setText(content,true);
		mailSender.send(message);
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {
		String siteUrl = request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(), "");
	}

	public void notifyMe(Integer userId, Product product) throws MessagingException, UnsupportedEncodingException {
		UserDtls user = userRepository.findById(userId).orElse(null);
		if(!ObjectUtils.isEmpty(user)) {
			String reciepentEmail = user.getEmail();

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message);

			messageHelper.setFrom("nayandhome@gmail.com", "Shopping Cart");
			messageHelper.setTo(reciepentEmail);

			String productUrl = "http://localhost:8080/product/" + product.getId();

			String content = "<p>Hello,</p>" 
					+ "<p>The product you were interested in is now back in stock!</p>"
					+ "<p>Product Name: <strong>" + product.getProductName() + "</strong></p>" 
					+ "<p>Click the link below to view the product:</p>" 
					+ "<p><a href=\"" + productUrl + "\">View Product</a></p>"
					+ "<p>Thank you for shopping with us!</p>";
			messageHelper.setSubject("Product Available");
			messageHelper.setText(content,true);
			mailSender.send(message);
		}
	}

	public Boolean sendMailForProductOrder(ProductOrder order,String status){
		
		MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper messageHelper = new MimeMessageHelper(message);

	    
	    try {
	    	messageHelper.setFrom("nayandhome@gmail.com", "Shopping Cart");
			messageHelper.setTo(order.getOrderSummary().getDeliveryAddress().getEmail());
		} catch (MessagingException | UnsupportedEncodingException e ) {
			System.err.println("***************>>messageHelper:1/Later I will handel this exceptions by Global Custum Exceptions<<*****************");
			System.out.println(e.getMessage());
		}

	    String content = "<html><body>"
	            + "<p>Dear [[name]],</p>"
	            + "<p>Thank you for your order. Your order status is now: <b>[[orderStatus]]</b>.</p>"
	            + "<h4>Order Details</h4>"
	            + "<p><b>Product Name:</b> [[productName]]</p>"
	            + "<p><b>Category:</b> [[category]]</p>"
	            + "<p><b>Quantity:</b> [[quantity]]</p>"
	            + "<p><b>Price:</b> ₹[[price]]</p>"
	            + "<p><b>Payment Type:</b> [[paymentType]]</p>"
	            + "<p>You can view the details of your order or track its status by visiting <a href='http://localhost:8080/user/user-orders'>My Orders</a>.</p>"
	            + "<p>We hope to see you again soon! Thank you for shopping with us.</p>"
	            + "<p>Best regards,<br>Your Shopping Cart Team</p>"
	            + "</body></html>";
	    
	    Double finalPrice = (order.getPrice() - order.getDiscount());

	    content = content.replace("[[name]]", order.getOrderSummary().getDeliveryAddress().getFirstName());
	    content = content.replace("[[orderStatus]]", status);
	    content = content.replace("[[productName]]", order.getProduct().getProductName());
	    content = content.replace("[[category]]", order.getProduct().getCategory());
	    content = content.replace("[[quantity]]", order.getQuantity().toString());
	    content = content.replace("[[price]]", finalPrice.toString());
	    content = content.replace("[[paymentType]]", order.getOrderSummary().getPaymentType());

	    try {
			messageHelper.setSubject("Your Order Status has been Updated");
			messageHelper.setText(content, true);
		} catch (MessagingException e) {
			System.err.println("***************>>messageHelper:2<<*****************");
			System.err.println(e.getMessage());
		}
	    mailSender.send(message);
	    
	    return true;
	}
}
