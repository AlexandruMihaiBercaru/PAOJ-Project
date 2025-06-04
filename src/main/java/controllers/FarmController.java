package controllers;

import exceptions.InsufficientFundsException;
import models.*;
import oracle.jdbc.internal.XSCacheOutput;
import services.*;
import view.UserInteractions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Date;
import java.util.stream.Collectors;

public class FarmController {

    private final FarmService farmService;
    private final SeedsLotService seedsLotService;
    private final CropService cropService;
    private final LandService landService;
    private Farm farm;

    public FarmController() {
        farmService = new FarmService();
        seedsLotService = new SeedsLotService();
        cropService = new CropService();
        landService = new LandService();
    }

    public void listAllFarmsContactInformation(){
        ArrayList<Farm> allFarms = farmService.getFarms();
        allFarms.stream()
                .sorted(Comparator.comparing(Farm::getFarmName))
                .forEach(System.out::println);
    }

    public void attachUserToFarm(User user){
        farmService.setFarmOwner(user);
        // caut ferma
        farm = farmService.findFarmByUserId(user.getUserId());

        // atasez pamantul
        farm.setFarmLand(farmService.getLandByFarmId(farm.getFarmId()));

        // atasez inventarul de seminte
        SeedInventory seedInventoryFromDb = new SeedInventory();
        seedInventoryFromDb.setOwnedSeeds(farmService.getSeedsInventory(farm));
        farm.getInventory().put("Seeds", seedInventoryFromDb);

        // atasez inventarul de recolte
        HarvestInventory harvestInventoryFromDb = new HarvestInventory();
        harvestInventoryFromDb.setHarvests(farmService.getHarvestsInInventory(farm));
        farm.getInventory().put("Harvests", harvestInventoryFromDb);

    }

    public void checkFunds(){
        System.out.println("\nYou have " + farm.getBudget() + " RON in your account.\n");
        farmService.updateCurrentFarm(farm);
    }

    public void saveEmailPhone(String email, String phone){
        farm.setEmail(email);
        farm.setPhone(phone);
        farmService.updateCurrentFarm(farm);

    }

    public void buySeeds(Scanner sc){
        System.out.println("What would you like to buy?");

        seedsLotService.printCropNames();

        String inputCrop = sc.nextLine();
        ArrayList<SeedsLot> seedsForSale = seedsLotService.getAllSeedsForSale();

        seedsForSale.stream()
                .filter(lot -> lot.getCrop().getCommonName().equals(inputCrop))
                .forEach(System.out::println);

        System.out.println("Choose seed lot");
        String lotId = sc.nextLine();
        SeedsLot targetLot = seedsLotService.getLotById(lotId);

        System.out.println("Quantity: ");
        int requestQuantity = Integer.parseInt(sc.nextLine());
        //the "transaction" begins
        try{
            if(checkQuantityAndBalance(targetLot, requestQuantity, farm.getBudget())){
                System.out.println("Successful transaction!");
                farm.setBudget(farm.getBudget() - requestQuantity * targetLot.getPricePerUnit());

                OwnedSeeds newLot = new OwnedSeeds(targetLot,
                        LocalDate.now(), requestQuantity, requestQuantity, farm.getFarmId());
                newLot.setSeedsId();

                targetLot.setAvailableQuantity(targetLot.getAvailableQuantity() - requestQuantity);
                seedsLotService.updateLot(targetLot);

                //updating the collection
                SeedInventory seedInventory = (SeedInventory) farm.getInventory().get("Seeds");
                ArrayList<OwnedSeeds> allOwnedSeeds =  seedInventory.getOwnedSeeds();
                allOwnedSeeds.add(newLot);
                seedInventory.setOwnedSeeds(allOwnedSeeds);
                farm.getInventory().put("Seeds", seedInventory);

                //db
                farmService.addSeedsToFarmInventory(newLot);

                System.out.println("Your inventory has been updated!");
            }
        } catch (InsufficientFundsException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    private boolean checkQuantityAndBalance(SeedsLot targetLot,
                                            int request, double balance)
            throws InsufficientFundsException, IllegalArgumentException
    {
        if(targetLot.getAvailableQuantity() < request){
            throw new IllegalArgumentException("There are not enough seeds available!");
        }
        if(targetLot.getPricePerUnit() * request > balance){
            throw new InsufficientFundsException("You do not have enough funds to buy that amount!");
        }
        return true;
    }


    public void listSeedInventory(){
        SeedInventory si = (SeedInventory) farm.getInventory().get("Seeds");
        si.getOwnedSeeds().forEach(System.out::println);
    }


    public void addLandLot(Scanner sc) {
        System.out.println("Lot Name: ");
        String lotName = sc.nextLine();
        System.out.println("Size: ");
        double size = Double.parseDouble(sc.nextLine());

        String usage = UserInteractions.getValidatedInput(sc,
                "usage: (arable/orchard)",
                us -> us.equals("arable") || us.equals("orchard"),
                "Land usage must be either arable or orchard.");
        LandLot newLand = null;

        if (usage.equals("arable"))
        {
            newLand = new ArableLand(lotName, size, usage, farm.getFarmId(),
                    null, null, false);
        } else if (usage.equals("orchard")) {
            System.out.println("Please provide the age of your trees: ");
            int age = Integer.parseInt(sc.nextLine());
            System.out.println("What fruits are you cultivating? ");
            String cropName = sc.nextLine();
            System.out.println("And what cultivar? ");
            String cultivar = sc.nextLine();

            Crop crop = cropService.getCropWithCultivarOrInsertNew(cropName, cultivar);

            newLand = new Orchard(lotName, size, usage, farm.getFarmId(), crop, age);
        }

        System.out.println("Your farmland has just expanded!\n");

        assert newLand != null;
        newLand.setLandId();
        this.farm.addLandParcel(newLand);

        //update db
        farmService.addLandLotToFarm(newLand);
    }


    public void viewFarmLand(){
        ArrayList<LandLot> landAttachedToFarm = farmService.getLandByFarmId(farm.getFarmId());
        landAttachedToFarm.forEach(System.out::println);
    }

    public void plantSeeds(Scanner sc) {

        List<LandLot> arableLandNotCultivated = farmService.getLandByFarmId(farm.getFarmId())
                .stream()
                .filter(lot -> lot.getLandUsage().equals("arable"))
                .filter(arable_lot -> !((ArableLand) arable_lot).isCurrentlyCultivated())
                .toList();

        arableLandNotCultivated.forEach(System.out::println);

        System.out.println("Please enter the name of the land you want to use: ");
        String landName = sc.nextLine();
        ArableLand targetLot = (ArableLand) landService.getLandLotByName(farm.getFarmId(), landName);

        listSeedInventory();
        System.out.println("Please pick a seed ID: ");
        String seedId = sc.nextLine();
        OwnedSeeds targetSeed = farmService.getSeedsByLotName(seedId, farm);


        System.out.println("Please choose the seed quantity you want to use: (<" + targetSeed.getQuantityAvailable() + ")");
        int seedQuantity = Integer.parseInt(sc.nextLine());

        targetSeed.setQuantityAvailable(targetSeed.getQuantityAvailable() - seedQuantity);
        targetLot.setCurrentlyCultivated(true);
        targetLot.setPlantingDate(Date.valueOf(LocalDate.now()));
        targetLot.setSeedsUsed(targetSeed);

        System.out.println("\nSeeds planted!\n");
        landService.UpdateLandLot(targetLot);
        farmService.updateOwnedSeeds(targetSeed);

        System.out.println("\nCurrently cultivated arable land:\n");
        landService.getCultivatedLandLots(farm.getFarmId()).forEach(System.out::println);
    }
}
