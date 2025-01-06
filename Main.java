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

		int noOfQuestions;
		String categ;
		String type;
		String diff;
		String[] gameArch = { "Default Settings", "Custom Settings" };
		int score = 0;
		int maxScore = 0;
		// declaring Scanner
		// Scanner keyInp = new Scanner(System.in);

		Procedures proc = new Procedures();

//Opening communication with User

		JOptionPane.showMessageDialog(null, "Hello User!\nWelcome to our Trivia Game", "Trivia Game",
				JOptionPane.INFORMATION_MESSAGE);
		boolean aa = true;
		while (aa==true) {
//asking Whether he wants custom settings or default for the game to play		
			int archType = JOptionPane.showOptionDialog(null, "Choose type of questions", "Trivia Game",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameArch, gameArch[1]);
			if (archType == -1) {
				proc.close();
			}
			if (archType == 0) {
				categ = null;
				type = null;
				diff = null;
				noOfQuestions = 10;
			} else {

				JOptionPane.showMessageDialog(null,
						"Hello User!\nTo Start the game please enter from 1-50 how many questions you would like to play.",
						"Trivia Game", JOptionPane.INFORMATION_MESSAGE);

				// Ask the User how many questions we wants
				/*
				 * System.out.print(
				 * "Hello User to our Trivia Game !\nTo Start the game please enter from 1-50 how many questions you would like to play. \nYou entered: "
				 * );
				 */

				// setters and getters for the parameters of the game
				proc.setQuest();
				noOfQuestions = proc.getQuest();
				proc.setCateg();
				categ = proc.getCateg();
				proc.setType();
				type = proc.getType();
				proc.setDiff();
				diff = proc.getDiff();
			}
			boolean a = true;
			while (a == true) {
				JOptionPane.showMessageDialog(null,
						"You are playing with " + noOfQuestions + " questions. Let's Start the game!", "Trivia Game",
						JOptionPane.INFORMATION_MESSAGE);

				// System.out.println(noOfQuestions+ type+" "+categ+" "+diff);

				TriviaApiClient client = new TriviaApiClient();
				try {
					OpenTrivia trivia = client.fetchQuestions(noOfQuestions, type, categ, diff);

					System.out.println("Response Code: " + trivia.getResponseCode());
					if (trivia.getResponseCode() == 0) {

						for (Result result : trivia.getResults()) {

							ArrayList<String> possibleAnswers = new ArrayList<String>();
							possibleAnswers.add(result.getCorrectAnswer());
							possibleAnswers.addAll(result.getIncorrectAnswers());
							Collections.shuffle(possibleAnswers);

							String[] options = possibleAnswers.toArray(new String[0]);
							System.out.println(Arrays.toString(options) + "\n");
							String quest = "Difficulty: " + result.getDifficulty() + "\nType: " + result.getType()
									+ "\n Question: " + result.getQuestion();

							int question = JOptionPane.showOptionDialog(null, quest, "Trivia Game",
									JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
									options[0]);

							if (options[question].equals(result.getCorrectAnswer())) {
								JOptionPane.showMessageDialog(null, "Correct Answer! ", "Trivia Game",
										JOptionPane.INFORMATION_MESSAGE);
								score = score + 10;

							} else {
								JOptionPane.showMessageDialog(null,
										"Wrong Answer! \nThe correct answer was: " + result.getCorrectAnswer(),
										"Trivia Game", JOptionPane.INFORMATION_MESSAGE);
								score = score - 5;
							}

							System.out.println(result.getDifficulty() + " Question: " + result.getQuestion());
							System.out.println("Correct Answer: " + result.getCorrectAnswer());
							System.out.println("Incorrect Answers: " + result.getIncorrectAnswers());
							System.out.println("---");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Failed Query", "Error!", JOptionPane.ERROR_MESSAGE);
					}

				} catch (IOException e) {
					System.err.println("Error fetching trivia questions: " + e.getMessage());
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("Error : " + e.getMessage());
					proc.close();
				}

				JOptionPane.showMessageDialog(null, "Your score is: " + score, "Trivia Game",
						JOptionPane.INFORMATION_MESSAGE);
				System.out.println(score);
				if (maxScore < score) {
					maxScore = score;
					JOptionPane.showMessageDialog(null, "New High Score! \n" + maxScore + " !!!", "Trivia Game",
							JOptionPane.INFORMATION_MESSAGE);

				}
				score = 0;
				int sameGame = JOptionPane.showOptionDialog(null, "Do you want to play the same game again? ",
						"Trivia Game", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
						null);
				System.out.println(sameGame);
				if (sameGame == 0) {
					continue;
				} else if (sameGame == 1) {
					a = false;
					maxScore = 0;
				} else if (sameGame == 2) {
					proc.close();
				}
			}
		}
	}
}
