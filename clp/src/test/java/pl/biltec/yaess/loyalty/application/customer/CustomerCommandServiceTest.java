package pl.biltec.yaess.loyalty.application.customer;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.biltec.yaess.clp.adapters.store.CustomerEventStoreWrapperRepository;
import pl.biltec.yaess.clp.ports.customer.CustomerCommandService;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.CustomerCommand;
import pl.biltec.yaess.clp.ports.customer.exception.UnsupportedCommandException;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerCommandServiceTest {

	private CustomerCommandService customerCommandService;

	@Before
	public void setUp() throws Exception {
		//given
		EventStore eventStore = Mockito.mock(EventStore.class);
		Mockito.when(eventStore.loadEvents(Mockito.any(RootAggregateId.class))).thenReturn(Collections.emptyList());
		customerCommandService = new CustomerCommandService(new CustomerEventStoreWrapperRepository(eventStore));
	}


	@Test
	public void shouldThrowExceptionForNotSupportedCommandCommand() throws Exception {
		//when
		try {
			customerCommandService.execute(newUndefinedCustomerCommand());
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
			public RootAggregateId getId() {

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
		customerCommandService.execute(new CreateCustomerCommand("test", new RootAggregateId()));
		//then no exception thrown
	}

}