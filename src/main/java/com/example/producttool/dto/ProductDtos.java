package com.example.producttool.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class ProductDtos {
	public record CreateProductRequest(
			@NotBlank @Size(max = 64) String sku,
			@NotBlank @Size(max = 256) String title,
			@Size(max = 2000) String description,
			@NotNull Long categoryId,
			@DecimalMin(value = "0.0", inclusive = true) BigDecimal price,
			@NotNull Boolean active,
			@Valid List<ProductAttributeValueUpsert> attributes
	) {}

	public record UpdateProductRequest(
			@Size(max = 256) String title,
			@Size(max = 2000) String description,
			@DecimalMin(value = "0.0", inclusive = true) BigDecimal price,
			Boolean active,
			@Valid List<ProductAttributeValueUpsert> attributes
	) {}

	public record ProductAttributeValueUpsert(
			@NotNull Long categoryAttributeId,
			String valueString,
			Double valueNumber,
			Boolean valueBoolean,
			String valueDate
	) {}

	public record ProductResponse(
			Long id,
			String sku,
			String title,
			String description,
			Long categoryId,
			String categoryName,
			BigDecimal price,
			boolean active,
			List<ProductAttributeValueResponse> attributes
	) {}

	public record ProductAttributeValueResponse(
			Long categoryAttributeId,
			String attributeName,
			String dataType,
			String value
	) {}
} 