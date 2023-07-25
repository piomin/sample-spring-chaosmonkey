package pl.piomin.services.order.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.services.order.model.Order;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    List<Order> findByCustomerId(Integer customerId);

}
