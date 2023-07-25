package pl.piomin.services.order;

import org.springframework.boot.SpringApplication;

public class OrderApplicationTest {

    public static void main(String[] args) {
        SpringApplication.from(OrderApplication::main)
                .with(MysqlContainerDevMode.class)
                .run(args);
    }

}
