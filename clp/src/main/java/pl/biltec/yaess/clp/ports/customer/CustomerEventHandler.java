package pl.biltec.yaess.clp.ports.customer;

import pl.biltec.yaess.clp.event.CustomerHasDiedEvent;


/**
 * <pre>
 * <b>Purpose:</b>
 * 	Receives events from other contexts and invokes *CommandService in order to perform operation
 * <b>Question:</b>
 * 	Where it should be located? Is it port or is it adapter?
 * </pre>
 *
 */
public class CustomerEventHandler {

	private CustomerCommandService customerCommandService;

	public void handle(CustomerHasDiedEvent event) {

		customerCommandService.delete(event.rootAggregateId().toString());
	}
}
