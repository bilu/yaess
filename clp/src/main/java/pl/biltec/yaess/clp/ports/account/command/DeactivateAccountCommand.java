package pl.biltec.yaess.clp.ports.account.command;

import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.ports.Command;


public class DeactivateAccountCommand extends Command {

	public DeactivateAccountCommand(String accountId, String originator) {
		super(originator, accountId);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("originator", originator)
			.append("rootAggregateId", rootAggregateId)
			.toString();
	}
}
