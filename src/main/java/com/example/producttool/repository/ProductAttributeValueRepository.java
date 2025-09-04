package com.example.producttool.repository;

import com.example.producttool.domain.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
} 