package pl.piomin.services.product.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.services.product.model.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    List<Product> findByName(String name);

}
