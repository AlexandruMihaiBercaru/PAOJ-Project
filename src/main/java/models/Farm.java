package models;

public class Farm {
    private String farnName;
    private User owner;
    private String address;
    private LandParcel[] farmLand;
    private Inventory[] inventory;

    public Farm(String farnName, User owner, String address) {
        this.farnName = farnName;
        this.owner = owner;
        this.address = address;
    }
}
