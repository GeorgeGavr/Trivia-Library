package Unipi.TriviaProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {
	public static void main(String[] args) {

		int noOfQuestions; // Number of questions
		String categ; // Category
		String type; // Type(multiple or boolean)
		String diff; // Difficulty of game
		String nametype;
		int score = 0; // Score per game
		int maxScore = 0; // Max score
		boolean q = true; // To check if player has started a game and if he wants to continue with this
							// settings

		final String[] gameDefault = { "OK" };
		// Whether player wants deafault settings or to play with his own settings
		String[] gameArch = { "Default Settings", "Custom Settings" };
		// Class that has constructors for the game settings
		Procedures proc = new Procedures();

//Opening communication with User

		int def = JOptionPane.showOptionDialog(null, "Hello User!\nWelcome to our Trivia Game", "Trivia Game",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gameDefault, gameDefault[0]);
		// -1 equals close button
		if (def != 0) {
			proc.close();
		}
		// just a boolean so player can start from main menu till he closes the dialog

		boolean aa = true;
		while (aa == true) {
//asking Whether he wants custom settings or default for the game to play		
			int archType = JOptionPane.showOptionDialog(null, "Choose type of questions", "Trivia Game",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameArch, gameArch[1]);
			// -1 equals close button
			if (archType == -1) {
				proc.close();
			}
			// Default category selected
			if (archType == 0) {
				categ = null;
				type = null;
				diff = null;
				noOfQuestions = 10;
			} else {
				// Custom settings
				// Ask the User how many questions we wants

				def = JOptionPane.showOptionDialog(null,
						"Please enter from 1-50 how many questions you would like to play.", "Trivia Game",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gameDefault,
						gameDefault[0]);
				// -1 equals close button
				if (def != 0) {
					proc.close();
				}

				// Setters and getters for the parameters of the game
				proc.setQuest();
				noOfQuestions = proc.getQuest();
				proc.setCateg();
				categ = proc.getCateg();
				proc.setType();
				type = proc.getType();
				proc.setDiff();
				diff = proc.getDiff();
			}
			// another boolean to keep track whether player wants to play with the same
			// parameters
			boolean a = true;
			while (a == true) {

				TriviaApiClient client = new TriviaApiClient();
				try {
					// pulling the Api
					OpenTrivia trivia = client.fetchQuestions(noOfQuestions, type, categ, diff);
					// Console checking
					// System.out.println("Response Code: " + trivia.getResponseCode());

					// checking if the game parameters are valid. If everythings is valid ,game
					// starts

					if (trivia.getResponseCode() == 0) {
						// for each question
						for (Result result : trivia.getResults()) {

							// Add all possible answers in an ArrayList and the randomize them. after insert
							// in an array to show them in GUI
							ArrayList<String> possibleAnswers = new ArrayList<String>();
							possibleAnswers.add(result.getCorrectAnswer());
							possibleAnswers.addAll(result.getIncorrectAnswers());
							Collections.shuffle(possibleAnswers);

							String[] options = possibleAnswers.toArray(new String[0]);

							// Console checking
							// System.out.println(Arrays.toString(options) + "\n");

							// Readying the question with the possible answers that will show up to the
							// player
							String quest = "Difficulty: " + result.getDifficulty() + "\nType: "
									+ proc.getNameType(result.getType()) + "\n Category: " + result.getCategory()
									+ "\n Question: " + result.getQuestion();

							// Console checking, to check the correct answer
							// System.out.println("Correct Answer: " + result.getCorrectAnswer());

							// GUI with the string above
							int question = JOptionPane.showOptionDialog(null, quest, "Trivia Game",
									JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
									options[0]);

							// If answer correct show to the player + give him 10 points
							// Close button here no action ,so no mistake to close program
							if (options[question].equals(result.getCorrectAnswer())) {
								JOptionPane.showMessageDialog(null, "Correct Answer! ", "Trivia Game",
										JOptionPane.INFORMATION_MESSAGE);
								score = score + 10;

							} else {
								// show him he picked the wrong one ,show the correct and deduct him 5 points
								JOptionPane.showMessageDialog(null,
										"Wrong Answer! \nThe correct answer was: " + result.getCorrectAnswer(),
										"Trivia Game", JOptionPane.INFORMATION_MESSAGE);
								score = score - 5;
							}

							/*
							 * Console Checking 
							 * System.out.println(result.getDifficulty() + " Question: " +
							 * result.getQuestion()); System.out.println("Correct Answer: " +
							 * result.getCorrectAnswer()); System.out.println("Incorrect Answers: " +
							 * result.getIncorrectAnswers()); System.out.println("---");
							 */
						}
					} else {
						// Failed query, parameters dont bring a feasible game
						JOptionPane.showMessageDialog(null, "Failed Query. Please try other parameters.", "Error!",
								JOptionPane.ERROR_MESSAGE);
						q = false;
					}
					// Something went wrong with the api call
				
				} catch (IOException e) {
					System.err.println("Error fetching trivia questions: " + e.getMessage());
					
					// After many attempts too fast it broke 
					q=false;
					
					def = JOptionPane.showOptionDialog(null,
							"Too many attempts in little time. \nBack to main menu." , "Trivia Game",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, gameDefault,
							gameDefault[0]);
					// -1 equals close button
					if (def != 0) {
						proc.close();
					}
					
				} catch (ArrayIndexOutOfBoundsException e) {
					// Close the program when user press close button
					System.err.println("Error : " + e.getMessage());
					proc.close();
				}
				// if he answered all questions, to show the score
				if (q == true) {
				
					def = JOptionPane.showOptionDialog(null,
							"Your score is: " + score, "Trivia Game",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gameDefault,
							gameDefault[0]);
					// -1 equals close button
					if (def != 0) {
						proc.close();
					}
					
				}

				// Console Checking
				// System.out.println(score);

				// Checking for a high Score
				if (maxScore < score) {
					maxScore = score;
					def = JOptionPane.showOptionDialog(null,
							"New High Score! \n" + maxScore + " !!!", "Trivia Game - Max Score",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gameDefault,
							gameDefault[0]);
					// -1 equals close button
					if (def != 0) {
						proc.close();
					}

				}

				// restarting score variable and asking whether he wants same game , other
				// parameters game or to close it
				score = 0;
				if (q == true) {
					int sameGame = JOptionPane.showOptionDialog(null, "Do you want to play the same game again? ",
							"Trivia Game", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
							null);

					// 3 options described above 0:continue / 1:other game , 2||-1:Close

					if (sameGame == 0) {
						continue;
					} else if (sameGame == 1) {
						a = false;
						maxScore = 0;
					} else if (sameGame == 2 || sameGame == -1) {
						proc.close();
					}

				} else {
					q = true;
					a = false;
				}
			}
		}
	}
}
