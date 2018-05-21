package pl.piomin.services.customer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {

	@Id
	private Integer id;
	private String name;
	private int availableFunds;
	private CustomerType type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAvailableFunds() {
		return availableFunds;
	}

	public void setAvailableFunds(int availableFunds) {
		this.availableFunds = availableFunds;
	}

	public CustomerType getType() {
		return type;
	}

	public void setType(CustomerType type) {
		this.type = type;
	}

}
