package exception;
/**
 * @author Amimul Ehsan Zoha
 * This class is a custom exception class which extends from the 
 * exception class. It has a constructor which lets us print out 
 * messages based on the type of exceptions to show the accurate 
 * information to the user.
 *
 */
public class invalidGuessException extends Exception {
	
	public invalidGuessException(String message){
		super(message);
	}	

}
