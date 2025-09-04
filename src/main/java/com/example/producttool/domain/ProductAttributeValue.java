package com.example.producttool.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_attribute_values",
	uniqueConstraints = @UniqueConstraint(name = "uk_product_attribute", columnNames = {"product_id", "category_attribute_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeValue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_attribute_id", nullable = false)
	private CategoryAttribute categoryAttribute;

	@Column(name = "value_string", length = 2000)
	private String valueString;

	@Column(name = "value_number")
	private Double valueNumber;

	@Column(name = "value_boolean")
	private Boolean valueBoolean;

	@Column(name = "value_date")
	private java.time.LocalDate valueDate;
} 