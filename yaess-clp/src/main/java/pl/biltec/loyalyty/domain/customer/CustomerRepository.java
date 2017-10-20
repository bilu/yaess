package pl.biltec.loyalyty.domain.customer;

public interface CustomerRepository {

	Customer getById(CustomerId id);

	void save(Customer customer);

}
