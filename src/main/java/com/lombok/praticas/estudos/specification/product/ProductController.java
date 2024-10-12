package com.lombok.praticas.estudos.specification.product;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductDto productDto) {
        ProductDto createDto = productService.saveProduct(productDto);
        return ResponseEntity.created(URI.create("/products/save/" + createDto.idProduct())).body(createDto);
    }

    @GetMapping("/AllPageable")
    public ResponseEntity<Page<ProductDto>> getAllProducsPgeable(ProductFilter productFilter, @PageableDefault(sort =
            "name") Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(productFilter, pageable));
    }
}
