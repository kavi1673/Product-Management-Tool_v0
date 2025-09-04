package com.example.producttool.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CategoryDtos {
	public record CreateCategoryRequest(
			@NotBlank @Size(max = 128) String name,
			@Size(max = 512) String description
	) {}

	public record UpdateCategoryRequest(
			@Size(max = 512) String description
	) {}

	public record CategoryResponse(
			Long id,
			String name,
			String description,
			List<CategoryAttributeResponse> attributes
	) {}

	public record CreateCategoryAttributeRequest(
			@NotBlank @Size(max = 128) String name,
			@NotBlank String dataType,
			@NotNull Boolean required,
			@Size(max = 2000) String allowedValuesCsv
	) {}

	public record CategoryAttributeResponse(
			Long id,
			String name,
			String dataType,
			boolean required,
			String allowedValuesCsv
	) {}
} 