package com.example.producttool.repository;

import com.example.producttool.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByNameIgnoreCase(String name);
} 