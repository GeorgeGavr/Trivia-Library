package Unipi.TriviaProject;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {

		boolean cont = true;
		int noOfQuestions;
		String Categ;

		// declaring Scanner
		Scanner keyInp = new Scanner(System.in);

		Procedures proc = new Procedures();

		JOptionPane.showMessageDialog(null, "Hello User!\nTo Start the game please enter from 1-50 how many questions you would like to play.", "Welcome to Trivia Game", JOptionPane.INFORMATION_MESSAGE);

		// Ask the User how many questions we wants
		/*System.out.print(
				"Hello User to our Trivia Game !\nTo Start the game please enter from 1-50 how many questions you would like to play. \nYou entered: ");
*/
		proc.setQuest();
		noOfQuestions = proc.getQuest();
		proc.setCateg();
		Categ=proc.getCateg();

		System.out.print(Categ);
		
		/*
		 * 
		 * 
		 * int noOfQuestions = keyInp.nextInt();
		 * System.out.println("So we will play with " + noOfQuestions + " questions.");
		 * 
		 * int noe1 = keyInp.nextInt(); System.out.print("So we will play with " + noe1
		 * + " questions.");
		 */

		// }
		System.out.println("End of program" + noOfQuestions);
	
		
		TriviaApiClient client = new TriviaApiClient();
		try {
			OpenTrivia trivia = client.fetchQuestions(noOfQuestions, "multiple", Categ, null);

			System.out.println("Response Code: " + trivia.getResponseCode());
			trivia.getResults().forEach(result -> {
				System.out.println("Question: " + result.getQuestion());
				System.out.println("Correct Answer: " + result.getCorrectAnswer());
				System.out.println("Incorrect Answers: " + result.getIncorrectAnswers());
				System.out.println("---");
			});
		} catch (IOException e) {
			System.err.println("Error fetching trivia questions: " + e.getMessage());
		}

	}
}
