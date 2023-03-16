# Wordle GUI


Designed and implemented a GUI-based UI version of the popular game Wordle applying MVC (Model/View/Controller) Architecture using Object Oriented Programming, JavaFX graphics, and tested using Junit. Made use of Observer-Observable Design pattern to develop a refined and responsive application. Created Documentation using Javadoc.


Model class makes necessary data structures after each guess has been 
made. It extends observable class of the observer- obervable design pattern.
The text based UI and the GUI based UI are its observers in corresponding 
circumstances. It also throws exceptions for any invalid input guesses as determined
 by the user. It also loads in the dictionary file which is the list of 
valid words and selects a random answer.

Controller: is the controller class of the MVC Arhitecture.
This class is used to query the model based on 
 the input from the user that comes through the view. It abstracts the
view class from the model. The view does not communicate directly with the
model but it communicates directly with the controller.

View Class: This program implements the GUI front end based graphical version of wordle in accordance with the MVC  architecture. Both the guesses and the guessed characters are Label objects placed inside GridPane objects. These two separate GridPane objects are placed inside of a VBox which is then placed inside of the Scene. It this displays the game actions after every update has been  made. Animation has been added after the user has ended the game using fade on and fade off transition. Alerts are shown for events like invalid guess and game being over. Event handling has been done through  Lambda functions.

Do not use this code without the permission of Author.