package pl.piomin.services.product;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.product.model.Product;
import pl.piomin.services.product.model.ProductCategory;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTests {

    protected Logger logger = Logger.getLogger(ProductControllerTests.class.getName());
    private static Integer id;

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withUsername("chaos")
            .withPassword("chaos123");

    @Test
    @Order(1)
    void addProduct() {
        Product p = new Product();
        p.setName("Test");
        p.setCount(10);
        p.setPrice(100);
        p.setCategory(ProductCategory.FOOD);
        p = restTemplate.postForObject("/products", p, Product.class);
        assertNotNull(p);
        assertNotNull(p.getId());
        id = p.getId();
    }

    @Test
    @Order(2)
    void findProductById() {
        Product p = restTemplate.getForObject("/products/{id}", Product.class, id);
        assertNotNull(p);
        assertNotNull(p.getId());
    }
}
