package pl.piomin.services.customer.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.services.customer.model.Customer;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    List<Customer> findByName(String name);

}
