package com.example.producttool.service;

import com.example.producttool.domain.*;
import com.example.producttool.dto.ProductDtos.*;
import com.example.producttool.repository.CategoryAttributeRepository;
import com.example.producttool.repository.CategoryRepository;
import com.example.producttool.repository.ProductAttributeValueRepository;
import com.example.producttool.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final CategoryAttributeRepository attributeRepository;
	private final ProductAttributeValueRepository pavRepository;
	private final ValidationService validationService;

	@Transactional(readOnly = true)
	public Product getProduct(Long id) {
		return productRepository.findWithAttributesById(id)
				.orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
	}

	@Transactional
	public Product createProduct(CreateProductRequest req) {
		productRepository.findBySku(req.sku()).ifPresent(p -> { throw new IllegalArgumentException("SKU already exists: " + req.sku()); });
		Category category = categoryRepository.findById(req.categoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found: " + req.categoryId()));
		Product product = Product.builder()
				.sku(req.sku().trim())
				.title(req.title().trim())
				.description(req.description())
				.category(category)
				.price(req.price())
				.active(Boolean.TRUE.equals(req.active()))
				.build();
		product = productRepository.save(product);

		upsertAttributes(product, req.attributes());
		return productRepository.findWithAttributesById(product.getId()).orElse(product);
	}

	@Transactional
	public Product updateProduct(Long id, UpdateProductRequest req) {
		Product product = productRepository.findWithAttributesById(id).orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
		if (req.title() != null) product.setTitle(req.title());
		if (req.description() != null) product.setDescription(req.description());
		if (req.price() != null) product.setPrice(req.price());
		if (req.active() != null) product.setActive(req.active());
		product = productRepository.save(product);
		if (req.attributes() != null) {
			upsertAttributes(product, req.attributes());
		}
		return productRepository.findWithAttributesById(product.getId()).orElse(product);
	}

	private void upsertAttributes(Product product, List<ProductAttributeValueUpsert> upserts) {
		if (upserts == null || upserts.isEmpty()) return;
		Map<Long, CategoryAttribute> idToAttr = new HashMap<>();
		for (CategoryAttribute attr : attributeRepository.findByCategoryId(product.getCategory().getId())) {
			idToAttr.put(attr.getId(), attr);
		}
		for (ProductAttributeValueUpsert u : upserts) {
			CategoryAttribute attr = Optional.ofNullable(idToAttr.get(u.categoryAttributeId()))
					.orElseThrow(() -> new IllegalArgumentException("Attribute not in product category: " + u.categoryAttributeId()));
			Object typedValue = coerceValue(attr.getDataType(), u);
			validationService.validateValue(attr, typedValue);

			Optional<ProductAttributeValue> existing = product.getAttributeValues().stream()
					.filter(v -> v.getCategoryAttribute().getId().equals(attr.getId()))
					.findFirst();
			ProductAttributeValue value = existing.orElseGet(() -> ProductAttributeValue.builder()
					.product(product)
					.categoryAttribute(attr)
					.build());
			assignValue(value, attr.getDataType(), typedValue);
			pavRepository.save(value);
			if (existing.isEmpty()) product.getAttributeValues().add(value);
		}
	}

	private Object coerceValue(DataType type, ProductAttributeValueUpsert u) {
		return switch (type) {
			case STRING -> u.valueString();
			case NUMBER -> u.valueNumber();
			case BOOLEAN -> u.valueBoolean();
			case DATE -> u.valueDate() == null ? null : LocalDate.parse(u.valueDate());
		};
	}

	private void assignValue(ProductAttributeValue value, DataType type, Object typed) {
		value.setValueString(null);
		value.setValueNumber(null);
		value.setValueBoolean(null);
		value.setValueDate(null);
		switch (type) {
			case STRING -> value.setValueString((String) typed);
			case NUMBER -> value.setValueNumber((Double) typed);
			case BOOLEAN -> value.setValueBoolean((Boolean) typed);
			case DATE -> value.setValueDate((java.time.LocalDate) typed);
		}
	}
} 