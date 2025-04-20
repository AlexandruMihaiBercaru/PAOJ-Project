package models;

import java.util.ArrayList;

public class Farm {
    private String farmName;
    private User owner;
    private String address;
    private String email;
    private String phone;
    private double budget;
    private ArrayList<LandParcel> farmLand;
    private ArrayList<Inventory> inventory;

    public Farm(String farmName, User owner, String address, String email, String phone, double budget) {
        this.farmName = farmName;
        this.owner = owner;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.budget = budget;
        this.farmLand = new ArrayList<LandParcel>();
        this.inventory = new ArrayList<Inventory>();


        Inventory seedInventory = new SeedInventory();
        Inventory harvestInventory = new HarvestInventory();
        inventory.add(seedInventory);
        inventory.add(harvestInventory);
    }

    public String getFarmName() {
        return farmName;
    }

    public User getOwner() {
        return owner;
    }

    public String getAddress() {
        return address;
    }

    public double getBudget() {
        return budget;
    }

    public ArrayList<LandParcel> getFarmLand() {
        return farmLand;
    }

    public ArrayList<Inventory> getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "FARM: " + farmName + "\n" +
                "OWNER: " + owner.getFirstName() + " " + owner.getLastName() + "\n" +
                "ADDRESS: " + address + "\n" +
                "EMAIL: " + email + "\n" +
                "PHONE: " + phone + "\n";
    }

    public void addLandParcel(LandParcel newLand){
        this.farmLand.add(newLand);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInventory(ArrayList<Inventory> inventory) {
        this.inventory = inventory;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
