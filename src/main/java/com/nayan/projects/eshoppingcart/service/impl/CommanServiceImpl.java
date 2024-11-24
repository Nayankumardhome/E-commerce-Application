package com.nayan.projects.eshoppingcart.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nayan.projects.eshoppingcart.service.CommanService;

import jakarta.servlet.http.HttpSession;

@Service
public class CommanServiceImpl implements CommanService{

	@Override
	public void removeSessionMsg() {
		HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes()))
									.getRequest().getSession();
		session.removeAttribute("successMsg");
		session.removeAttribute("errorMsg");
	}

}
