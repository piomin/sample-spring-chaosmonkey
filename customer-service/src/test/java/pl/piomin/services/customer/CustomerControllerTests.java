package pl.piomin.services.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.customer.model.Customer;
import pl.piomin.services.customer.model.CustomerType;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CustomerControllerTests {

    protected Logger logger = Logger.getLogger(CustomerControllerTests.class.getName());
    private static Integer id;

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withUsername("chaos")
            .withPassword("chaos123");

    @Test
    void addCustomer() {
        Customer c = new Customer();
        c.setName("Test");
        c.setType(CustomerType.NORMAL);
        c.setAvailableFunds(10000);
        c = restTemplate.postForObject("/customers", c, Customer.class);
        assertNotNull(c);
        assertNotNull(c.getId());
        id = c.getId();
    }

    @Test
    void findCustomerById() {
        Customer c = restTemplate.getForObject("/customers/{id}", Customer.class, id);
        assertNotNull(c);
        assertNotNull(c.getId());
    }
}
