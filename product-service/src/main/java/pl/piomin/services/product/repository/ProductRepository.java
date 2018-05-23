package pl.piomin.services.product.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.piomin.services.product.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

	List<Product> findByName(String name);
	
}
