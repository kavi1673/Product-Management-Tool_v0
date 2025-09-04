package com.example.producttool.service;

import com.example.producttool.domain.CategoryAttribute;
import com.example.producttool.domain.DataType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Service
public class ValidationService {
	public void validateValue(CategoryAttribute attribute, Object value) {
		if (attribute.isRequired() && value == null) {
			throw new IllegalArgumentException("Attribute '" + attribute.getName() + "' is required");
		}
		if (value == null) return;

		DataType dataType = attribute.getDataType();
		switch (dataType) {
			case STRING -> {
				if (!(value instanceof String)) throw new IllegalArgumentException("Expected STRING for '" + attribute.getName() + "'");
				validateAllowed(attribute, (String) value);
			}
			case NUMBER -> {
				if (!(value instanceof Number)) throw new IllegalArgumentException("Expected NUMBER for '" + attribute.getName() + "'");
			}
			case BOOLEAN -> {
				if (!(value instanceof Boolean)) throw new IllegalArgumentException("Expected BOOLEAN for '" + attribute.getName() + "'");
			}
			case DATE -> {
				if (value instanceof String s) {
					try { LocalDate.parse(s); } catch (DateTimeParseException ex) { throw new IllegalArgumentException("Invalid DATE for '" + attribute.getName() + "'"); }
				} else if (!(value instanceof LocalDate)) {
					throw new IllegalArgumentException("Expected DATE for '" + attribute.getName() + "'");
				}
			}
		}
	}

	private void validateAllowed(CategoryAttribute attribute, String value) {
		String csv = attribute.getAllowedValuesCsv();
		if (csv == null || csv.isBlank()) return;
		List<String> allowed = Arrays.stream(csv.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.toList();
		if (!allowed.isEmpty() && allowed.stream().noneMatch(a -> a.equalsIgnoreCase(value))) {
			throw new IllegalArgumentException("Value '" + value + "' not allowed for attribute '" + attribute.getName() + "'");
		}
	}
} 