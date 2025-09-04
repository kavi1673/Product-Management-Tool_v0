package com.example.producttool.web;

import com.example.producttool.domain.Category;
import com.example.producttool.domain.CategoryAttribute;
import com.example.producttool.dto.CategoryDtos.*;
import com.example.producttool.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories")
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping
	public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CreateCategoryRequest req) {
		Category c = categoryService.createCategory(req);
		return ResponseEntity.created(URI.create("/api/categories/" + c.getId()))
				.body(new CategoryResponse(c.getId(), c.getName(), c.getDescription(), List.of()));
	}

	@GetMapping("/{id}")
	public CategoryResponse get(@PathVariable Long id) {
		Category c = categoryService.getCategoryOrThrow(id);
		List<CategoryAttribute> attrs = categoryService.listAttributes(id);
		return new CategoryResponse(
				c.getId(), c.getName(), c.getDescription(),
				attrs.stream().map(a -> new CategoryAttributeResponse(a.getId(), a.getName(), a.getDataType().name(), a.isRequired(), a.getAllowedValuesCsv())).toList()
		);
	}

	@PatchMapping("/{id}")
	public CategoryResponse update(@PathVariable Long id, @RequestBody @Valid UpdateCategoryRequest req) {
		Category c = categoryService.updateCategory(id, req);
		List<CategoryAttribute> attrs = categoryService.listAttributes(id);
		return new CategoryResponse(
				c.getId(), c.getName(), c.getDescription(),
				attrs.stream().map(a -> new CategoryAttributeResponse(a.getId(), a.getName(), a.getDataType().name(), a.isRequired(), a.getAllowedValuesCsv())).toList()
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/attributes")
	public ResponseEntity<CategoryAttributeResponse> addAttribute(@PathVariable Long id, @RequestBody @Valid CreateCategoryAttributeRequest req) {
		CategoryAttribute a = categoryService.addAttribute(id, req);
		return ResponseEntity.created(URI.create("/api/categories/" + id + "/attributes/" + a.getId()))
				.body(new CategoryAttributeResponse(a.getId(), a.getName(), a.getDataType().name(), a.isRequired(), a.getAllowedValuesCsv()));
	}

	@GetMapping("/{id}/attributes")
	public List<CategoryAttributeResponse> listAttributes(@PathVariable Long id) {
		return categoryService.listAttributes(id).stream()
				.map(a -> new CategoryAttributeResponse(a.getId(), a.getName(), a.getDataType().name(), a.isRequired(), a.getAllowedValuesCsv()))
				.toList();
	}
} 