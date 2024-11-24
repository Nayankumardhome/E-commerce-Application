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
public class Product{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
	@SequenceGenerator(name = "product_generator", sequenceName = "product_seq", allocationSize = 1)
	private Integer id;
	@Column(length = 500, name = "product_name")
	private String productName;
	@Column(length = 5000)
	private String description;
	
	/*Advance stuff
	 * @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
    @JoinColumn(name = "category_id")
    private Category category;
	 */
	private String category;
	private Double price;
	private int stock;
	private String image;
	
	private Double discount;
	private Double discountPrice;
	
	@Column(name = "is_active", columnDefinition = "TINYINT(1)")
	private Boolean isActive;
}
