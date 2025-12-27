package com.miniprojet.orderservice.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class OrderItem {
    private Long productId;
    private Integer quantity;
    private Double price;
}
