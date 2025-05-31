package services;

import models.*;

import java.util.Scanner;

///  this class manages a single farm (all the business logic) for a given user, who is the owner of that farm
public class FarmService {

    private User farmOwner;
    private Farm farm;
    private int landParcelsCount = 0;

    public FarmService(User farmOwner) {
        this.farmOwner = farmOwner;
    }

    public void newFarm(Scanner sc){
        System.out.println("Farm name: ");
        String name = sc.nextLine();
        System.out.println("Address: ");
        String address = sc.nextLine();
        System.out.println("Email: ");
        String email = sc.nextLine();
        System.out.println("Phone: ");
        String phone = sc.nextLine();
        System.out.println("Budget: ");
        double budget = Double.parseDouble(sc.nextLine());

        Farm myFarm = new Farm(name, farmOwner, address, email, phone, budget);
        this.farm = myFarm;

        System.out.println("You have successfully added your farm!");

    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public void checkFunds(){
        System.out.println("\nYou have " + this.farm.getBudget() + " RON left in your account.\n" +
                "-------------------------");
    }

    public void createLandParcel(Scanner sc) {
        System.out.println("Parcel ID: ");
        String parcelId = sc.nextLine();
        System.out.println("Size: ");
        double size = Double.parseDouble(sc.nextLine());
        System.out.println("Usage: (cultivation)"); // will be extended to include pasture, permanent crops (orchard / vineyard)
        String usage = sc.nextLine();

        LandLot newLand;
        if (usage.equals("cultivation"))
        {
            newLand = new ArableLand(parcelId, size, usage);
            this.farm.addLandParcel(newLand);
            System.out.println("Your farmland has just expanded!\n");
        }
    }

    public void viewFarmLand(){
        boolean existsOne = false;
        for(var parcel : this.farm.getFarmLand()){
            existsOne = true;
            System.out.println(parcel);
            System.out.println("-------------------------");
        }
        if(!existsOne)
            System.out.println("Wow...no farmland!\n");
    }

    public void updateContactInfo(Scanner sc){
        System.out.println("New email");
        String email = sc.nextLine();
        System.out.println("New phone number");
        String phone = sc.nextLine();

        this.farm.setEmail(email);
        this.farm.setPhone(phone);

        System.out.println("The contact information has been updated.\n");

    }

    public void showInventory(){
        SeedInventory seedInventory = (SeedInventory)farm.getInventory().get(0);
        seedInventory.listInventory();
    }
}
