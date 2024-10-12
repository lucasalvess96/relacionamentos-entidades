package com.lombok.praticas.estudos.specification.product;

import com.lombok.praticas.estudos.specification.category.CategoryDto;

public record ProductDto(Long idProduct, String name, Double price, CategoryDto categoryDto) {
}
