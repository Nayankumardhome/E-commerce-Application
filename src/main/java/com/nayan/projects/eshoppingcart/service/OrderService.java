package com.nayan.projects.eshoppingcart.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nayan.projects.eshoppingcart.model.OrderRequest;
import com.nayan.projects.eshoppingcart.model.OrderResponseDto;
import com.nayan.projects.eshoppingcart.model.ProductOrder;
import com.nayan.projects.eshoppingcart.model.UserDtls;

public interface OrderService {

	public OrderResponseDto orderPlace(UserDtls user, OrderRequest orderRequest);

	public List<ProductOrder> getAllOrders(Integer id);
	
	public List<String> getTimeFilters();

	public List<ProductOrder> filterOrdersByYearAndStatus(UserDtls user, List<String> selectedStatus, List<String> selectedTime);

	public List<String> getOrderStatuses();

	public List<ProductOrder> searchOrders(UserDtls user, String searchQuery);

	public ProductOrder updateOrderStatus(Integer orderId, String string, String reason, String detailedReason);
	
	public ProductOrder updateOrderStatusReplaced(Integer orderId, String status);

	public List<ProductOrder> filterOrdersByYearAndStatus(List<String> selectedStatus, List<String> selectedTime);

	public List<ProductOrder> getAllOrders();

	public List<ProductOrder> searchOrders(Long productOrderId);

	public ProductOrder updateOrderStatus(Integer id, Integer statusId) throws Exception;

	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);

	public Page<ProductOrder> filterOrdersByYearAndStatusPagination(List<String> selectedStatus, List<String> selectedTime,
			Integer pageNo, Integer pageSize);

	public Page<ProductOrder> getAllOrdersPagination(Integer id, Integer pageNo, Integer pageSize);

	public Page<ProductOrder> filterOrdersByYearAndStatusPagination(UserDtls user, List<String> selectedStatus,
			List<String> selectedTime, Integer pageNo, Integer pageSize);

	public Page<ProductOrder> searchOrdersPagination(UserDtls user, String searchQuery, Integer pageNo,
			Integer pageSize);
}
