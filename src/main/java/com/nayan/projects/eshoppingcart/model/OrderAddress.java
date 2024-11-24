package com.nayan.projects.eshoppingcart.model;

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
public class OrderAddress {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_generator")
	@SequenceGenerator(name = "address_generator", sequenceName = "address_seq", allocationSize = 1)
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private String mobileNumber;
	
	private String email;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private Integer pincode;
}
