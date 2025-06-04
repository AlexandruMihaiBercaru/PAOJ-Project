package models;

import utils.RandomStringGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Farm {
    private String farmId;
    private String farmName;
    private User owner;
    private String address;
    private String email;
    private String phone;
    private double budget;
    private ArrayList<LandLot> farmLand;
    private Map<String, Inventory> inventory;

    // constructor pentru ferma neinitializata (fara owner)
    public Farm(String farmName, String address, String email, String phone, double budget) {
        this.farmName = farmName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.budget = budget;
        this.farmLand = new ArrayList<>();
        this.inventory = new HashMap<>();

        this.farmId = RandomStringGenerator.newString(6);

        Inventory seedInventory = new SeedInventory();
        Inventory harvestInventory = new HarvestInventory();
        inventory.put("Seeds", seedInventory);
        inventory.put("Harvests", harvestInventory);
    }

    public Farm(String farmName, User owner, String address, String email, String phone, double budget){
        this.farmName = farmName;
        this.owner = owner;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.budget = budget;
        this.farmLand = new ArrayList<>();
        this.inventory = new HashMap<>();

        Inventory seedInventory = new SeedInventory();
        Inventory harvestInventory = new HarvestInventory();
        inventory.put("Seeds", seedInventory);
        inventory.put("Harvests", harvestInventory);
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

    public ArrayList<LandLot> getFarmLand() {
        return farmLand;
    }

    public Map<String, Inventory> getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "===================================\n" +
                "FARM: " + farmName + "\n" +
                "OWNER: " + owner.getFirstName() + " " + owner.getLastName() + "\n" +
                "ADDRESS: " + address + "\n" +
                "EMAIL: " + email + "\n" +
                "PHONE: " + phone + "\n";
    }

    public void addLandParcel(LandLot newLand){
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

    public void setInventory(HashMap<String, Inventory> inventory) {
        this.inventory = inventory;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setFarmLand(ArrayList<LandLot> farmLand) {
        this.farmLand = farmLand;
    }
}

