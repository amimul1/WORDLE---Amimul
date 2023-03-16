package view;
//imports
/**
 * @author Amimul Ehsan Zoha

 * FILE: WordleGUIView.java
 * 
 * PURPOSE: This program implements the GUI front end based graphical version of wordle in accordance with
 * the MVC  architecture. Both the guesses and the guessed characters are Label objects placed inside GridPane objects. 
 * These two separate GridPane objects are placed inside of a VBox which is then placed inside of the Scene.
 * It this displays the game actions after every update has been  made.
 * Animation has been added after the user has ended the game using fade on and fade off transition.
 * Alerts are shown for events like invalid guess and game being over. Event handling has been done through 
 * Lambda functions.
 * 
 */
//imports 
import java.util.Observable;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Observable;
import controller.WordleController;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;
import exception.invalidGuessException;
import java.io.FileNotFoundException;



//extends application of java fx
/**
 * This class of the GUI view class. This class implements the 
 * interface Observer and therefore we need to implement the update method 
 * for updating the display graphics after every move.
 */
public class WordleGUIView extends Application implements java.util.Observer  {
	private Label[][] progressArray= new Label[6][5];
	private Label[] characterArray = new Label[26];
	GridPane progressGrid = new GridPane();
	GridPane letterGrid = new GridPane();
	VBox vbox = new VBox(progressGrid,letterGrid);
	private WordleController controller;
	private int posX =0;
	private int posY = 0;
	
	private String curGuessString = "";
	private int guessNumber = 0;
	/* Constants for the scene */
	private static final int SCENE_SIZE = 800;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 10;

	/* Constants for letters in grid */
	private static final int LETTER_FONT_SIZE = 75;
	private static final int SMALL_FONT_SIZE = 20;
	private static final int SMALL_LETTER_SQUARE_SIZE = 50;
	private static final int LETTER_SQUARE_SIZE = 90;
	private static final int LETTER_BORDER_WIDTH = 2;
	private static final String FONT_NAME = "TIMES NEW ROMAN";
	private static final String TITLE = "WORDLE";
	private static javafx.scene.layout.Border BORDER = new javafx.scene.layout.Border(
            new BorderStroke(
            Color.FIREBRICK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
	
	//constants for background
	private static final Background INCORRECT_INDEX = new Background(new BackgroundFill(
            Color.color(1, 1, 0, 1d), new CornerRadii(0), null)); //yellow
	
	private static final Background CORRECT = new Background(new BackgroundFill(
            Color.color(0, 1, 0, 1d), new CornerRadii(0), null)); //green
	
	private static final Background INCORRECT = new Background(new BackgroundFill(
            Color.color(0.7, 0.7, 0.7, 1d), new CornerRadii(0), null)); //grey
	
	private static final Background EMPTY = new Background(new BackgroundFill(
            Color.GREY, new CornerRadii(0), null)); //white
	private static final Color TEXT_COLOR = Color.web("Black",1);
	private static final int MAX_GUESSES = 6;
	private static final int WORD_LENGTH = 5;
	
	/**
	 * This method basically starts showing the graphics on the window. I screated the 
	 * scene, set the title, had two gridpanes in vbox and the labels inside of my gridpanes
	 * I do the alignment and padding to be similar to actual wordle and set the border and 
	 * gap for every label objects.
	 * @param Stage stage which will hold the other gui objects
	 * 
	 */
	public void start(Stage stage) throws Exception {
		stage.setTitle(TITLE);
		progressGrid.setHgap(GRID_GAP);
		progressGrid.setVgap(GRID_GAP);
		letterGrid.setHgap(GRID_GAP);
		letterGrid.setVgap(GRID_GAP);
		Scene scene = new Scene(vbox, SCENE_SIZE, SCENE_SIZE);
		progressGrid.setAlignment(Pos.TOP_CENTER);
		letterGrid.setAlignment(Pos.BOTTOM_CENTER);
		progressGrid.setPadding(new Insets(25,25,25,25));
		letterGrid.setPadding(new Insets(25,25,25,25));
		controller = new WordleController();
		controller.addObserver(this);
		//lambda function used for event handling
		scene.setOnKeyReleased((KeyEvent ke) -> handleKey(ke));
		drawEmptyProgressArray();
		drawEmptyCharacterArray();
		stage.setScene(scene);
	    stage.show();   
	}
	
	/**
	 * This method updates the window after an event of the game
	 * so that the latest view is displayed. It gets the data of what to update
	 * from the data structures of the model which holds the data.
	 * Then at every update, I updated the graphics display to match the
	 * situation of the game by accessing my label objects kept in arrays.
	 * @param Obsevable o - the observable (model) object
	 */
	@Override
	public void update(Observable o, Object arg) {
		WordleModel model = (WordleModel) o;
		INDEX_RESULT[] guessedCharacters = model.getGuessedCharacters();
		Guess[] progresss = model.getProgress();
		updateProgressArray(progresss);
		updateCharacterArray(guessedCharacters);
		drawProgressArray();
		drawCharacterArray();								
	}
	/**
	 * This method updates the array of characters by their index results 
	 * according to the guess made by the user 
	 * @param guessedCharacters the array of characters by their index results 
	 */
	private void updateCharacterArray(INDEX_RESULT[] guessedCharacters) {
		for (int i = 0; i < 26; i++) {
			if (guessedCharacters[i]!= null) {
				if (guessedCharacters[i].getDescription().equals("Correct")) {
					Label labelObj = labelCreatorForLetterGrid("correct", Character.toString((char)(i+65)));
					characterArray[i]= labelObj;
				}
				else if (guessedCharacters[i].getDescription().equals("Correct letter, wrong index")) {
					Label labelObj = labelCreatorForLetterGrid("incorrectIndex", Character.toString((char)(i+65)));
					characterArray[i]= labelObj;
				}
				else{
					Label labelObj = labelCreatorForLetterGrid("incorrect", Character.toString((char)(i+65)));
					characterArray[i]= labelObj;
				}
			}
			else {
				Label labelObj = labelCreatorForLetterGrid("unguessed", Character.toString((char)(i+65)));
				characterArray[i]= labelObj;
			}
		}
	}
	
	/**
	 * This method is the event handler which is the used in case of event handling 
	 * requirements. It changes the game according to user input in the keyboard like
	 * the guess of different letters, stops invalid input like numbers, checks for making 
	 * new guesses and shows alerts in case of bad guess.
	 * It also shows animation at the end of the game signifying the end 
	 * @param ke a keyevent obj which is basically the key of the keyboard
	 */
	public void handleKey(KeyEvent ke) {
			if (!controller.isGameOver()) {
	        if (ke.getCode().equals(KeyCode.DELETE) || ke.getCode().equals(KeyCode.BACK_SPACE)) {
	            if (posX>0){
	            	posX-=1;
	            	curGuessString = curGuessString.substring(0,posX);	                
	                Label labelObj = labelCreator("backspace", " ");
	                progressGrid.add(labelObj, posX, posY);	
	            }
	        }	        
	        else if(ke.getCode().equals(KeyCode.ENTER) && (posX==5)) {	        	
	        	try {
					controller.makeGuess(curGuessString);
					guessNumber+=1;
					posY += 1;
					curGuessString = "";
					posX=0;
				} catch (invalidGuessException e) {
					Alert alert = new Alert(AlertType.ERROR);
	    			alert.setHeaderText(e.getMessage() + "! \nClick OK to continue");
	    			alert.setTitle("Input Valid Guess! ");
	    			alert.showAndWait();
					System.out.println(e.getMessage());
				}
	        	if (controller.isGameOver()) {
	        		FadeTransition fadeTrans = new FadeTransition(Duration.seconds(1), vbox);
	    			fadeTrans.setFromValue(2.0);
	    			fadeTrans.setToValue(.30);	   
	    			fadeTrans.setCycleCount(8);	    	       
	    			fadeTrans.setAutoReverse(true);
	    	        // Play the Animation
	    			fadeTrans.play();
	        		Alert alert = new Alert(AlertType.INFORMATION);
	    			alert.setHeaderText("Good Game! The word was " + controller.getAnswer());
	    			alert.setTitle("Game Over");
	    			alert.showAndWait();
	    			FadeTransition fadeTrans2 = new FadeTransition(Duration.seconds(1), vbox);
	    			fadeTrans2.setFromValue(2.0);
	    			fadeTrans2.setToValue(.30);
	    	        // Let the animation run forever
	    			fadeTrans2.setCycleCount(FadeTransition.INDEFINITE);
	    	        // Reverse direction on alternating cycles
	    			fadeTrans2.setAutoReverse(true);
	    	        // Play the Animation
	    			fadeTrans2.play();
	        	}
	        }
	        else {
	        	String inputForTest = ke.getCode().getName();
	        	char c = inputForTest.charAt(0);
	        	int asciNum = (int) c;
	        	if(posX<5 && posY<6 && (( asciNum>= 65 && asciNum <= 90) || (asciNum>= 97 && asciNum <= 122)))  {
	        		String input = ke.getCode().getName();
	            	Label labelObj = labelCreator("", input);
	            	progressGrid.add(labelObj, posX, posY);
	            	curGuessString+=input;
	            	posX+=1;
	        	}
	        }
			}
	       
	}
	
	
	/**
	 * this method updates the progress array according to data and creates
	 * corresponding label objects for showing it to the user in the graphical view
	 * @param progress a an array of guesses containing the individual result
	 */
	private void updateProgressArray(Guess[] progress) {
		for(int i = 0; i < MAX_GUESSES; i++ ) {
			if (progress[i]!= null) {
				for (int j = 0; j < WORD_LENGTH; j++) {
					if (progress[i].getIndices()[j].getDescription().equals("Correct")) {
						Label labelObj = labelCreator("correct", ""+ progress[i].getGuess().charAt(j));	
						progressArray[i][j] = labelObj;
					}
					else if (progress[i].getIndices()[j].getDescription().equals("Correct letter, wrong index")) {
						Label labelObj = labelCreator("incorrectIndex", ""+ progress[i].getGuess().charAt(j));	
						progressArray[i][j] = labelObj;	
					}
					else if (progress[i].getIndices()[j].getDescription().equals("Incorrect")){
						Label labelObj = labelCreator("incorrect", ""+ progress[i].getGuess().charAt(j));	
						progressArray[i][j] = labelObj;		
					}
				}
			}
			else {
				for(int counter = 0; counter < 5; counter +=1) {
					progressArray[i][counter] = labelCreator("backspace", " ");
				}
			}
		}
	}
	/**
	 * This method updates the drawing/ display of the graphics in the 
	 * windows according to current game state by getting info from the label 
	 * objects for the upper game board as in the game progress.
	 */
	private void drawProgressArray() {
		for (int i = 0;i<6;i++) {
			for (int j = 0; j<5; j++) {
				Label labelObj = progressArray[i][j];
				progressGrid.add(labelObj, j, i);//opposite pos of i, j for gui
			}
		}	
	}
	/**
	 * This method creates a label object for the game progress according to one of the possible conditions 
	 * passed in as parameters . The color and appearance and the text varies according to that.
	 * @param labelType: A string which specifies if a label is correct, incorrect, correct letter wrong index
	 * of unguessed
	 * @param alphabet the letter
	 * @return a newly created label object
	 */
	private Label labelCreator(String labelType, String letter) {
		Label labelObj = new Label();
		if (labelType.equals("correct")){
			labelObj.setText(letter);
			labelObj.setFont(new Font(FONT_NAME, LETTER_FONT_SIZE));
			labelObj.setBackground(CORRECT);
			labelObj.setPrefWidth(LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		else if (labelType.equals("incorrect")){
			labelObj.setText(letter);
			labelObj.setFont(new Font(FONT_NAME, LETTER_FONT_SIZE));
			labelObj.setBackground(INCORRECT);
			labelObj.setPrefWidth(LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		
		else if (labelType.equals("incorrectIndex")){
			labelObj.setText(letter);
			labelObj.setFont(new Font(FONT_NAME, LETTER_FONT_SIZE));
			labelObj.setBackground(INCORRECT_INDEX);
			labelObj.setPrefWidth(LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		else if (labelType.equals("backspace")){
			labelObj.setText(" ");
			labelObj.setFont(new Font(FONT_NAME, LETTER_FONT_SIZE));
			labelObj.setBackground(EMPTY);
			labelObj.setPrefWidth(LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		else {
			//System.out.println("went into else of label creator");
			labelObj.setText(letter);
			labelObj.setFont(new Font(FONT_NAME, LETTER_FONT_SIZE));
			labelObj.setBackground(EMPTY);
			labelObj.setPrefWidth(LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		
		return labelObj;
	}
	
	/**
	 * This method creates a label object for the letter grid according to one of the possible conditions 
	 * passed in as parameters . The color and appearance and the text varies according to that.
	 * @param labelType: A strig which specifies if a label is correct, incorrect, correct letter wrong index
	 * of unguessed
	 * @param alphabet the letter
	 * @return a newly created label object
	 */
	private Label labelCreatorForLetterGrid(String labelType, String alphabet) {
		Label labelObj = new Label();
		if (labelType.equals("correct")){
			labelObj.setText(alphabet);
			labelObj.setFont(new Font(FONT_NAME, SMALL_FONT_SIZE));
			labelObj.setBackground(CORRECT);
			labelObj.setPrefWidth(SMALL_LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		else if (labelType.equals("incorrect")){
			labelObj.setText(alphabet);
			labelObj.setFont(new Font(FONT_NAME, SMALL_FONT_SIZE));
			labelObj.setBackground(INCORRECT);
			labelObj.setPrefWidth(SMALL_LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		
		else if (labelType.equals("incorrectIndex")){
			labelObj.setText(alphabet);
			labelObj.setFont(new Font(FONT_NAME, SMALL_FONT_SIZE));
			labelObj.setBackground(INCORRECT_INDEX);
			labelObj.setPrefWidth(SMALL_LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		else {
			labelObj.setText(alphabet);
			labelObj.setFont(new Font(FONT_NAME, SMALL_FONT_SIZE));
			labelObj.setBackground(EMPTY);
			labelObj.setPrefWidth(SMALL_LETTER_SQUARE_SIZE);
			labelObj.setAlignment(Pos.CENTER);
			labelObj.setTextFill(TEXT_COLOR);
			labelObj.setBorder(BORDER);
		}
		
		return labelObj;
	}
	/**
	 * This method updates the drawing/ display of the graphics in the 
	 * windows according to current game state by getting info from the label 
	 * objects for the lower keyboard as in the game progress.
	 */
	private void drawCharacterArray() {
		int  counter = 0;
		for (int i = 0;i<3;i++) {
			for (int j = 0; j<9; j++) {
				if (counter==26) {
					break;
				}
				Label labelObj = characterArray[counter];
				counter +=1;
				letterGrid.add(labelObj, j, i);//opposite pos of i, j for gui
			}	
		}
	}
	/**
	 * This method draws the drawing/ display of the graphics in the 
	 * windows for the start of the game for the upper board
	 */
	private void drawEmptyProgressArray() {
		for (int i = 0;i<6;i++) {
			for (int j = 0; j<5; j++) {
				Label labelObj = labelCreator("backspace"," ");
				progressArray[i][j]=labelObj;
				
				progressGrid.add(labelObj, j, i);//opposite pos of i, j for gui
			}	
		}
	}
	/**
	 * This method draws the drawing/ display of the graphics in the 
	 * windows for the start of the game for the lower keyboard as in the game progress.
	 */
	private void drawEmptyCharacterArray() {
		int  counter = 0;
		for (int i = 0;i<3;i++) {
			for (int j = 0; j<9; j++) {
				if (counter==26) {
					break;
				}
				String letter = Character.toString(counter+65);
				
				Label labelObj = labelCreatorForLetterGrid("unguessed", letter);
				characterArray[counter]=labelObj;
				counter +=1;
				letterGrid.add(labelObj, j, i);//opposite pos of i, j for gui
			}	
		}
	}

}
