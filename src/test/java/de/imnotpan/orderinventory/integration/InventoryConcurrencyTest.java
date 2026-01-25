package de.imnotpan.orderinventory.integration;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;


import de.imnotpan.orderinventory.config.AbstractIntegrationTest;

public class InventoryConcurrencyTest extends AbstractIntegrationTest{

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldPreventOverReservation() throws InterruptedException {

        // Create initial product
        restTemplate.postForEntity(
            "/api/v1/products",
            Map.of(
                "sku", "SKU-1",
                "name", "Test Product",
                "price", 10.0
            ),
            Map.class
        );

        // Initial Restock
        restTemplate.postForEntity("/api/v1/inventory/SKU-1/restock?quantity=10", 
            null, 
            Void.class);

        int threads = 2;
        CountDownLatch latch = new CountDownLatch(threads);
        var executor = Executors.newFixedThreadPool(threads);

        for (int i=0; i<threads; i++){
            executor.submit(() -> {
                try{
                    restTemplate.postForEntity(
                        "/api/v1/inventory/SKU-1/reserve?quantity=10", 
                        null, 
                        Void.class
                    );
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        var response = restTemplate.getForEntity(
            "/api/v1/inventory/SKU-1",
            Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((Integer) response.getBody().get("availableQuantity"))
            .isGreaterThanOrEqualTo(0);
    }
}