package com.miniprojet.orderservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private String status;
    private Double totalAmount;

    @ElementCollection
    private List<OrderItem> products;

    private String userId;
}
