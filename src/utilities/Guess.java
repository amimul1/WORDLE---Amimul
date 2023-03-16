package utilities;
/**
 * @author Tyler Conklin
 * 
 * This class represents a guess. It stores the original guess, the results
 * of the guess for each specific character index, and whether this guess was
 * correct or not.
 */
public class Guess {
	
	private String guess;
	private INDEX_RESULT[] indices;
	private boolean isCorrect;
	/**
	 * Guess constructor.
	 * @param guess A string of the original guess.
	 * @param indices An array describing the correctness of each individual index in the guess.
	 * @param isCorrect A boolean of whether that guess is correct or not.
	 */
	public Guess(String guess, INDEX_RESULT[] indices, boolean isCorrect) {
		if (guess.length() != indices.length) {
			throw new IllegalArgumentException("The length of the guess and its index results must be equal.");
		}
		this.guess = guess;
		this.indices = indices;
		this.isCorrect = isCorrect;
	}
	
	public String getGuess() {
		return this.guess;
	}
	
	public INDEX_RESULT[] getIndices() {
		return this.indices;
	}
	
	public boolean getIsCorrect() {
		return this.isCorrect;
	}
	
}
