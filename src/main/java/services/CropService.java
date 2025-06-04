package services;

import models.Crop;
import persistence.CropRepository;

import java.util.Optional;

public class CropService {

    private CropRepository cropRepository = CropRepository.getInstance();

    public CropService() {}

    public Crop getCropWithCultivarOrInsertNew(String cropName, String cultivar) {
        Optional<Crop> crop = cropRepository.findAll()
                .stream()
                .filter(c -> c.getCommonName().equals(cropName) && c.getCultivar().equals(cultivar))
                .findFirst();

        if (crop.isPresent()){
            return crop.get();
        }
        else{
            Crop newCrop = new Crop(null, cultivar, cropName, "perennial");
            newCrop.setCropId();

            Object result = cropRepository.save(newCrop);
            return (Crop) result;
        }
    }
}
