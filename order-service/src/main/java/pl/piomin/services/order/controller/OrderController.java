package pl.piomin.services.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.order.model.Order;
import pl.piomin.services.order.repository.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	OrderRepository repository;
	
	@PostMapping
	public Order add(@RequestBody Order order) {
		return repository.save(order);
	}
	
	@GetMapping("/{id}")
	public Order findById(@PathVariable("id") Integer id) {
		return repository.findById(id).get();
	}
	
	@GetMapping("/customer/{customerId}")
	public List<Order> history(@PathVariable("customerId") Integer customerId) {
		return repository.findByCustomerId(customerId);
	}
	
}
