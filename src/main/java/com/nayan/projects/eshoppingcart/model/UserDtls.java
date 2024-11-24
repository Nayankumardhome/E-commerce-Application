package com.nayan.projects.eshoppingcart.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class UserDtls {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
	@SequenceGenerator(name = "user_generator", sequenceName = "user_seq", allocationSize = 1)
	private Integer id;
	
	private String name;
	
	@Column(length = 50,name = "mobile_number")
	private String mobileNumber;
	
	private String email;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private Integer pincode;
	
	private String password;
	
	@Column(name = "profile_image")
	private String profileImage;
	
	private String role;
	
	@Column(name = "is_enable", columnDefinition = "TINYINT(1)")
	private Boolean isEnable;
	
	@Column(name = "account_non_locked", columnDefinition = "TINYINT(1)")
	private Boolean accountNonLocked;
	
	@Column(name = "failed_attempts")
	private Integer failedAttempts;
	
	@Column(name = "lock_time")
	private Date lockTime;
	
	@Column(name = "reset_token")
	private String resetToken;
}
