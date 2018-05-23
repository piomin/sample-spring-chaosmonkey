package pl.piomin.services.customer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.piomin.services.customer.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	List<Customer> findByName(String name);
	
}
