package com.nayan.projects.eshoppingcart.model;

import java.time.LocalDate;

import com.nayan.projects.eshoppingcart.util.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Getter
@Setter
@Entity
public class ProductOrder {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_order_generator")
	@SequenceGenerator(name = "product_order_generator", sequenceName = "product_order_seq", allocationSize = 1)
	private Integer id;
	
	private Long productOrderId;
	
	@ManyToOne
    @JoinColumn(name = "order_summary_id", nullable = false)
    private OrderSummary orderSummary;
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDtls user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;
    private Double price;
    private Double discount;
    
    private OrderStatus status;
    private LocalDate cancleDate;
    private LocalDate returnDate;
    private LocalDate replacedDate;
    private LocalDate deliveredDate;
    
    @Transient
    private String reason;
    @Transient
    private String detailedReason;
    
    
}
