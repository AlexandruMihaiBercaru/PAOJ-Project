package services;

import exceptions.EntityNotFoundException;
import models.*;
import persistence.FarmRepository;
import persistence.FarmSeedsRepository;
import persistence.HarvestRepository;
import persistence.LandLotRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

///  this class manages a single farm (all the business logic) for a given user, who is the owner of that farm
public class FarmService {

    private final FarmRepository farmRepository = FarmRepository.getInstance();
    private final FarmSeedsRepository farmSeedsRepository = FarmSeedsRepository.getInstance();
    private final LandLotRepository landLotRepository = LandLotRepository.getInstance();
    private final HarvestRepository harvestRepository = HarvestRepository.getInstance();

    private User farmOwner;

    public FarmService() {}

    public FarmService(User farmOwner) {
        this.farmOwner = farmOwner;
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

    public void updateCurrentFarm(Farm farm){
        farmRepository.update(farm);
    }

    public OwnedSeeds addSeedsToFarmInventory(OwnedSeeds seeds){
        return farmSeedsRepository.save(seeds);
    }

    public ArrayList<OwnedSeeds> getSeedsInventory(Farm farm){
        return farmSeedsRepository.findAllByFarmId(farm.getFarmId());
    }

    public ArrayList<Harvest> getHarvestsInInventory(Farm farm){
        return harvestRepository.findAllByFarmId(farm.getFarmId());
    }

    public User getFarmOwner() {
        return farmOwner;
    }

    public void setFarmOwner(User farmOwner) {
        this.farmOwner = farmOwner;
    }

    public ArrayList<LandLot> getLandByFarmId(String farmId) {
        return landLotRepository.findAllByFarmId(farmId);
    }

    public LandLot addLandLotToFarm(LandLot landLot){
        return landLotRepository.save(landLot);
    }

    public OwnedSeeds getSeedsByLotName(String seedId, Farm farm) {
        ArrayList<OwnedSeeds> allSeedsfarmSeedsRepository = getSeedsInventory(farm);

        Optional<OwnedSeeds> targetSeedLot = allSeedsfarmSeedsRepository
                .stream()
                .filter(seed -> seed.getSeedsId().equals(seedId))
                .findFirst();
        return targetSeedLot.orElse(null);
    }

    public void updateOwnedSeeds(OwnedSeeds seeds){
        farmSeedsRepository.update(seeds);
    }
}
