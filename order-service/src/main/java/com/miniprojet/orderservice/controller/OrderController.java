package com.miniprojet.orderservice.controller;

import com.miniprojet.orderservice.entity.Order;
import com.miniprojet.orderservice.repository.OrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    public OrderController(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public List<Order> getMyOrders(Principal principal) {
        return orderRepository.findByUserId(principal.getName());
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public Order create(@RequestBody Order order, Principal principal) {
        order.setDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setTotalAmount(order.getProducts().stream().mapToDouble(i -> i.getQuantity() * i.getPrice()).sum());
        order.setUserId(principal.getName());
        // Call Product Service to check stock
        for (var item : order.getProducts()) {
            ResponseEntity<com.miniprojet.orderservice.model.Product> resp = restTemplate.getForEntity("http://product-service:8081/api/products/" + item.getProductId(), com.miniprojet.orderservice.model.Product.class);
            com.miniprojet.orderservice.model.Product product = resp.getBody();
            if (product == null || product.getQuantity() < item.getQuantity()) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Insufficient stock for product " + item.getProductId());
            }
        }
        return orderRepository.save(order);
    }
}
