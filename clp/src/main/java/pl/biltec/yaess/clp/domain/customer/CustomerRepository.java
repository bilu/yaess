package pl.biltec.yaess.clp.domain.customer;

public interface CustomerRepository {

	Customer getById(CustomerId id);

	void save(Customer customer);

}
