package pl.biltec.yaess.clp.ports.customer;

public interface AuthorizationService {


	boolean isAllowedToInvokeCommand(Command command);


}
