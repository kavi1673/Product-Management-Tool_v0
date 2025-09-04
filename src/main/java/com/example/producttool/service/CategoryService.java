package com.example.producttool.service;

import com.example.producttool.domain.Category;
import com.example.producttool.domain.CategoryAttribute;
import com.example.producttool.domain.DataType;
import com.example.producttool.dto.CategoryDtos.*;
import com.example.producttool.repository.CategoryAttributeRepository;
import com.example.producttool.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final CategoryAttributeRepository attributeRepository;

	@Transactional
	public Category createCategory(CreateCategoryRequest req) {
		categoryRepository.findByNameIgnoreCase(req.name()).ifPresent(c -> {
			throw new IllegalArgumentException("Category already exists: " + req.name());
		});
		Category category = Category.builder()
				.name(req.name().trim())
				.description(req.description())
				.build();
		return categoryRepository.save(category);
	}

	@Transactional(readOnly = true)
	public Category getCategoryOrThrow(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
	}

	@Transactional
	public Category updateCategory(Long id, UpdateCategoryRequest req) {
		Category category = getCategoryOrThrow(id);
		if (req.description() != null) category.setDescription(req.description());
		return categoryRepository.save(category);
	}

	@Transactional
	public void deleteCategory(Long id) {
		Category category = getCategoryOrThrow(id);
		categoryRepository.delete(category);
	}

	@Transactional
	public CategoryAttribute addAttribute(Long categoryId, CreateCategoryAttributeRequest req) {
		Category category = getCategoryOrThrow(categoryId);
		attributeRepository.findByCategoryAndNameIgnoreCase(category, req.name()).ifPresent(a -> {
			throw new IllegalArgumentException("Attribute already exists: " + req.name());
		});
		DataType type = DataType.valueOf(req.dataType().toUpperCase());
		CategoryAttribute attribute = CategoryAttribute.builder()
				.category(category)
				.name(req.name().trim())
				.dataType(type)
				.required(Boolean.TRUE.equals(req.required()))
				.allowedValuesCsv(req.allowedValuesCsv())
				.build();
		return attributeRepository.save(attribute);
	}

	@Transactional(readOnly = true)
	public List<CategoryAttribute> listAttributes(Long categoryId) {
		return attributeRepository.findByCategoryId(categoryId);
	}
} 