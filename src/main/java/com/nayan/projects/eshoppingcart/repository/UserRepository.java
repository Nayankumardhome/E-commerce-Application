package com.nayan.projects.eshoppingcart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nayan.projects.eshoppingcart.model.UserDtls;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer>{

	public UserDtls findByEmail(String email);

	public List<UserDtls> findByRole(String role);
	
	public UserDtls findByResetToken(String resetToken);

	public Page<UserDtls> findByRole(String string, Pageable pageable);
}
