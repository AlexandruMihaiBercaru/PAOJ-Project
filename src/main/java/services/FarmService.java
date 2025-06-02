package services;

import exceptions.EntityNotFoundException;
import models.*;
import persistence.FarmRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

///  this class manages a single farm (all the business logic) for a given user, who is the owner of that farm
public class FarmService {

    private final FarmRepository farmRepository = FarmRepository.getInstance();

    private User farmOwner;
    private Farm farm;
    //private int landParcelsCount = 0;

    public FarmService() {
        //this.farmOwner = farmOwner;
    }

    public Farm addNewFarm(Farm newFarm) {
        try{
            return farmRepository.save(newFarm);
        } catch (Exception e) {
            System.err.println("Could not save new farm: " + e.getMessage());
            return null;
        }
    }

    public Farm findFarmByName(String farmName) {
        Optional<Farm> f = farmRepository.findByNameOrId(farmName);
        return f.orElseThrow(() -> new EntityNotFoundException("There is no farm with the name: " + farmName));
    }

    public Farm findFarmByUserId(String userId){
        Optional<Farm> f = farmRepository.findByNameOrId(userId);
        return f.orElse(null);
    }

    public ArrayList<Farm> getFarms(){
        return (ArrayList<Farm>) farmRepository.findAll();
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
