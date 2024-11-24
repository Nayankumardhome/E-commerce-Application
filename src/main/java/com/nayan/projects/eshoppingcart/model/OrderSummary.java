package com.nayan.projects.eshoppingcart.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
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
public class OrderSummary {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
	@SequenceGenerator(name = "order_generator", sequenceName = "order_seq", allocationSize = 1)
	private Integer id;
	
	private Long orderId;

    @OneToMany(mappedBy = "orderSummary", cascade = CascadeType.ALL)
    private List<ProductOrder> productOrders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDtls user;
    
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private OrderAddress deliveryAddress;  // Delivery address for this order

    private LocalDate orderDate;
    private Double totalPrice;   // Total price of the order
    private Double discount;     // Total discount applied
    private Double platformFee;  // Platform fees (if applicable)
    private Double deliveryCharges;
    private Double finalAmount;  // Final amount after applying discount and fees
    
    private String paymentType;  // e.g., "CARD", "UPI", "COD"
}
