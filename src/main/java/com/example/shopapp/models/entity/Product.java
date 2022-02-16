package com.example.shopapp.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
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
