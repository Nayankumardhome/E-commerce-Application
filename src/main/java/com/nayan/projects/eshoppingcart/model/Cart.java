package com.nayan.projects.eshoppingcart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
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
public class Cart {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_generator")
	@SequenceGenerator(name = "cart_generator", sequenceName = "cart_seq", allocationSize = 1)
	private Integer id;
	
	@ManyToOne
	private UserDtls user;
	
	@ManyToOne
	private Product product;
	
	private Integer quantity;
	
	@Transient
	private Double totalPrice;
	
	@Transient
	private Double totalOrderPrice;
}
