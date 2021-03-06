package pl.biltec.yaess.core.common.exception;

public class FileNotFoundException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String path) {
		super("File '" + path + "' not found.");
	}
}