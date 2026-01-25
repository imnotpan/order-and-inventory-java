package de.imnotpan.orderinventory.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.imnotpan.orderinventory.api.inventory.dto.InventoryResponse;
import de.imnotpan.orderinventory.config.AbstractIntegrationTest;

public class InventoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void createProduct_shouldAlsoCreateInventoryRow() {
        createProduct("SKU-T-1");

        InventoryResponse inventory = restTemplate.getForObject(
            "/api/v1/inventory/SKU-T-1",
            InventoryResponse.class
        );

        assertThat(inventory).isNotNull();
        assertThat(inventory.availableQuantity()).isEqualTo(0);
        assertThat(inventory.reservedQuantity()).isEqualTo(0);
    }

    @Test
    void reserveStock_happyPath_shouldMoveAvailableToReserved() {
        createProduct("SKU-T-2");

        ResponseEntity<Void> restock = restTemplate.postForEntity(
            "/api/v1/inventory/SKU-T-2/restock?quantity=10",
            null,
            Void.class
        );
        assertThat(restock.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Void> reserve = restTemplate.postForEntity(
            "/api/v1/inventory/SKU-T-2/reserve?quantity=6",
            null,
            Void.class
        );
        assertThat(reserve.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        InventoryResponse inventory = restTemplate.getForObject(
            "/api/v1/inventory/SKU-T-2",
            InventoryResponse.class
        );

        assertThat(inventory.availableQuantity()).isEqualTo(4);
        assertThat(inventory.reservedQuantity()).isEqualTo(6);
    }

    @Test
    void reserveStock_insufficient_shouldReturn409_andNotChangeInventory() {
        createProduct("SKU-T-3");

        restTemplate.postForEntity(
            "/api/v1/inventory/SKU-T-3/restock?quantity=5",
            null,
            Void.class
        );

        InventoryResponse before = restTemplate.getForObject(
            "/api/v1/inventory/SKU-T-3",
            InventoryResponse.class
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "/api/v1/inventory/SKU-T-3/reserve?quantity=10",
            null,
            Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("code")).isEqualTo("INSUFFICIENT_STOCK");

        InventoryResponse after = restTemplate.getForObject(
            "/api/v1/inventory/SKU-T-3",
            InventoryResponse.class
        );

        assertThat(after.availableQuantity())
            .isEqualTo(before.availableQuantity());
        assertThat(after.reservedQuantity())
            .isEqualTo(before.reservedQuantity());
    }

    private void createProduct(String sku) {
        Map<String, Object> request = Map.of(
            "sku", sku,
            "name", "Test Product",
            "price", 10.0
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "/api/v1/products",
            request,
            Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("id")).isNotNull();
    }
}
