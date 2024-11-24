package com.nayan.projects.eshoppingcart.service;

import com.nayan.projects.eshoppingcart.model.OrderRequest;
import com.nayan.projects.eshoppingcart.model.UserDtls;

public interface CODOrderService {

	public Boolean CODOrder(UserDtls user, OrderRequest orderRequest) throws Exception;

}
