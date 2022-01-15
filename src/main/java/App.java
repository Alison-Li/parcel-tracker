import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
//        while (true) {
//            System.out.println("=== Parcel Tracker ===");
//            Scanner scanner = new Scanner(System.in);
//            String userInput = scanner.nextLine();
//        }
        //createTracking("canada-post", "8199374218670348");
        //getTrackingList(false);
    }

    /**
     *
     * @return A list of "slug" names for the couriers, to be used when creating a tracking.
     * @throws Exception
     */
    public static ArrayList<String> getCourierList() throws Exception {
        ArrayList<String> cleanedCourierList = new ArrayList<>();
        JsonObject response = HttpUtil.get("https://api.trackinghive.com/couriers/list");
        JsonArray courierData = response.getAsJsonArray("data");
        for (JsonElement e : courierData) {
            JsonObject obj = e.getAsJsonObject();
            String slug = obj.get("slug").toString();
            cleanedCourierList.add(slug);
        }
        return cleanedCourierList;
    }

    /**
     * Adds a tracked parcel to the tracking list.
     * @param courierName The "slug" name for the courier.
     * @param trackingNumber
     * @throws Exception
     */
    public static void createTracking(String courierName, String trackingNumber) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("tracking_number", trackingNumber);
        obj.addProperty("slug", courierName);
        String data = obj.toString();
        HttpUtil.post("https://api.trackinghive.com/trackings", data);
    }

    /**
     * Displays the list of tracked parcels.
     * @param pendingOnly Filter only pending parcels.
     * @throws Exception
     */
    public static void getTrackingList(boolean pendingOnly) throws Exception {
        ArrayList<String> cleanedTrackingList = new ArrayList<>();
        JsonObject response = HttpUtil.get("https://api.trackinghive.com/trackings?pageId=1&limit=20]");
        JsonArray trackingData = response.getAsJsonArray("data");
        // TODO: Display in more readable format
        System.out.println(trackingData.getAsJsonArray());
    }
}