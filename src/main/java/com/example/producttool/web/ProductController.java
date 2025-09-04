package com.example.producttool.web;

import com.example.producttool.domain.Product;
import com.example.producttool.domain.ProductAttributeValue;
import com.example.producttool.dto.ProductDtos.*;
import com.example.producttool.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products")
public class ProductController {
	private final ProductService productService;

	@PostMapping
	public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest req) {
		Product p = productService.createProduct(req);
		return ResponseEntity.created(URI.create("/api/products/" + p.getId())).body(toResponse(p));
	}

	@GetMapping("/{id}")
	public ProductResponse get(@PathVariable Long id) {
		return toResponse(productService.getProduct(id));
	}

	@PatchMapping("/{id}")
	public ProductResponse update(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest req) {
		Product p = productService.updateProduct(id, req);
		return toResponse(p);
	}

	private ProductResponse toResponse(Product p) {
		return new ProductResponse(
				p.getId(),
				p.getSku(),
				p.getTitle(),
				p.getDescription(),
				p.getCategory().getId(),
				p.getCategory().getName(),
				p.getPrice(),
				p.isActive(),
				p.getAttributeValues().stream().map(this::toAttributeResponse).toList()
		);
	}

	private ProductAttributeValueResponse toAttributeResponse(ProductAttributeValue v) {
		String value = switch (v.getCategoryAttribute().getDataType()) {
			case STRING -> v.getValueString();
			case NUMBER -> v.getValueNumber() == null ? null : v.getValueNumber().toString();
			case BOOLEAN -> v.getValueBoolean() == null ? null : v.getValueBoolean().toString();
			case DATE -> v.getValueDate() == null ? null : v.getValueDate().toString();
		};
		return new ProductAttributeValueResponse(
				v.getCategoryAttribute().getId(),
				v.getCategoryAttribute().getName(),
				v.getCategoryAttribute().getDataType().name(),
				value
		);
	}
} 