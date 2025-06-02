package controllers;

import models.Farm;
import services.FarmService;

import java.util.ArrayList;
import java.util.Comparator;

public class FarmController {

    private final FarmService farmService = new FarmService();

    public FarmController() {}

    public void listAllFarmsContactInformation(){
        ArrayList<Farm> allFarms = farmService.getFarms();
        allFarms.stream()
                .sorted(Comparator.comparing(Farm::getFarmName))
                .forEach(System.out::println);
    }
}
