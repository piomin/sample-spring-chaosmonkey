package pl.piomin.services.customer;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class MysqlContainerDevMode {

    @Bean
    @ServiceConnection
    public MySQLContainer<?> mysql() {
        return new MySQLContainer<>("mysql:8.0")
                .withUsername("chaos")
                .withPassword("chaos123");
    }

}
