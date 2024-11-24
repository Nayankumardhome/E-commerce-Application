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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "category")
public class Category {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
	@SequenceGenerator(name = "category_generator", sequenceName = "category_seq", allocationSize = 1)
	private Integer id;
	private String name;
	private String image;
	
	@Column(name = "is_active", columnDefinition = "TINYINT(1)")
	private Boolean isActive;
}
