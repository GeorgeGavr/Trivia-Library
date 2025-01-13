package Unipi.TriviaProject;

import java.util.Scanner;
import javax.swing.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class Procedures {

	private int noOfQuestions;
	private String selectedId;
	private String selectedType;
	private String selectedDiff;
	final String[] gameDefault = { "OK" };
	int def;

	// declaring Scanner
	Scanner keyInp = new Scanner(System.in);
	//close program method
	public  void close() {
		JOptionPane.showMessageDialog(null, "Goodbye!", "Cancelled", JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}
	//setting the no. of questions
	public void setQuest() {
		while (true) {
			// Opening Dialog Box
			String input = JOptionPane.showInputDialog(null, "Enter the number of questions (1-50):",
					"Set Number of Questions", JOptionPane.QUESTION_MESSAGE);

			// Handle cancel or close
			if (input == null) {
				close();

			}

			try {
				noOfQuestions = Integer.parseInt(input);

				if (noOfQuestions > 0 && noOfQuestions <= 50) {
					
					 def = JOptionPane.showOptionDialog(null,
							"You set " + noOfQuestions + " questions.", "Trivia Game - Success",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gameDefault,
							gameDefault[0]);
					// -1 equals close button
					if (def != 0) {
						close();
					}

					break;
				} else {
					
					 def = JOptionPane.showOptionDialog(null,
								"Please enter a number between 1 and 50.", "Trivia Game - Invalid Input",
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, gameDefault,
								gameDefault[0]);
						// -1 equals close button
						if (def != 0) {
							close();
						}
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	//getters
	public int getQuest() {
		return noOfQuestions;
	}

	public String getDiff() {
		return selectedDiff;
	}

	public String getCateg() {

		return selectedId;
	}

	public String getType() {

		return selectedType;
	}
	
	
	public String getNameType(String selectedType) {

		String nameType ="";
		if(selectedType.equals("multiple")) {
			nameType="Multiple";
		}else if (selectedType.equals("boolean")) {
			nameType="True/False";
		}
		return nameType;
	}
	
	
	//setting the type of questions
	public void setType() {
		String[] type = { "Multiple", "True/False", "Random" };

		int chType = JOptionPane.showOptionDialog(null, "Choose type of questions", "Type",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, type, type[2]);

		if (chType == 0) {
			this.selectedType = "multiple";
		} else if (chType == 1) {
			this.selectedType = "boolean";
		} else if (chType == 2) {
			this.selectedType = null;
		} else {
			close();
		}

	}

	//setting the category
	public void setDiff() {
		String[] diff = { "Easy", "Medium", "Hard", "Random" };

		int chType = JOptionPane.showOptionDialog(null, "Choose Difficulty of questions", "Difficulty",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, diff, diff[3]);

		if (chType == 0) {
			this.selectedDiff = "easy";
		} else if (chType == 1) {
			this.selectedDiff = "medium";
		} else if (chType == 2) {
			this.selectedDiff = "hard";
		} else if (chType == 3) {
			this.selectedDiff = null;
		} else {
			close();
		}
	}


	
	public void setCateg() {

		// JSON string brought by api https://opentdb.com/api_category.php

		/*String jsonString = "{\n" + "    \"trivia_categories\": [\n" + "        {\"id\": 0, \"name\": \"Random\"},\n"
				+ "        {\"id\": 9, \"name\": \"General Knowledge\"},\n"
				+ "        {\"id\": 10, \"name\": \"Entertainment: Books\"},\n"
				+ "        {\"id\": 11, \"name\": \"Entertainment: Film\"},\n"
				+ "        {\"id\": 12, \"name\": \"Entertainment: Music\"},\n"
				+ "        {\"id\": 13, \"name\": \"Entertainment: Musicals & Theatres\"},\n"
				+ "        {\"id\": 14, \"name\": \"Entertainment: Television\"},\n"
				+ "        {\"id\": 15, \"name\": \"Entertainment: Video Games\"},\n"
				+ "        {\"id\": 16, \"name\": \"Entertainment: Board Games\"},\n"
				+ "        {\"id\": 17, \"name\": \"Science & Nature\"},\n"
				+ "        {\"id\": 18, \"name\": \"Science: Computers\"},\n"
				+ "        {\"id\": 19, \"name\": \"Science: Mathematics\"},\n"
				+ "        {\"id\": 20, \"name\": \"Mythology\"},\n" + "        {\"id\": 21, \"name\": \"Sports\"},\n"
				+ "        {\"id\": 22, \"name\": \"Geography\"},\n" + "        {\"id\": 23, \"name\": \"History\"},\n"
				+ "        {\"id\": 24, \"name\": \"Politics\"},\n" + "        {\"id\": 25, \"name\": \"Art\"},\n"
				+ "        {\"id\": 26, \"name\": \"Celebrities\"},\n"
				+ "        {\"id\": 27, \"name\": \"Animals\"},\n" + "        {\"id\": 28, \"name\": \"Vehicles\"},\n"
				+ "        {\"id\": 29, \"name\": \"Entertainment: Comics\"},\n"
				+ "        {\"id\": 30, \"name\": \"Science: Gadgets\"},\n"
				+ "        {\"id\": 31, \"name\": \"Entertainment: Japanese Anime & Manga\"},\n"
				+ "        {\"id\": 32, \"name\": \"Entertainment: Cartoon & Animations\"}\n" + "    ]\n" + "}";

*/
		
		
		try {
			String jsonString=TriviaApiClient.Categories();
	
			
			// Parse JSON
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(jsonString);
			JsonNode categoriesNode = rootNode.get("trivia_categories");

			// Create 2D array
			String[][] categories = new String[categoriesNode.size()+1][2];
			categories[categoriesNode.size()][0]="0";
			categories[categoriesNode.size()][1]="Random";
			for (int i = 0; i < categoriesNode.size(); i++) {
				JsonNode category = categoriesNode.get(i);
				categories[i][0] = String.valueOf(category.get("id"));
				categories[i][1] = category.get("name").asText();
			}

			// Create options for selection
			String[] categoryNames = new String[categories.length];
			for (int i = 0; i < categories.length; i++) {
				categoryNames[i] = categories[i][1];
			}

			// Show category selection dialog
			String selectedCategory = (String) JOptionPane.showInputDialog(null, "Select a category:",
					"Category Picker", JOptionPane.QUESTION_MESSAGE, null, categoryNames, categoryNames[0] // Default
																											// selection
			);

			// Handle selection
			if (selectedCategory != null) {

				for (String[] category : categories) {
					if (category[1].equals(selectedCategory)) {
						this.selectedId = category[0];

						break;
					}
				}
			
				def = JOptionPane.showOptionDialog(null,
						"You selected: " + selectedCategory, "Trivia Game - Category Selected",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gameDefault,
						gameDefault[0]);
				// -1 equals close button
				if (def != 0) {
					close();
				}
			} else {
				close();

			}

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error parsing JSON.", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

}
