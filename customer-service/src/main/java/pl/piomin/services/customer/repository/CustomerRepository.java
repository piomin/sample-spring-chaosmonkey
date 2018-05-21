package pl.piomin.services.customer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.piomin.services.customer.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
