package de.imnotpan.orderinventory.integration;

import de.imnotpan.orderinventory.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class ProductIntegrationTest extends AbstractIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateProduct(){
        Map<String, Object> request = Map.of(
            "sku", "SKU-INT-1",
            "name", "Integration Keyboard",
            "price", 120.00
        );

        ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            "/api/v1/products", 
                            request, 
                            Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("sku")).isEqualTo("SKU-INT-1");
    }


    @Test 
    void shouldListProducts() {
        ResponseEntity<Map> response = 
                restTemplate.getForEntity("/api/v1/products", Map.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).containsKey("content");

            
    }
}