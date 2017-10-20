package pl.biltec.es.common.contract.exception;

public class FileNotFoundException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String path) {
		super("File '" + path + "' not found.");
	}
}