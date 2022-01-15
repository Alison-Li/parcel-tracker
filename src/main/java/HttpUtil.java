import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtil {

    /**
     * How to obtain a bearer token:
     * 1. Sign up at: https://www.trackhive.co/
     * 2. Go to Settings -> API Keys
     * 3. Click the '+' to generate a token
     */
    private static final String BEARER_TOKEN = "YOUR_TOKEN";

    public static JsonObject get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Authorization", BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void post(String uri, String data) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Authorization", BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("STATUS CODE: " + response.statusCode());
            } else {
                System.out.println("Tracking successfully created.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
