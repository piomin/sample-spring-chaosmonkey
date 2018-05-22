package pl.piomin.services.order.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.order.client.CustomerClient;
import pl.piomin.services.order.client.ProductClient;
import pl.piomin.services.order.model.Customer;
import pl.piomin.services.order.model.Order;
import pl.piomin.services.order.model.OrderStatus;
import pl.piomin.services.order.model.Product;
import pl.piomin.services.order.repository.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	OrderRepository repository;
	@Autowired
	CustomerClient customerClient;
	@Autowired
	ProductClient productClient;
	
	@PostMapping
	public Order add(@RequestBody Order order) {
		Product product = productClient.findById(order.getProductId());
		Customer customer = customerClient.findById(order.getCustomerId());
		int totalPrice = order.getProductsCount() * product.getPrice();
		if (customer != null && customer.getAvailableFunds() >= totalPrice && product.getCount() >= order.getProductsCount()) {
			order.setPrice(totalPrice);
			order.setStatus(OrderStatus.ACCEPTED);
			product.setCount(product.getCount() - order.getProductsCount());
			productClient.update(product);
			customer.setAvailableFunds(customer.getAvailableFunds() - totalPrice);
			customerClient.update(customer);
		} else {
			order.setStatus(OrderStatus.REJECTED);
		}
		return repository.save(order);
	}
	
	@GetMapping("/{id}")
	public Order findById(@PathVariable("id") Integer id) {
		Optional<Order> order = repository.findById(id);
		if (order.isPresent()) {
			Order o = order.get();
			Product product = productClient.findById(o.getProductId());
			o.setProductName(product.getName());
			Customer customer = customerClient.findById(o.getCustomerId());
			o.setCustomerName(customer.getName());
			return o;
		} else {
			return null;
		}
	}
	
	@GetMapping("/customer/{customerId}")
	public List<Order> history(@PathVariable("customerId") Integer customerId) {
		return repository.findByCustomerId(customerId);
	}
	
}
