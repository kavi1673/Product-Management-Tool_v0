package com.example.producttool.repository;

import com.example.producttool.domain.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findBySku(String sku);

	@EntityGraph(attributePaths = {"category", "attributeValues", "attributeValues.categoryAttribute"})
	Optional<Product> findWithAttributesById(Long id);
} 