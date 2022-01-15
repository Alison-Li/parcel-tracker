import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class App {
    private static final String APP_TITLE = "PARCEL TRACKER";
    private static final String MENU_HELP_TEXT = "Enter the number for the menu option you would like to select, e.g. enter '1' for [1]";
    private static final String MENU_OPTIONS = "[1] View tracking list\n[2] Create tracking\n[3] View supported couriers\n[any] Exit";

    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println(APP_TITLE);
            System.out.println(MENU_HELP_TEXT);
            System.out.println(MENU_OPTIONS);

            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();

            if (userInput.equals("1")) {
                ArrayList<TrackingItem> itemList = getTrackingList();
                for (TrackingItem item : itemList) {
                    item.prettyPrint();
                }
            } else if (userInput.equals("2")) {
                System.out.println("Enter the courier slug: ");
                String slug = scanner.nextLine();
                System.out.println("Enter the tracking number");
                String trackingNumber = scanner.nextLine();
                createTracking(slug, trackingNumber);
            } else if (userInput.equals("3")) {
                ArrayList<String> courierList = getCourierList();
                Collections.sort(courierList);
                for (String s : courierList) {
                    System.out.println(s);
                }
            } else {
                break;
            }
        }
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
            JsonObject dataObj = e.getAsJsonObject();
            String slug = dataObj.get("slug").toString();
            cleanedCourierList.add(slug);
        }
        return cleanedCourierList;
    }

    /**
     * Displays the list of tracked parcels.
     * @throws Exception
     */
    public static ArrayList<TrackingItem> getTrackingList() throws Exception {
        ArrayList<TrackingItem> cleanedTrackingList = new ArrayList<>();
        JsonObject response = HttpUtil.get("https://api.trackinghive.com/trackings?pageId=1&limit=20");
        JsonArray trackingData = response.getAsJsonArray("data");
        for (JsonElement e : trackingData) {
            JsonObject dataObj = e.getAsJsonObject();
            String slug = dataObj.get("slug").toString(); // use slug as courier name
            String trackingNumber = dataObj.get("tracking_number").toString();
            String trackingId = dataObj.get("_id").toString();
            String currentStatus = dataObj.get("current_status").toString();

            JsonObject trackingsObj = dataObj.getAsJsonObject("trackings");
            String expectedDelivery = trackingsObj.get("expected_delivery").toString();

            TrackingItem item = new TrackingItem(slug, trackingNumber, trackingId, currentStatus, expectedDelivery);
            cleanedTrackingList.add(item);
        }
        return cleanedTrackingList;
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
        int statusCode = HttpUtil.post("https://api.trackinghive.com/trackings", data);
        if (statusCode != 200) {
            System.out.println("Something went wrong.\nSTATUS CODE: " + statusCode);
        } else {
            System.out.println("Tracking successfully created.");
        }
    }
}