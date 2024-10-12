package com.lombok.praticas.estudos.specification.product;

import com.lombok.praticas.estudos.specification.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString(of = "idProduct")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;

    private String name;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getIdProduct(), product.getIdProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdProduct());
    }
}
