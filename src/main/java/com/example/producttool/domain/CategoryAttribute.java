package com.example.producttool.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category_attributes",
	uniqueConstraints = @UniqueConstraint(name = "uk_category_attribute_name", columnNames = {"category_id", "name"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAttribute {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(nullable = false, length = 128)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private DataType dataType;

	@Column(nullable = false)
	private boolean required;

	@Column(name = "allowed_values", length = 2000)
	private String allowedValuesCsv;
} 