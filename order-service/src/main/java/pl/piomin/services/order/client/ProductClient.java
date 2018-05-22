package pl.piomin.services.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pl.piomin.services.order.model.Product;

@FeignClient(name = "product-service")
public interface ProductClient {

	@PutMapping("/products")
	Product update(@RequestBody Product product);
	
	@GetMapping("/products/{id}")
	Product findById(@PathVariable("id") Integer id);
	
}
