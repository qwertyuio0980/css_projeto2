package facade.exceptions;


/**
 * 
 * @author css020
 *
 */
public class ApplicationException extends Exception {
	
	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -3416001628323171383L;

	
	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public ApplicationException(String message) {
		super (message);
	}
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * Do not use for lower exception types that might not be 
	 * available in the receiver context.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public ApplicationException(String message, Exception e) {
		super (message, e);
	}

}
