package com.lombok.praticas.estudos.specification.product;

import static com.lombok.praticas.estudos.specification.category.CategoryConverte.toCategoryDto;

public record ProductConverte() {

    public static ProductDto toProductDto(Product product) {
        return new ProductDto(
                product.getIdProduct(),
                product.getName(),
                product.getPrice(),
                toCategoryDto(product.getCategory())
        );
    }
}
