package services;

import models.ArableLand;
import models.LandLot;
import persistence.LandLotRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LandService {
    private LandLotRepository landLotRepository = LandLotRepository.getInstance();

    public LandService() {}

    public LandLot getLandLotByName(String farmId, String landName) {
        ArrayList<LandLot> lots = landLotRepository.findAllByFarmId(farmId);
        Optional<LandLot> myLot = lots.stream()
                .filter(l -> l.getLandName().equals(landName))
                .findFirst();
        return myLot.orElse(null);
    }

    public void UpdateLandLot(LandLot landLot) {
        landLotRepository.update(landLot);
    }

    public List<LandLot> getCultivatedLandLots(String farmId){
        return landLotRepository.findAllByFarmId(farmId)
                .stream()
                .filter(l -> l instanceof ArableLand && ((ArableLand)l).isCurrentlyCultivated())
                .collect(Collectors.toList());

    }
}
