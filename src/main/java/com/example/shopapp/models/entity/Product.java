package com.example.shopapp.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private String color;
    @Enumerated(EnumType.STRING)
    private TypeOfProduct type;

    public enum TypeOfProduct {
        CLOTHES,
        ACCESSORIES,
        SHOES,
        SUPPLEMENTS,
        FOOD
    }
}
