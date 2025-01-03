package Unipi.TriviaProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class TriviaApiClient {

    private static final String BASE_URL = "https://opentdb.com/api.php";
    private final ObjectMapper objectMapper;

    public TriviaApiClient() {
        this.objectMapper = new ObjectMapper();
    }

    public OpenTrivia fetchQuestions(int amount, String type, String category, String difficulty) throws IOException {

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("?amount=").append(amount);

        if (type != null && !type.isEmpty()) {
            urlBuilder.append("&type=").append(type);
        }
        if (category != null && !category.isEmpty()) {
            urlBuilder.append("&category=").append(category);
        }
        if (difficulty != null && !difficulty.isEmpty()) {
            urlBuilder.append("&difficulty=").append(difficulty);
        }

        String url = urlBuilder.toString();


        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);


        HttpResponse response = client.execute(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());


        return objectMapper.readValue(jsonResponse, OpenTrivia.class);
    }
}
