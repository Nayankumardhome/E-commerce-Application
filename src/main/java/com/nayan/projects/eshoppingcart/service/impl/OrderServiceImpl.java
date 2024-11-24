package com.nayan.projects.eshoppingcart.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nayan.projects.eshoppingcart.model.Cart;
import com.nayan.projects.eshoppingcart.model.OrderAddress;
import com.nayan.projects.eshoppingcart.model.OrderRequest;
import com.nayan.projects.eshoppingcart.model.OrderResponseDto;
import com.nayan.projects.eshoppingcart.model.OrderSummary;
import com.nayan.projects.eshoppingcart.model.ProductOrder;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.repository.CartRepository;
import com.nayan.projects.eshoppingcart.repository.OrderAddressRepository;
import com.nayan.projects.eshoppingcart.repository.OrderSummaryRepository;
import com.nayan.projects.eshoppingcart.repository.ProductOrderRepository;
import com.nayan.projects.eshoppingcart.service.OrderService;
import com.nayan.projects.eshoppingcart.service.UPIOrderService;
import com.nayan.projects.eshoppingcart.util.CommanUtil;
import com.nayan.projects.eshoppingcart.util.OrderResponse;
import com.nayan.projects.eshoppingcart.util.OrderStatus;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderAddressRepository addressRepository;
	
	@Autowired
	private OrderSummaryRepository summaryRepository;
	
	@Autowired
	private UPIOrderService upiOrderService;
	
	@Autowired
	private ProductOrderRepository productOrderRepository;
	
	@Autowired
	private CommanUtil commanUtil;

	@Transactional
	@Override
	public OrderResponseDto orderPlace(UserDtls user, OrderRequest orderRequest) {
		
//		DecimalFormat decfor = new DecimalFormat("0.00");

	    List<Cart> carts = cartRepository.findByUserId(user.getId());
	    Double totalDiscount = 0.0;
	    Double totalPrice = 0.0;
	    List<ProductOrder> productOrders = new ArrayList<>();

	    OrderSummary orderSummary = new OrderSummary();
	    orderSummary.setOrderId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
	    orderSummary.setUser(user);
	    orderSummary.setOrderDate(LocalDate.now());

	    OrderAddress address = new OrderAddress();
	    address.setFirstName(orderRequest.getFirstName());
	    address.setLastName(orderRequest.getLastName());
	    address.setEmail(orderRequest.getEmail());
	    address.setMobileNumber(orderRequest.getMobileNumber());
	    address.setAddress(orderRequest.getAddress());
	    address.setCity(orderRequest.getCity());
	    address.setState(orderRequest.getState());
	    address.setPincode(orderRequest.getPincode());
	    orderSummary.setDeliveryAddress(address);
	    orderSummary.setPaymentType(orderRequest.getPaymentType());

	    for (Cart cart : carts) {
	        ProductOrder productOrder = new ProductOrder();
	        productOrder.setProductOrderId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
	        productOrder.setProduct(cart.getProduct());
	        productOrder.setQuantity(cart.getQuantity());
	        productOrder.setUser(user);
	        productOrder.setStatus(OrderStatus.IN_PROGRESS);

	        Double price = cart.getProduct().getPrice() * cart.getQuantity();
	        totalPrice += price;

	        Double discountPercentage = cart.getProduct().getDiscount();
	        Double discount = price * (discountPercentage / 100);
	        productOrder.setDiscount(discount);

	        totalDiscount += discount;

	        productOrder.setPrice(price - discount);

	        productOrder.setOrderSummary(orderSummary);

	        productOrders.add(productOrder);
	    }

	    Double finalAmount = totalPrice - totalDiscount;
	    orderSummary.setPlatformFee(3.0);

	    if (finalAmount >= 500) {
	        orderSummary.setDeliveryCharges(0.0);
	    } else {
	        orderSummary.setDeliveryCharges(40.0);
	    }

	    orderSummary.setTotalPrice(totalPrice);
	    orderSummary.setDiscount(totalDiscount);
	    orderSummary.setFinalAmount(finalAmount + orderSummary.getPlatformFee() + orderSummary.getDeliveryCharges());
	    orderSummary.setProductOrders(productOrders);

	    if ("COD".equals(orderRequest.getPaymentType())) {
	        OrderAddress orderAddress = addressRepository.save(address); 
	        OrderSummary summary = summaryRepository.save(orderSummary);
	        for(ProductOrder order: productOrders) {
				Boolean sendMail = commanUtil.sendMailForProductOrder(order, order.getStatus().getStatus());
	        }

	        cartRepository.deleteByUserId(user.getId());

	        if (orderAddress != null && summary != null) {
	            return new OrderResponseDto(OrderResponse.ORDER_PLACED.getId(), OrderResponse.ORDER_PLACED.name());
	        } else {
	            return new OrderResponseDto(OrderResponse.ORDER_ERROR.getId(), OrderResponse.ORDER_ERROR.name());
	        }
	    } else if("UPI".equals(orderRequest.getPaymentType())) {
//            String url = upiOrderService.UPIOrder(orderSummary.getOrderId().toString(), finalAmount);
//            
//            return new OrderResponseDto(OrderResponse.ORDER_PENDING.getId(), OrderResponse.ORDER_PENDING.getStatus(), url);
        } else if("Card".equals(orderRequest.getPaymentType())) {
        	//card logic
        }

		
		return new OrderResponseDto(OrderResponse.ORDER_ERROR.getId(), OrderResponse.ORDER_ERROR.name());
	}

	@Override
	public List<ProductOrder> getAllOrders(Integer id) {
		return productOrderRepository.findByUserId(id);
	}

	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer id, Integer pageNo, Integer pageSize) {
				
		List<ProductOrder> productOrders = productOrderRepository.findByUserId(id);
		
		Collections.reverse(productOrders);
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		int start = Math.min((int) pageable.getOffset(), productOrders.size());
	    int end = Math.min((start + pageable.getPageSize()), productOrders.size());
	    List<ProductOrder> pagedOrders = productOrders.subList(start, end);
		
		return new PageImpl<ProductOrder>(pagedOrders, pageable, productOrders.size());
	}

	public List<String> getTimeFilters() {
        List<Integer> orderYears = productOrderRepository.findDistinctOrderYears();
        
        List<String> timeFilters = new ArrayList<>();

        timeFilters.add("Last 30 days");

        for (int i = 0; i < Math.min(orderYears.size(), 3); i++) {
            timeFilters.add(orderYears.get(i).toString());
        }

        if (orderYears.size() > 3) {
            timeFilters.add("Older");
        }

        return timeFilters;
    }

	@Override
	public List<ProductOrder> filterOrdersByYearAndStatus(UserDtls user, List<String> selectedStatus, List<String> selectedTime) {
	    List<ProductOrder> allOrders = productOrderRepository.findByUserId(user.getId());
	    LocalDate currentDate = LocalDate.now();

	    return allOrders.stream()
	        .filter(order -> {
	            String orderStatus = order.getStatus().getFinalStatus();
	            
	            boolean statusMatch = selectedStatus == null || selectedStatus.isEmpty() || selectedStatus.contains(orderStatus);

	            LocalDate orderDate = order.getOrderSummary().getOrderDate();
	            int orderYear = orderDate.getYear();

	            boolean timeMatch = false;

	            if (selectedTime == null || selectedTime.isEmpty()) {
	                timeMatch = true;
	            } else {
	                if (selectedTime.contains("Last 30 days") && orderDate.isAfter(currentDate.minusDays(30))) {
	                    timeMatch = true;
	                }

	                if (selectedTime.contains(String.valueOf(orderYear))) {
	                    timeMatch = true;
	                }

	                if (selectedTime.contains("Older") && orderDate.isBefore(currentDate.minusYears(3))) {
	                    timeMatch = true;
	                }
	            }

	            return statusMatch && timeMatch;
	        })
	        .collect(Collectors.toList());
	}

	@Override
	public Page<ProductOrder> filterOrdersByYearAndStatusPagination(UserDtls user, List<String> selectedStatus,
			List<String> selectedTime, Integer pageNo, Integer pageSize) {
		List<ProductOrder> allOrders = productOrderRepository.findByUserId(user.getId());
	    LocalDate currentDate = LocalDate.now();

	    List<ProductOrder> orders = allOrders.stream()
	        .filter(order -> {
	            String orderStatus = order.getStatus().getFinalStatus();
	            
	            boolean statusMatch = selectedStatus == null || selectedStatus.isEmpty() || selectedStatus.contains(orderStatus);

	            LocalDate orderDate = order.getOrderSummary().getOrderDate();
	            int orderYear = orderDate.getYear();

	            boolean timeMatch = false;

	            if (selectedTime == null || selectedTime.isEmpty()) {
	                timeMatch = true;
	            } else {
	                if (selectedTime.contains("Last 30 days") && orderDate.isAfter(currentDate.minusDays(30))) {
	                    timeMatch = true;
	                }

	                if (selectedTime.contains(String.valueOf(orderYear))) {
	                    timeMatch = true;
	                }

	                if (selectedTime.contains("Older") && orderDate.isBefore(currentDate.minusYears(3))) {
	                    timeMatch = true;
	                }
	            }

	            return statusMatch && timeMatch;
	        })
	        .collect(Collectors.toList());
	    
	    Collections.reverse(orders);
	    Pageable pageable = PageRequest.of(pageNo, pageSize);
	    int start = Math.min((int) pageable.getOffset(), orders.size());
	    int end = Math.min(start + pageable.getPageSize(), orders.size());
	    List<ProductOrder> pagedOrders = orders.subList(start, end);
	    
	    return new PageImpl<ProductOrder>(pagedOrders, pageable, orders.size());
	}

	@Override
	public Page<ProductOrder> filterOrdersByYearAndStatusPagination(List<String> selectedStatus,
			List<String> selectedTime, Integer pageNo, Integer pageSize) {
		
	    List<ProductOrder> allOrders = productOrderRepository.findAll();
	    LocalDate currentDate = LocalDate.now();


	    List<ProductOrder> filteredOrders = allOrders.stream()
	        .filter(order -> {
	            String orderStatus = order.getStatus().getFinalStatus();
	            
	            boolean statusMatch = selectedStatus == null || selectedStatus.isEmpty() || selectedStatus.contains(orderStatus);

	            LocalDate orderDate = order.getOrderSummary().getOrderDate();
	            int orderYear = orderDate.getYear();

	            boolean timeMatch = false;

	            if (selectedTime == null || selectedTime.isEmpty()) {
	                timeMatch = true;
	            } else {
	                if (selectedTime.contains("Last 30 days") && orderDate.isAfter(currentDate.minusDays(30))) {
	                    timeMatch = true;
	                }

	                if (selectedTime.contains(String.valueOf(orderYear))) {
	                    timeMatch = true;
	                }

	                if (selectedTime.contains("Older") && orderDate.isBefore(currentDate.minusYears(3))) {
	                    timeMatch = true;
	                }
	            }

	            return statusMatch && timeMatch;
	        })
	        .collect(Collectors.toList());

	    Collections.reverse(filteredOrders);
	    Pageable pageable = PageRequest.of(pageNo, pageSize);
	    int start = Math.min((int) pageable.getOffset(), filteredOrders.size());
	    int end = Math.min((start + pageable.getPageSize()), filteredOrders.size());
	    List<ProductOrder> pagedOrders = filteredOrders.subList(start, end);

	    return new PageImpl<>(pagedOrders, pageable, filteredOrders.size());
	}

	@Override
	public List<String> getOrderStatuses() {
		List<String> orderStatuses = Arrays.asList(
	            OrderStatus.ORDER_RECEIVED.getFinalStatus(),
	            OrderStatus.DELIVERED.getFinalStatus(),
	            OrderStatus.CANCELED.getFinalStatus(),
	            OrderStatus.RETURNED.getFinalStatus(),
	            OrderStatus.REPLACED.getFinalStatus()
	        );
		return orderStatuses;
	}

	@Override
	public List<ProductOrder> searchOrders(UserDtls user, String searchQuery) {
	    List<ProductOrder> allOrders = productOrderRepository.findByUserId(user.getId());
	    
	    if (searchQuery == null || searchQuery.trim().isEmpty()) {
	        return allOrders;
	    }
	    
	    String searchField = searchQuery.toLowerCase();
	    
	    List<ProductOrder> searchOrders = allOrders.stream()
	        .filter(order -> {
	            String productName = order.getProduct().getProductName().toLowerCase();
	            String orderStatus = order.getStatus().getFinalStatus().toLowerCase(); // Use ProductOrder's getStatus()

	            return productName.contains(searchField) || orderStatus.contains(searchField);
	        })
	        .collect(Collectors.toList());
	    
	    return searchOrders;
	}

	@Override
	public Page<ProductOrder> searchOrdersPagination(UserDtls user, String searchQuery, Integer pageNo,
			Integer pageSize) {
		List<ProductOrder> allOrders = productOrderRepository.findByUserId(user.getId());
	    
	    if (searchQuery == null || searchQuery.trim().isEmpty()) {
	    	Collections.reverse(allOrders);
		    Pageable pageable = PageRequest.of(pageNo, pageSize);
		    int start = Math.min((int) pageable.getOffset(), allOrders.size());
		    int end = Math.min((start + pageable.getPageSize()), allOrders.size());
		    List<ProductOrder> pagedOrders = allOrders.subList(start, end);
		    
		    return new PageImpl<ProductOrder>(pagedOrders,pageable,allOrders.size());
	    }
	    
	    String searchField = searchQuery.toLowerCase();
	    
	    List<ProductOrder> searchOrders = allOrders.stream()
	        .filter(order -> {
	            String productName = order.getProduct().getProductName().toLowerCase();
	            String orderStatus = order.getStatus().getFinalStatus().toLowerCase(); // Use ProductOrder's getStatus()

	            return productName.contains(searchField) || orderStatus.contains(searchField);
	        })
	        .collect(Collectors.toList());
	    
	    Collections.reverse(searchOrders);
	    Pageable pageable = PageRequest.of(pageNo, pageSize);
	    int start = Math.min((int) pageable.getOffset(), searchOrders.size());
	    int end = Math.min((start + pageable.getPageSize()), searchOrders.size());
	    List<ProductOrder> pagedOrders = searchOrders.subList(start, end);
	    
	    return new PageImpl<ProductOrder>(pagedOrders,pageable,searchOrders.size());
	}

	@Override
	public ProductOrder updateOrderStatus(Integer orderId, String status, String reason, String detailedReason) {
		ProductOrder productOrder = productOrderRepository.findById(orderId).orElse(null);
		
		if(productOrder != null) {
			
			if("Canceled".equalsIgnoreCase(status)) {
				productOrder.setStatus(OrderStatus.CANCELED);
				productOrder.setCancleDate(LocalDate.now());
			} else if("Returned".equalsIgnoreCase(status)) {
				productOrder.setStatus(OrderStatus.RETURNED_IN_PROGRESS);
				productOrder.setReturnDate(LocalDate.now());
			}
	        productOrder.setReason(reason);
	        productOrder.setDetailedReason(detailedReason);
	        
	        return productOrderRepository.save(productOrder);
		}
		return null;
		
	}

	@Override
	public ProductOrder updateOrderStatusReplaced(Integer orderId, String status) {
		
		ProductOrder productOrder = productOrderRepository.findById(orderId).orElse(null);
		if(productOrder != null) {

			if("Replaced".equalsIgnoreCase(status)) {
				productOrder.setStatus(OrderStatus.REPLACED_IN_PROGRESS);
			}
			return productOrderRepository.save(productOrder);
		}
		return null;
	}

	@Override
	public List<ProductOrder> filterOrdersByYearAndStatus(List<String> selectedStatus, List<String> selectedTime) {
		List<ProductOrder> allOrders = productOrderRepository.findAll();
		LocalDate currentDate = LocalDate.now();

	    return allOrders.stream()
	        .filter(order -> {
	            String orderStatus = order.getStatus().getFinalStatus();
	            
	            boolean statusMatch = selectedStatus == null || selectedStatus.isEmpty() || selectedStatus.contains(orderStatus);

	            LocalDate orderDate = order.getOrderSummary().getOrderDate();
	            int orderYear = orderDate.getYear();

	            boolean timeMatch = false;

	            if (selectedTime == null || selectedTime.isEmpty()) {
	                timeMatch = true;
	            } else {
	                if (selectedTime.contains("Last 30 days") && orderDate.isAfter(currentDate.minusDays(30))) {
	                    timeMatch = true;
	                }

	                if (selectedTime.contains(String.valueOf(orderYear))) {
	                    timeMatch = true;
	                }

	                if (selectedTime.contains("Older") && orderDate.isBefore(currentDate.minusYears(3))) {
	                    timeMatch = true;
	                }
	            }

	            return statusMatch && timeMatch;
	        })
	        .collect(Collectors.toList());
	}

	@Override
	public List<ProductOrder> getAllOrders() {
		List<ProductOrder> orders = productOrderRepository.findAll();
		return orders;
	}

	@Override
	public List<ProductOrder> searchOrders(Long productOrderId) {
		List<ProductOrder> orders = productOrderRepository.findByProductOrderId(productOrderId);
		return orders;
	}

	@Override
	public ProductOrder updateOrderStatus(Integer id, Integer statusId) throws Exception {
		 Optional<ProductOrder> optionalOrder = productOrderRepository.findById(id);
	        if (!optionalOrder.isPresent()) {
	            throw new Exception("Order not found.");
	        }

	        ProductOrder order = optionalOrder.get();

	        OrderStatus newStatus = getOrderStatusById(statusId);
	        if(newStatus.getFinalStatus().equals("Delivered")) {
	        	order.setDeliveredDate(LocalDate.now());
	        } else if(newStatus.getFinalStatus().equals("Replaced")) {
	        	order.setReplacedDate(LocalDate.now());
	        } else if(newStatus.getFinalStatus().equals("Canceled")) {
	        	order.setCancleDate(LocalDate.now());
	        } else if(newStatus.getFinalStatus().equals("Returned")) {
	        	order.setReturnDate(LocalDate.now());
	        }

	        order.setStatus(newStatus);
	        return productOrderRepository.save(order);
	}
	
	private OrderStatus getOrderStatusById(Integer statusId) throws Exception {
        return Arrays.stream(OrderStatus.values())
            .filter(status -> status.getId().equals(statusId))
            .findFirst()
            .orElseThrow(() -> new Exception("Invalid status ID."));
    }

	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orderSummary.orderDate").descending());
		return productOrderRepository.findAll(pageable);
	}
}
