package pl.biltec.yaess.loyalty.application.customer;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.biltec.yaess.clp.domain.customer.CustomerEventStore;
import pl.biltec.yaess.clp.domain.customer.CustomerEventsStream;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.ports.customer.CustomerApplicationService;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.CustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.exception.UnsupportedCommandException;


public class CustomerApplicationServiceTest {

	private CustomerApplicationService customerApplicationService;

	@Before
	public void setUp() throws Exception {
		//given
		CustomerEventStore customerEventStore = Mockito.mock(CustomerEventStore.class);
		Mockito.when(customerEventStore.loadEvents(Mockito.any(CustomerId.class))).thenReturn(new CustomerEventsStream(1, Collections.emptyList()));
		customerApplicationService = new CustomerApplicationService(customerEventStore);
	}


	@Test
	public void shouldThrowExceptionForNotSupportedCommandCommand() throws Exception {
		//when
		try {
			customerApplicationService.execute(newUndefinedCustomerCommand());
			Fail.fail("Exception expected");
		}
		catch (Exception e) {
			//then
			Assertions.assertThat(e).isInstanceOf(UnsupportedCommandException.class);
		}
	}

	private CustomerCommand newUndefinedCustomerCommand() {

		return new CustomerCommand() {

			@Override
			public CustomerId getId() {

				return null;
			}

			@Override
			public String toString() {

				return null;
			}
		};
	}

	@Test
	public void shouldNotThrowExceptionForSupportedCustomerCommand() throws Exception {
		customerApplicationService.execute(new CreateCustomerCommand("test", new CustomerId()));
		//then no exception thrown
	}

}