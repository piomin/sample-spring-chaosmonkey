package pl.piomin.services.gateway;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;

import pl.piomin.services.gateway.model.Customer;
import pl.piomin.services.gateway.model.CustomerType;
import pl.piomin.services.gateway.model.Product;
import pl.piomin.services.gateway.model.ProductCategory;

public class AddTestData {

	private static Logger LOGGER = LoggerFactory.getLogger(AddTestData.class);
	
	TestRestTemplate rest = new TestRestTemplate(); 
	
	@Test
	public void addTestCustomers() {
		for (int i = 0; i < 10; i++) {
			Customer c = new Customer();
			c.setAvailableFunds(50000);
			c.setName("Test Test" + i);
			c.setType(CustomerType.NORMAL);
			c = rest.postForObject("http://localhost:8090/customer-service/customers", c, Customer.class);
			LOGGER.info("New customer: id={}", c.getId());
		}
	}
	
//	@Test
	public void addTestProducts() {
		for (int i = 0; i < 10; i++) {
			Product p = new Product();
			p.setName("Test" + i);
			p.setCategory(ProductCategory.FOOD);
			p.setCount(10000);
			p.setPrice(500);
			p = rest.postForObject("http://localhost:8090/product-service/products", p, Product.class);
			LOGGER.info("New product: id={}", p.getId());
		}
	}
	
}
