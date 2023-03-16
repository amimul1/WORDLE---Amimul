/**
 * @author Amimul Ehsan Zoha
 * FILE: WordleTextView.java
 * 
 * PURPOSE: This class implements the text based Ui version opf 
 * the popular game Wordle by making use of the MVC
 * architecture. There are exceptions,
 * creating own custom exception, taking only valid word as guesses as set by
 * the dictionary.txt file and better abstraction.
 * MVC architecture is the model-view- controller 
 * architecture when we have something to model (in this case the 
 * wordle game) and user interface that displays and interacts with that
 * model (the view). The controller is the code that manipulates the model
 * in response to actions from the view. 
 * This class below is the view part (observer) of 
 * MVC and this displays the game actions and progress to the user in text
 * form. 
 */

//imports
package view;
import java.util.Observable;
import controller.WordleController;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;
import exception.invalidGuessException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
@SuppressWarnings("deprecation")

//implements the observer interface where we update the view after 
//every change in the game.

public class WordleTextView implements java.util.Observer {
	private WordleController controller;
	private String userGuess;
	private static final int MAX_GUESSES = 6;
	private static final int WORD_LENGTH = 5;

	/**
	 * The constructor runs the main logic of the game by calling methods from 
	 * the controller and catches exceptions caused by wrong inputs.
	 */
	public WordleTextView() {
		Scanner userInputObj = new Scanner(System.in);
		String decision = "yes";
		//loop used in case the user wants to play again
		while (playAgain(decision)) {
			controller = new WordleController();
			controller.addObserver(this);
			// this loop runs until a single game is over
			while(!controller.isGameOver()) {
				System.out.println("Enter a guess: ");
				userGuess = userInputObj.nextLine();
				try {
					controller.makeGuess(userGuess);
				}
				catch(invalidGuessException e){
					System.out.println(e);
					continue;
				}
				if (controller.isGameOver()) {
					System.out.println("Good game! The word was " + controller.getAnswer());
					System.out.println("Would you like to play again? yes/no");
					decision = userInputObj.nextLine();
					playAgain (decision);		
				}
			}	
		}	
	}
	/**
	 * This method This method is called whenever the observed object is changed.
	 * It is overridden in this view. It helps us get the data from the data structures from 
	 * the model and we can update the view according to that.
	 * @param Obsevable o - the observable (model) object
	 */
	@Override
	//this method implements the observer
	public void update(Observable o, Object arg) {
		WordleModel model = (WordleModel) o;
		INDEX_RESULT[] guessedCharacters = model.getGuessedCharacters();
		Guess[] progresss = model.getProgress();
		//System.out.println(printGuessedCharUpdate(guessedCharacters));
		//System.out.println(controller.getAnswer());
		System.out.println(printGameUpdate(progresss, userGuess));
		System.out.println(printGuessedCharUpdate(guessedCharacters));
	}
	/**
	 * This method prints the game update after the guess is made
	 * It has been made public for Junit testing coverage which can be changed 
	 * to private for the practical world.
	 * @param progress an array of guess objects after each guess
	 * @param guessString a String which is the guess made by user.
	 * @return a String which is the representation of the game update.
	 */	
	public static String printGameUpdate(Guess[] progress, String guessString) {
		String updateString="";
		for(int i = 0; i < MAX_GUESSES; i++ ) {
			if (progress[i]!= null) {
				for (int j = 0; j < WORD_LENGTH; j++) {
					if (progress[i].getIndices()[j].getDescription().equals("Correct")) {
						updateString+= (""+ progress[i].getGuess().charAt(j)).toUpperCase();	
					}
					else if (progress[i].getIndices()[j].getDescription().equals("Correct letter, wrong index")) {
						updateString+= (""+ progress[i].getGuess().charAt(j)).toLowerCase();	
					}
					else if (progress[i].getIndices()[j].getDescription().equals("Incorrect")){
						updateString += "_";	
					}
				}
			}
			else {
				updateString+= "_____";		
			}
			updateString += "\n";
		}
		return updateString;
	}
	/**
	 * This method prints the guessed characters update after the guess is made
	 * It has been made public for Junit testing coverage which can be changed 
	 * to private for the practical world.
	 * @param guessedCharacters an array of Index Result objects which allows us
	 * to see if a specific letter for a guess is correct, incorrect of correct 
	 * letter but at wrong index
	 * @return a String which is the representation of the guessed character update.
	 */
	public static String printGuessedCharUpdate(INDEX_RESULT[] guessedCharacters){
		String outputString = "";
		ArrayList<String> unguessedChars = new ArrayList<String>();
		ArrayList<String> correctChars = new ArrayList<String>();
		ArrayList<String> incorrectChars = new ArrayList<String>();
		ArrayList<String> correctWrongIndexChars = new ArrayList<String>();
		for (int i = 0; i < 26; i++) {
			if (guessedCharacters[i]!= null) {
				if (guessedCharacters[i].getDescription().equals("Correct")) {
					correctChars.add(Character.toString((char)(i+65)));
				}
				else if (guessedCharacters[i].getDescription().equals("Correct letter, wrong index")) {
					correctWrongIndexChars.add(Character.toString((char)(i+65)));
				}
				else {
					incorrectChars.add(Character.toString((char)(i+65)));
				}	
			}
			else {
				unguessedChars.add(Character.toString((char)(i+65)));
			}
		}
		outputString += ("Unguessed [ ");
		for (String charr: unguessedChars) {
			outputString += (charr + " ");
		}
		outputString += ("]") + "\n";
		
		outputString += ("Incorrect [ ");
		for (String charr:incorrectChars ) {
			outputString += (charr + " ");
		}
		outputString += ("]") + "\n";
		
		outputString += ("Correct [ ");
		for (String charr:correctChars ) {
			outputString += (charr + " ");
		}
		outputString += ("]") + "\n";
		
		outputString += ("Correct letter, wrong index [ ");
		for (String charr: correctWrongIndexChars ) {
			outputString += (charr + " ");
		}
		outputString += ("]") +"\n";
		return outputString;
	}
	/**
	 * This method returns true or false based on if user wishes to 
	 * play a game again.
	 * @param decision
	 * @return a boolean representing if the user wants to play a new game.
	 */
	private static boolean playAgain (String decision) {
		if(decision.toLowerCase().equals("yes")){
			return true;
		}
		if (decision.toLowerCase().equals("no")) {
			return false;
		}
		return false;
	}

}
