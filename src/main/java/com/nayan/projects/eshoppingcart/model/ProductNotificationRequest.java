package com.nayan.projects.eshoppingcart.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "product_notification_request")
public class ProductNotificationRequest {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_generator")
	@SequenceGenerator(name = "notification_generator", sequenceName = "notification_seq", allocationSize = 1)
	private Integer id;
	
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "user_id")
	private Integer userId;
}
