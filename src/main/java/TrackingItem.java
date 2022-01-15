public class TrackingItem {
    private String courier;
    private String trackingNumber;
    private String trackingId;
    private String currentStatus;
    private String expectedDelivery;

    public TrackingItem(String courier, String trackingNumber, String trackingId, String currentStatus, String expectedDelivery) {
        this.courier = courier;
        this.trackingNumber = trackingNumber;
        this.trackingId = trackingId;
        this.currentStatus = currentStatus;
        this.expectedDelivery = expectedDelivery;
    }

    public String getCourier() {
        return this.courier;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public String getTrackingId() {
        return this.trackingId;
    }

    public String getCurrentStatus() {
        return this.currentStatus;
    }

    public String getExpectedDelivery() {
        return this.expectedDelivery;
    }

    public void prettyPrint() {
        System.out.println("COURIER: " + this.getCourier());
        System.out.println("TRACKING NUMBER: " + this.getTrackingNumber());
        System.out.println("CURRENT STATUS: " + this.getCurrentStatus());
        System.out.println("EXPECTED DELIVERY: " + this.getExpectedDelivery() + "\n");
    }
}
