package model;
/**
 * @author Amimul Ehsan Zoha
 * FILE: WordleModel.java
 * PURPOSE: This class is the model class of the MVC Architecture.
 * This class makes necessary data structures after each guess has been 
 * made. It extends observable class of the observer- obervable design pattern.
 * The text based UI and the GUI based UI are its observers in corresponding 
 * circumstances. It also throws exceptions for any invalid input guesses as determined
 * by the user. It also loads in the dictionary file which is the list of 
 * valid words and selects a random answer.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import exception.invalidGuessException;
import utilities.Guess;
import utilities.INDEX_RESULT;

public class WordleModel extends java.util.Observable{
	//fields and constants
	private static final String FILENAME = "dictionary.txt";
	private INDEX_RESULT[] guessedCharacters;
	private String answer;
	private Guess[] progress;
	private List<String> validWordList;
	private Scanner inDictionaryFile;
	private  Random random_method;
	private static final int WORD_LENGTH = 5;
	private static final int TOTAL_LETTERS = 26;
	private static final int maxGuesses = 6;

	/**
	 * This is the constructor for the wordleModel class 
	 * @throws FileNotFoundException if the dictionary file 
	 * is not found.
	 */
	public WordleModel() throws FileNotFoundException{
		/**
		 * Maintains an array of INDEX_RESULTs for the guessed characters. There
		 * should be 26 indices in this array, one for each character in the English
		 * alphabet. Before a character has been guessed, its position in the array
		 * should hold the value 'null'.
		 */
		guessedCharacters = new INDEX_RESULT[TOTAL_LETTERS];
		progress = new Guess[maxGuesses];
		inDictionaryFile = new Scanner(new File("Dictionary.txt"));
		validWordList = validWordList(inDictionaryFile);
		random_method = new Random();
		answer = selectRandomAnswer(inDictionaryFile);
		answer = answer.toUpperCase();	
	}
	
	/**
	 * This method is called when a guess is made. It throws exceptions
	 * for the invalid user guesses and it is handled in the classes that
	 * calls this method. It calls set changed method and notify the observers
	 * of any changes.
	 * @param guess a String which is the user Guess
	 * @param guessNumber an integer  which is the guess number
	 * @throws invalidGuessException thrown when the input guess is invalid.
	 */
	public void makeGuess(int guessNumber, String guess) throws  invalidGuessException {
		guess = guess.toUpperCase();
		if (!isCorrectLength(guess)) {
			 throw new invalidGuessException("You must make a guess of 5 letter words"); 
		}
		if (!isAlphaa(guess)) { 
			 throw new invalidGuessException("You must make an alphatical guess"); 
		} 
		boolean isValidWord = isValidWord(validWordList, guess); 
		if (!(isValidWord)){ 
			throw new invalidGuessException("You must guess a valid word"); 
		}
		boolean isGuessCorrect = false;
		if (guess.toUpperCase().equals(answer.toUpperCase())){
			isGuessCorrect = true;
		}
		// the guess characters and game display is updated after each guess.
		updateGuessedCharacters(guess);
		INDEX_RESULT[] curGuessIndices = updateIndices(guess, answer);
		Guess curGuess = new Guess(guess, curGuessIndices, isGuessCorrect);
		progress[guessNumber]=curGuess;
		//set changed and all the observers of this observable are
		// notified of the changes which updates the display to the user 
		// based on the changes.
		setChanged();
		notifyObservers();	
	}
	/**
	 * This method returns the random generated answer.
	 * @return a String which is the answer for the game.
	 */
	public String getAnswer() {
		return answer;
	}
	
	
	/**
	 * This method returns the result of the guessed chars.
	 * @return an array of INDEX_RESULT enum representing 
	 * the result of each characters. null if not guessed.
	 */
	public INDEX_RESULT[] getGuessedCharacters() {
		return guessedCharacters;
	}
	
	/**
	 * This method returns the current progress of the game as an 
	 * array of guess objects
	 * @return an array of guess objects representing the current 
	 * progress of the game.
	 */
	public Guess[] getProgress() {
		return progress;	
	}
	/**
	 * This method updates the Indices, as in the the result of each letter
	 * of the guess by comparing with each letter of the answer
	 * @param guess a String which is the user Guess
	 * @param answer a String which is the answer.
	 * @return an array of INDEX_RESULT representing the result of each slot 
	 */
	private INDEX_RESULT[] updateIndices(String guess, String answer) {
		
		guess = guess.toUpperCase();
		INDEX_RESULT[] curGuessIndices = new INDEX_RESULT[WORD_LENGTH];
		for (int i=0; i< WORD_LENGTH; i++) {
			if (answer.contains("" + guess.charAt(i))){
				if (answer.charAt(i) == guess.charAt(i)) {
					curGuessIndices[i] = INDEX_RESULT.CORRECT;
				}
				else {
					curGuessIndices[i] = INDEX_RESULT.CORRECT_WRONG_INDEX;
				}
			}
			else {
				curGuessIndices[i] = INDEX_RESULT.INCORRECT;
			}
		}
		return curGuessIndices;	
	}
	/**
	 * This method updates the guessed characters array after every guess
	 * by comparing the guess with the answer.
	 * @param guess a String which is the user guess.
	 */
	private void updateGuessedCharacters(String guess) {
	
		for (int i = 0; i < WORD_LENGTH; i++) {
			if (answer.contains("" + guess.charAt(i))){
				if ((answer.charAt(i)+ "").equals(guess.charAt(i)+"")) {
					guessedCharacters[guess.charAt(i) - 'A'] = INDEX_RESULT.CORRECT;
				}
				else {
					guessedCharacters[guess.charAt(i) - 'A'] = INDEX_RESULT.CORRECT_WRONG_INDEX;
				}
			}
			else {
				guessedCharacters[guess.charAt(i) - 'A'] = INDEX_RESULT.INCORRECT;	
			}
		}
	}

	/**
	 * This method generates a random answer at random for the game.
	 * @param dictionaryFile the txt file containing the list of 
	 * valid words.
	 * @return String which is the generated random word.
	 */
	private String selectRandomAnswer(Scanner dictionaryFile) {
	    int index = random_method.nextInt(validWordList.size());
	    String randomAnswer = validWordList.get(index);
	    return randomAnswer;
	}
	
	/**
	 * This method returns if the guess is alphabetic 
	 * @param guess a String which is user guess
	 * @return boolean if the guess is alphabetic
	 */
	private static boolean isAlphaa(String guess){
		
        if (guess == null) {
            return false;
        }
        for (int i = 0; i < guess.length(); i++)
        {
            char c = guess.charAt(i);
            if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
                return false;
            }
        }
        return true;
    }
	
	/**
	 * This method returns a boolean based on if the guess is of 
	 * correct length.
	 * @param guess a String which is the userGuess.
	 * @return a boolean representing if the guess is of correct length.
	 */
	private static boolean isCorrectLength(String guess){
		
		if (guess.length() == 5) {
			return true;
		}
		else {
			return false;	
		}
	}
	
	/**
	 * Returns a list of valid words by iterating over the dictionary file
	 * @param inDictionaryFile the text file containing the list of 
	 * valid words.
	 * @return a list of valid words by iterating over the dictionary file
	 */
	private static List<String> validWordList(Scanner inDictionaryFile){
		
		
		List<String> validWordList = new ArrayList<String>();
		//preparing the answers word lists from the "Dictionary.txt" file
	    while (inDictionaryFile.hasNext()) {
	      String word = inDictionaryFile.next().toUpperCase();
	      validWordList.add(word);
	    }
	    return validWordList;
		
	}
	
	
	/**
	 * This method returns if the word is a valid word according to the dictionary
	 * @param validWordList a array of list of the valid words
	 * @param word a String to check if it valid word or not.
	 * @return a boolean that represents if the word is valid or not.
	 */
	private boolean isValidWord(List<String> validWordList, String word) {
		
		for (int i =0; i< validWordList.size(); i++) {
			if (validWordList.get(i).equals(word)) {
				return true;
			}	
		}	
		return false;
	}
}
