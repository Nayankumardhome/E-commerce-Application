package com.nayan.projects.eshoppingcart.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.repository.UserRepository;
import com.nayan.projects.eshoppingcart.service.UserService;
import com.nayan.projects.eshoppingcart.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String email = request.getParameter("username");

		UserDtls user = userRepository.findByEmail(email);

		if(user != null) {

			if(user.getIsEnable()) {

				if(user.getAccountNonLocked()) {

					if(user.getFailedAttempts() < AppConstant.ATTEMPT_TIME) {
						userService.increaseFailedAttempt(user);
					} else {
						userService.userAccountLock(user);
						exception = new LockedException("Failed Attempts 3.\nYour Account is TEMPORARY BLOCK for 1hr.");
					}

				} else {

					if(userService.unlockAccountTimeExpired(user)) {
						exception = new LockedException("Your Account is UNBLOCK please try to login.");
					} else {
						exception = new LockedException("Your Account is TEMPORARY BLOCK.\nPlease try after : " + (((((user.getLockTime().getTime()) + AppConstant.UNLOCK_DURATION_TIME) - System.currentTimeMillis()))/60000) + " minutes");
					}
				}

			} else {
				exception = new LockedException("Your Account is INACTIVE.");
			}
		} else {
			exception = new LockedException("Invalid Email or Password.");
		}

		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}
}