package com.nayan.projects.eshoppingcart.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nayan.projects.eshoppingcart.model.Cart;
import com.nayan.projects.eshoppingcart.model.OrderRequest;
import com.nayan.projects.eshoppingcart.model.OrderResponseDto;
import com.nayan.projects.eshoppingcart.model.OrderSummary;
import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.model.ProductOrder;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.service.CardService;
import com.nayan.projects.eshoppingcart.service.CartService;
import com.nayan.projects.eshoppingcart.service.CategoryService;
import com.nayan.projects.eshoppingcart.service.OrderService;
import com.nayan.projects.eshoppingcart.service.ProductService;
import com.nayan.projects.eshoppingcart.service.UserService;
import com.nayan.projects.eshoppingcart.util.CommanUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CommanUtil commanUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String home() {
		return "user/home";
	}
	
	@ModelAttribute
	public void getUserDetails(Principal principal, Model model) {
		if(!ObjectUtils.isEmpty(principal)) {
			UserDtls user = userService.getUserByEmail(principal.getName());
			model.addAttribute("user", user);
			model.addAttribute("cardItemsCount", cartService.getCartIntemsCount(user.getId()));
		}
		model.addAttribute("categories", categoryService.getAllActiveCategory());
	}
	
	
	/*
	 * Cart section
	 */
	
	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
		
		 Cart cart = cartService.saveCart(uid, pid);
		 System.out.println("From UserController: " + cart);
		 if(ObjectUtils.isEmpty(cart)) {
			 session.setAttribute("errorMsg", "Product Add to cart Fail. Some thing Wrong on Server.");
		 } else {
			 session.setAttribute("successMsg", "Add successfully into the cart.");
		 }
		
		return "redirect:/product/"+ pid;
	}
	
	@GetMapping("/cart")
	public String cartItems(Principal principal, Model model) {

		UserDtls user = userService.getLoggedInUserDetails(principal);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		model.addAttribute("carts", carts);
		
		if(carts.isEmpty()) {
			return "user/cart";
		}
		
		Double totalPrice = 0.0;
		Double totalDiscount = 0.0;

		for (Cart cart : carts) {
		    Double price = cart.getProduct().getPrice() * cart.getQuantity();
		    totalPrice += price;
		    
		    Double discountPercentage = cart.getProduct().getDiscount();
		    Double discountAmount = price * (discountPercentage / 100);
		    totalDiscount += discountAmount;
		}

		Double finalAmount = totalPrice - totalDiscount;

		OrderSummary orderSummary = new OrderSummary();
		orderSummary.setPlatformFee(3.0);

		if (finalAmount >= 500) {
		    orderSummary.setDeliveryCharges(0.0);
		} else {
		    orderSummary.setDeliveryCharges(40.0);
		}

		orderSummary.setTotalPrice(totalPrice);
		orderSummary.setDiscount(totalDiscount);
		orderSummary.setFinalAmount(finalAmount + orderSummary.getPlatformFee() + orderSummary.getDeliveryCharges());
	    model.addAttribute("OrderSummary", orderSummary);

		return "user/cart";
	}
	
	@GetMapping("/cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String symbol, @RequestParam Integer cid) {
		cartService.updateQuantity(symbol,cid);
		return "redirect:/user/cart";
	}
	
	
	@GetMapping("/notifyMe")
	public String notifyMe(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
		
		Boolean isNotify = userService.registerNotification(pid,uid);
		if(isNotify) {
			session.setAttribute("successMsg", "You will be notified when the product is back in stock.");
		} else {
			session.setAttribute("errorMsg", "Unable to register for notifications. Please try again.");
		}
		
		return "redirect:/product/"+ pid;
	}
	
	/*
	 * Order Section
	 */
	
	@GetMapping("/orders")
	public String orderPage(Principal principal, Model model) {
		
		UserDtls user = userService.getLoggedInUserDetails(principal);
		List<Cart> carts = cartService.getCartsByUser(user.getId());

		Double totalPrice = 0.0;
		Double totalDiscount = 0.0;

		for (Cart cart : carts) {
		    
		    Double price = cart.getProduct().getPrice() * cart.getQuantity();
		    totalPrice += price;
		    
		    Double discountPercentage = cart.getProduct().getDiscount();
		    Double discountAmount = price * (discountPercentage / 100);
		    totalDiscount += discountAmount;
		}

		Double finalAmount = totalPrice - totalDiscount;

		OrderSummary orderSummary = new OrderSummary();
		orderSummary.setPlatformFee(3.0);

		if (finalAmount >= 500) {
		    orderSummary.setDeliveryCharges(0.0);
		} else {
		    orderSummary.setDeliveryCharges(40.0);
		}

		orderSummary.setTotalPrice(totalPrice);
		orderSummary.setDiscount(totalDiscount);
		orderSummary.setFinalAmount(finalAmount + orderSummary.getPlatformFee() + orderSummary.getDeliveryCharges());

		// Add the order summary to the model
		model.addAttribute("OrderSummary", orderSummary);
		return "user/order";
	}
	
	@PostMapping("/checkout")
	public String saveOrder(@ModelAttribute OrderRequest orderRequest, HttpSession session, Principal principal) {
		
		UserDtls user = userService.getLoggedInUserDetails(principal);
		
		OrderResponseDto response = orderService.orderPlace(user, orderRequest);
		if(response.getId()==1) {
			return "redirect:/user/success";
		}

		return "/user/home";
	}
	
	@GetMapping("/success")
	public String loadSuccessPage() {
		return "/user/success";
	}
	
	@GetMapping("/user-orders")
	public String allOrders(Principal principal, Model model,
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "6") Integer pageSize) {
		
		UserDtls user = userService.getLoggedInUserDetails(principal);
//		List<ProductOrder> productOrders = orderService.getAllOrders(user.getId());
		Page<ProductOrder> page = orderService.getAllOrdersPagination(user.getId(),pageNo, pageSize);
		List<ProductOrder> productOrders = page.getContent();
		
		List<String> timeFilters = orderService.getTimeFilters();
		List<String> orderStatuses = orderService.getOrderStatuses();
		
		if((!ObjectUtils.isEmpty(productOrders)) && (!ObjectUtils.isEmpty(timeFilters))) {
			model.addAttribute("orderStatuses", orderStatuses);
			model.addAttribute("timeFilters", timeFilters);
			model.addAttribute("orders", productOrders);
			model.addAttribute("ordersSize", productOrders.size());
			model.addAttribute("pageNo", page.getNumber());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("totalElements", page.getTotalElements());
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("isFirst", page.isFirst());
			model.addAttribute("isLast", page.isLast());
			
			model.addAttribute("selectedStatus", null);
		    model.addAttribute("selectedTime", null);
		    model.addAttribute("searchQuery",null);
		}
		
		return "/user/my_orders";
	}
	
	@GetMapping("/filterMyOrders")
	public String filterOrders(
			@RequestParam(value = "status", required = false) List<String> selectedStatus,
            @RequestParam(value = "time", required = false) List<String> selectedTime,
            Model model, Principal principal,
            @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "3") Integer pageSize
            ) {
		
	    if (selectedStatus == null) {
	        selectedStatus = new ArrayList<>();
	    }
	    if (selectedTime == null) {
	        selectedTime = new ArrayList<>();
	    }
		
	    UserDtls user = userService.getLoggedInUserDetails(principal);

//	    List<ProductOrder> orders = orderService.filterOrdersByYearAndStatus(user,selectedStatus, selectedTime);
	    Page<ProductOrder> page = orderService.filterOrdersByYearAndStatusPagination(user, selectedStatus, selectedTime, pageNo, pageSize);
	    List<ProductOrder> orders = page.getContent();

	    List<String> timeFilters = orderService.getTimeFilters();
	    List<String> orderStatuses = orderService.getOrderStatuses();

	    model.addAttribute("orders", orders);
	    model.addAttribute("ordersSize", orders.size());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());
		
	    model.addAttribute("orderStatuses", orderStatuses);
	    model.addAttribute("timeFilters", timeFilters);
	    model.addAttribute("selectedStatus", selectedStatus);
	    model.addAttribute("selectedTime", selectedTime);
	    
	    model.addAttribute("searchQuery",null);

	    return "/user/my_orders";
	}
	
	@GetMapping("/searchMyOrders")
    public String searchOrders(@RequestParam String searchQuery,
    		Model model, Principal principal,
    		@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "3") Integer pageSize) {
		
		UserDtls user = userService.getLoggedInUserDetails(principal);
		
//		List<ProductOrder> orders = orderService.searchOrders(user, searchQuery);
		Page<ProductOrder> page = orderService.searchOrdersPagination(user, searchQuery, pageNo, pageSize);
		List<ProductOrder> orders = page.getContent();
		
		List<String> timeFilters = orderService.getTimeFilters();
	    List<String> orderStatuses = orderService.getOrderStatuses();
	    
//	    Collections.reverse(orders);
	    
	    
	    model.addAttribute("orderStatuses", orderStatuses);
	    model.addAttribute("timeFilters", timeFilters);
	    
		model.addAttribute("orders", orders);
		model.addAttribute("ordersSize", orders.size());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());
		
		model.addAttribute("searchQuery",searchQuery);
		
		model.addAttribute("selectedStatus", null);
	    model.addAttribute("selectedTime", null);
	    
		return "/user/my_orders";
	}
	
	@GetMapping("/updateOrderStatus")
	public String updateOrderStatus(@RequestParam Long id, @RequestParam String status, Model model) {
		model.addAttribute("orderId", id);
		switch(status) {
			case "replace":
				model.addAttribute("actionType", "replace");
				break;
			case "cancel":
				model.addAttribute("actionType", "cancel");
				break;
			case "return":
				model.addAttribute("actionType", "return");
				break;
			default:
				return "redirect:/user/user-orders";
		}
		return "/user/order_update_status_reason";
	}
	
	@PostMapping("/submitOrderStatusUpdate")
	public String submitOrderStatusUpdate(@RequestParam Integer orderId, 
	                                      @RequestParam String actionType, 
	                                      @RequestParam String reason, 
	                                      @RequestParam(required = false) String detailedReason,
	                                      Model model) {
	    try {
	        // Input validation
	        if (reason == null || reason.isEmpty()) {
	            model.addAttribute("error", "Reason is required.");
	            return "redirect:/user/user-orders"; // Or an error page
	        }

	        ProductOrder order = null;
	        Boolean sendMail = false;

	        switch (actionType) {
	            case "cancel":
	                order = orderService.updateOrderStatus(orderId, "Canceled", reason, detailedReason);
	                sendMail = commanUtil.sendMailForProductOrder(order, order.getStatus().getStatus());
	                break;

	            case "return":
	                order = orderService.updateOrderStatus(orderId, "Returned", reason, detailedReason);
	                sendMail = commanUtil.sendMailForProductOrder(order, order.getStatus().getStatus());
	                break;

	            default:
	                return "redirect:/user/user-orders";
	        }

	    } catch (Exception e) {
	        model.addAttribute("error", "An error occurred while processing your request.");
	        return "redirect:/user/user-orders";
	    }

	    return "redirect:/user/user-orders";
	}

	@PostMapping("/replaceOrderProducts")
	public String replaceOrderProducts(@RequestParam Integer orderId, 
	                                   @RequestParam(defaultValue = "0") Integer pageNo,
	                                   @RequestParam(defaultValue = "3") Integer pageSize,
	                                   Model model) {
	    try {
	        // Fetch the replacement products with pagination
	        Page<Product> page = productService.findReplacementProductsPagination(orderId, pageNo, pageSize);
	        List<Product> replacementProducts = page.getContent();

	        // Add attributes to the model for rendering the view
	        model.addAttribute("replacementProducts", replacementProducts);
	        model.addAttribute("replacementProductsSize", replacementProducts.size());
	        model.addAttribute("pageNo", page.getNumber()); // Ensure this is the current page number
	        model.addAttribute("pageSize", pageSize);
	        model.addAttribute("totalElements", page.getTotalElements());
	        model.addAttribute("totalPages", page.getTotalPages());
	        model.addAttribute("isFirst", page.isFirst());
	        model.addAttribute("isLast", page.isLast());
	        model.addAttribute("orderId", orderId);

	        return "user/select_replacement_products";

	    } catch (Exception e) {
	        model.addAttribute("error", "An error occurred while fetching replacement products.");
	        return "redirect:/user/user-orders";
	    }
	}


	
	@PostMapping("/replaceProductSelect")
	public String replaceProductSelect(@RequestParam Integer orderId) {
		ProductOrder order = orderService.updateOrderStatusReplaced(orderId, "Replaced");

		Boolean sendMail = commanUtil.sendMailForProductOrder(order, order.getStatus().getStatus());

        return "redirect:/user/user-orders";
	}
	
	@GetMapping("/profile")
	public String profile() {
		return "/user/profile";
	}
	
	@PostMapping("/uploadProfileImage")
    public String uploadProfileImage(@RequestParam MultipartFile profileImage, HttpSession session, Principal principal) {
		
		if (profileImage.isEmpty()) {
        	session.setAttribute("errorMsg", "No file selected for upload.");
            return "redirect:/user/profile";
        }
        
        String image = (!ObjectUtils.isEmpty(profileImage)) ? profileImage.getOriginalFilename() : "default.jpg";
        UserDtls user = userService.getLoggedInUserDetails(principal);
        user.setProfileImage(image);
        
        UserDtls updateProfileImage = null;

        try {
        	updateProfileImage = userService.uploadProfileImage(user, profileImage);
        } catch (IOException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Failed to upload the profile image.");
        } catch (Exception e) {
			session.setAttribute("errorMsg", "Unexpected error occurred: " + e.getMessage());
		}
		
		if(ObjectUtils.isEmpty(updateProfileImage)) {
			session.setAttribute("errorMsg", "Profile Image Not updated! Something wrong on Server side");
		} else {
			session.setAttribute("successMsg", "Profile image updated successfully.");
		}
        return "redirect:/user/profile";
    }
	
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute UserDtls userDtls, HttpSession session, Principal principal) {
		
		if(userDtls == null) {
			session.setAttribute("errorMsg", "Update Information is missing..");
			return "redirect:/user/profile";
		}
		
		UserDtls user = userService.getLoggedInUserDetails(principal);
		
		UserDtls updateProfileUser = userService.updateProfile(user, userDtls);
		
		if(updateProfileUser != null) {
			session.setAttribute("successMsg", "Profile updated successfully.");
		} else {
			session.setAttribute("errorMsg", "Profile Not updated! Something wrong on Server side");
		}
		
		return "redirect:/user/profile";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam String password, @RequestParam String newPassword, Principal principal, RedirectAttributes redirectAttributes) {
		
		UserDtls user = userService.getLoggedInUserDetails(principal);
		Boolean matches = passwordEncoder.matches(password,user.getPassword());
		if(matches) {
			String encodePassword = passwordEncoder.encode(newPassword);
			user.setPassword(encodePassword);
			UserDtls updatePasswordUser = userService.updateUser(user);
			if (ObjectUtils.isEmpty(updatePasswordUser)) {
	            redirectAttributes.addFlashAttribute("errorMsg", "Password Not updated! Something went wrong on the server side.");
	        } else {
	            redirectAttributes.addFlashAttribute("successMsg", "Password updated successfully.");
	        }
	    } else {
	        redirectAttributes.addFlashAttribute("errorMsg", "Current Password Incorrect");
	    }
	    
	    return "redirect:/user/profile#update-password";
	}
}
