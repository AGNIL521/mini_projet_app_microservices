package com.miniprojet.orderservice.model;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
