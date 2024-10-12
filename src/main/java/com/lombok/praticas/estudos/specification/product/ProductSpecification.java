package com.lombok.praticas.estudos.specification.product;

import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> getProduct(ProductFilter productFilter) {
        return (root, query, criteriaBuilder) -> Objects.nonNull(productFilter.getIdProduct()) ?
                criteriaBuilder.equal(root.get("idProduct"), productFilter.getIdProduct()) :
                criteriaBuilder.conjunction();
    }

    public static Specification<Product> getNameProduct(ProductFilter productFilter) {
        return (root, query, criteriaBuilder) -> Objects.nonNull(productFilter.getName()) ?
                criteriaBuilder.equal(root.get("name"), productFilter.getName()) :
                criteriaBuilder.conjunction();
    }

    public static Specification<Product> getPriceProduct(ProductFilter productFilter) {
        return (root, query, criteriaBuilder) -> Objects.nonNull(productFilter.getPrice()) ?
                criteriaBuilder.equal(root.get("price"), productFilter.getPrice()) :
                criteriaBuilder.conjunction();
    }
}
