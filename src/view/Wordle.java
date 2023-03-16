/**
 * @author Amimul Ehsan Zoha
 * FILE: Wordle.java

 * 
 * PURPOSE: This is the main class. JavaFX is used to create a GUI for our Wordle game. 
 * When invoked with a command line argument of “-text”,
 * the text-oriented UI is launched. When invoked with a command line argument of “-gui” GUI UI is launched 
 * The default will be the GUI view if no command line argument is given. 
 */



package view;
import java.io.FileNotFoundException;
import javafx.application.Application;

public class Wordle {
	
    public static void main(String[] args) throws FileNotFoundException {
    	if(args[0].equals("-text")) {
    		WordleTextView textView = new WordleTextView();
    	}
    	else if(args[0].equals("-gui")) {
    		Application.launch(WordleGUIView.class, args);
    	}
    	else{
    		Application.launch(WordleGUIView.class, args);
    	} 	
    } 
}
