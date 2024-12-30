package APILIBRARY;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        TriviaApiClient client = new TriviaApiClient();

        try {
            OpenTrivia trivia = client.fetchQuestions(10, "multiple", null, "easy");

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
