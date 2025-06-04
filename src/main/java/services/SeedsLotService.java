package services;

import exceptions.EntityNotFoundException;
import models.Crop;
import models.SeedsLot;
import persistence.CropRepository;
import persistence.SeedsLotRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SeedsLotService {

    private SeedsLotRepository seedsLotRepository = SeedsLotRepository.getInstance();
    private CropRepository cropRepository = CropRepository.getInstance();

    private List<SeedsLot> seedsForSale = new ArrayList<>();

    public SeedsLotService(){
        seedsForSale = seedsLotRepository.findAll();
    }

    public ArrayList<SeedsLot> getAllSeedsForSale(){
        return (ArrayList<SeedsLot>) seedsLotRepository.findAll();
    }

    public SeedsLot getLotById(String id){
        return seedsLotRepository.findById(id).orElseThrow(()
        -> new EntityNotFoundException("No lot was found...!"));
    }

    public void printCropNames() {
        cropRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Crop::getCommonName))
                .map(crop -> "--> " + crop.getCommonName())
                .distinct()
                .forEach(System.out::println);
    }

    public void inspectSeedMarket(){
        seedsForSale.forEach(SeedsLot::printInfo);
    }

    public void updateLot(SeedsLot seedsLot){
        seedsLotRepository.update(seedsLot);
    }

    public CropRepository getCropRepository() {
        return cropRepository;
    }

}
