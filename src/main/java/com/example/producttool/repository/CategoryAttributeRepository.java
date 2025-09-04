package com.example.producttool.repository;

import com.example.producttool.domain.Category;
import com.example.producttool.domain.CategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, Long> {
	List<CategoryAttribute> findByCategoryId(Long categoryId);
	Optional<CategoryAttribute> findByCategoryAndNameIgnoreCase(Category category, String name);
} 