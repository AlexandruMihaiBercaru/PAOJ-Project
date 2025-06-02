package services;

import models.Inventory;
import models.SeedInventory;
import models.SeedsLot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SeedService {

    private String filePath = "src/main/data/seeds.csv";

    private SeedsLot[] seedsForSale;

    public SeedService(){
        try{
            seedsForSale = FileParseService.parseSeedLots(filePath);
        }
        catch(IOException e){
            System.out.println("Error while parsing seeds.csv file...");
        }
    }

    public void inspectSeedMarket(){
        for(SeedsLot seedsLot : seedsForSale){
            seedsLot.printInfo();
        }
    }

    // TODO -> EXCEPTII

    public void buySeeds(Scanner sc, FarmService farmService){
        System.out.println("Choose seed lot");
        int lotId = Integer.parseInt(sc.nextLine());

        SeedsLot targetLot = null;
        int idx = -1;

        for(int i = 0; i < seedsForSale.length; i++){
            if (seedsForSale[i].getLotId() == lotId){
                targetLot = seedsForSale[i];
                idx = i;
                break;
            }
        }
        if(targetLot == null){
            System.out.println("No such lot!");
            return;
        }

        System.out.println("Quantity: ");
        int requestQuantity = Integer.parseInt(sc.nextLine());

        while(targetLot.getAvailableQuantity() < requestQuantity){
            System.out.println("Quantity exceeds limit, choose a smaller number!");
            System.out.println("Quantity: ");
            requestQuantity = Integer.parseInt(sc.nextLine());
        }
/*
        double budgetBeforeTransaction = farmService.getFarm().getBudget();
        if(budgetBeforeTransaction < requestQuantity * targetLot.getPricePerUnit()) {
            System.out.println("Insufficient funds!...");
            return;
        }
        else{
            System.out.println("Successful transaction!");
            farmService.getFarm().setBudget(budgetBeforeTransaction - requestQuantity * targetLot.getPricePerUnit());

            ArrayList<Inventory> oldInventory = farmService.getFarm().getInventory();
            SeedInventory seedInventory = (SeedInventory) oldInventory.get(0);
            ArrayList<SeedsLot> ownedSeedsLots = seedInventory.getOwnedSeeds();

            // actualizez stocul fermei
            ownedSeedsLots.add(new SeedsLot(targetLot, requestQuantity));
            seedInventory.setOwnedSeeds(ownedSeedsLots);
            oldInventory.set(0, seedInventory);
            farmService.getFarm().setInventory(oldInventory);

            // actualizez stocul de seminte pentru vanzare
            seedsForSale[idx].setAvailableQuantity(targetLot.getAvailableQuantity() - requestQuantity);

            System.out.println("Your inventory has been updated!");
        }
*/


    }


}
