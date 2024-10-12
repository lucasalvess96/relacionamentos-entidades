package com.lombok.praticas.estudos.specification.product;

import com.lombok.praticas.estudos.specification.category.Category;
import com.lombok.praticas.estudos.specification.category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.lombok.praticas.estudos.specification.product.ProductSpecification.*;

@Service
public class ProductService {

    ProductRepository productRepository;

    CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDto saveProduct(ProductDto productDto) {
        Product product = toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductConverte.toProductDto(savedProduct);
    }

    private Product toProduct(ProductDto productDto) {
        Category category = new Category();
        category.setNameCategory(productDto.name());
        categoryRepository.save(category);
        Product product = new Product();
        product.setName(productDto.name());
        product.setPrice(productDto.price());
        product.setCategory(category);
        return product;
    }

    public Page<ProductDto> getAllProducts(ProductFilter productFilter, Pageable pageable) {
        Specification<Product> specification = Specification.where(getProduct(productFilter))
                .and(getNameProduct(productFilter))
                .and(getPriceProduct(productFilter));
        return productRepository.findAll(specification, pageable)
                .map(ProductConverte::toProductDto);
    }
}
