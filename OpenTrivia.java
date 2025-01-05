package Unipi.TriviaProject;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import Unipi.TriviaProject.Result;


public class OpenTrivia {

    @JsonProperty("response_code")
    private long responseCode;

    private List<Result> results;

    public OpenTrivia() {
    }

    public OpenTrivia(long responseCode, List<Result> results) {
        super();
        this.responseCode = responseCode;
        this.results = results;
    }

    public long getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(long responseCode) {
        this.responseCode = responseCode;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OpenTrivia.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("responseCode");
        sb.append('=');
        sb.append(this.responseCode);
        sb.append(',');
        sb.append("results");
        sb.append('=');
        sb.append(((this.results == null)?"<null>":this.results));
        sb.append(']');
        return sb.toString();
    }
}
