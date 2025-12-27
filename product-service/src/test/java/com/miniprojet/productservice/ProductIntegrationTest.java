package com.miniprojet.productservice;

import com.miniprojet.productservice.entity.Product;
import com.miniprojet.productservice.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        productRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void testCreateAndGetProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);
        product.setQuantity(10);

        webTestClient.post()
                .uri("/api/products")
                .bodyValue(product)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Test Product")
                .jsonPath("$.price").isEqualTo(100.0);

        webTestClient.get()
                .uri("/api/products")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Test Product");
    }

    @Test
    @SuppressWarnings("null")
    void testGetProductById() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);
        product.setQuantity(10);

        Product saved = productRepository.save(product);

        webTestClient.get()
                .uri("/api/products/" + saved.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.getId())
                .jsonPath("$.name").isEqualTo("Test Product");
    }

    @Test
    void testGetNonExistentProduct() {
        webTestClient.get()
                .uri("/api/products/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}
