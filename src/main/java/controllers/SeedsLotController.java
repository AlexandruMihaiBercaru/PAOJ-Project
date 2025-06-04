package controllers;

import services.SeedsLotService;

public class SeedsLotController {

    private final SeedsLotService seedsLotService;

    public SeedsLotController() {
        seedsLotService = new SeedsLotService();
    }

    public void printSeedLotsListedForSale(){
        seedsLotService.inspectSeedMarket();
    }
}
