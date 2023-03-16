package controller;
import utilities.Guess;
import utilities.INDEX_RESULT;

import java.io.FileNotFoundException;
import java.util.Observer;

import exception.invalidGuessException;
import model.WordleModel;
/**
 * @author Amimul Ehsan Zoha
 * FILE: WordleController.java
 * PURPOSE: This class is the controller class of the MVC Arhitecture.
 * This class is used to query the model based on 
 * the input from the user that comes through the view. It abstracts the
 * view class from the model. The view does not communicate directly with the
 * model but it communicates directly with the controller.
 */


public class WordleController {
	
	private WordleModel model;
	private String guessString;
	private int guessNumber;
	/**
	 * This is a constructor of the controller class.
	 */
	public WordleController () {
		// error handling with try and catch for invalid guesses.
		//Custom exception class has been created
		try {
			this.model = new WordleModel();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		guessString="";
		guessNumber = 0;	
	} 
	/**
	 * This method lets the model,which is the observable of the 
	 * observer-observable design pattern add a observer which will 
	 * observe it. We have two observers, the text ui and the GUI ui
	 * @param viewObj which a observer object like GUI 
	 */
	public void addObserver(Observer viewObj) {
		model.addObserver(viewObj);
	}
	/**
	 * This method returns a boolean representing if the game 
	 * is over or not
	 * @return a boolean value which is true if game is over
	 * and false if the game is not over.
	 */
	public boolean isGameOver() {
		if (guessNumber == 6) { return true; } 
		if(getAnswer().toUpperCase().equals(guessString.toUpperCase())) { 
			return true;
		}
		return false;
	}
	/**
	 * This method returns the answer by calling a method from the model 
	 * class. 
	 * @return a string which is the answer word.
	 */
	public String getAnswer() {
		return model.getAnswer();
	}
	/**
	 * This method is called when the user makes a guess in the game
	 * It calls the makeGuess method of the model and therefore the
	 * inner logic and rule of the game is abstracted away from the 
	 * controller into the model
	 * @param guess a String which is user guess
	 * @return void
	 * @throws invalidGuessException if an invalid guess is made.
	 */
	public void makeGuess(String guess) throws  invalidGuessException{
		guessString = guess;
		model.makeGuess(guessNumber,guess);
		guessNumber +=1;
	}

}
