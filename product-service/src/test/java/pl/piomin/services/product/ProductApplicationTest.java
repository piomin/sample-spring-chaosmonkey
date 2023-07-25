package pl.piomin.services.product;

import org.springframework.boot.SpringApplication;

public class ProductApplicationTest {

    public static void main(String[] args) {
        SpringApplication.from(ProductApplication::main)
                .with(MysqlContainerDevMode.class)
                .run(args);
    }

}
