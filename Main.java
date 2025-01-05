package Unipi.TriviaProject;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {

		
		int noOfQuestions;
		String categ;
		String type;
		String diff;

		// declaring Scanner
		// Scanner keyInp = new Scanner(System.in);

		Procedures proc = new Procedures();

		JOptionPane.showMessageDialog(null,
				"Hello User!\nTo Start the game please enter from 1-50 how many questions you would like to play.",
				"Welcome to Trivia Game", JOptionPane.INFORMATION_MESSAGE);

		// Ask the User how many questions we wants
		/*
		 * System.out.print(
		 * "Hello User to our Trivia Game !\nTo Start the game please enter from 1-50 how many questions you would like to play. \nYou entered: "
		 * );
		 */
		proc.setQuest();
		noOfQuestions = proc.getQuest();
		proc.setCateg();
		categ = proc.getCateg();
		proc.setType();
		type = proc.getType();
		proc.setDiff();
		diff = proc.getDiff();

		JOptionPane.showMessageDialog(null, "You are playing with " + noOfQuestions + " questions",
				"Welcome to Trivia Game", JOptionPane.INFORMATION_MESSAGE);

		TriviaApiClient client = new TriviaApiClient();
		try {
			OpenTrivia trivia = client.fetchQuestions(noOfQuestions, type, categ, diff);

			System.out.println("Response Code: " + trivia.getResponseCode());
			if (trivia.getResponseCode() == 0) {

				trivia.getResults().forEach(result -> {
					System.out.println(result.getDifficulty() + " Question: " + result.getQuestion());
					System.out.println("Correct Answer: " + result.getCorrectAnswer());
					System.out.println("Incorrect Answers: " + result.getIncorrectAnswers());
					System.out.println("---");
				});
			}else {
					System.out.println("failed query");
				}
			
		} catch (IOException e) {
			System.err.println("Error fetching trivia questions: " + e.getMessage());
		}

	}
}
