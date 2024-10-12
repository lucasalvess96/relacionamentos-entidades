package com.lombok.praticas.estudos.specification.category;

public record CategoryConverte() {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getIdCategory(),
                category.getNameCategory()
        );
    }
}
