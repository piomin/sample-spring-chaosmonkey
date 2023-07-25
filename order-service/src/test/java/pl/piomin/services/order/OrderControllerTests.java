package pl.piomin.services.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.order.model.Order;
import pl.piomin.services.order.model.OrderStatus;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class OrderControllerTests {

    protected Logger logger = Logger.getLogger(OrderControllerTests.class.getName());
    private static Integer id;

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withUsername("chaos")
            .withPassword("chaos123");

    @Test
    void addOrder() {
        Order o = new Order();
        o.setCustomerId(1);
        o.setCustomerName("Test");
        o.setPrice(1000);
        o.setStatus(OrderStatus.ACCEPTED);
        o.setProductId(1);
        o.setProductName("Test");
        o.setProductsCount(10);
        o = restTemplate.postForObject("/orders", o, Order.class);
        assertNotNull(o);
        assertNotNull(o.getId());
        id = o.getId();
    }

    @Test
    void findOrderById() {
        Order o = restTemplate.getForObject("/orders/{id}", Order.class, id);
        assertNotNull(o);
        assertNotNull(o.getId());
    }
}
