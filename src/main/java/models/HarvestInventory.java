package models;

import java.util.ArrayList;

public class HarvestInventory extends Inventory {

    private ArrayList<Harvest> harvests;


    @Override
    public void listInventory() {
        for(var harvest: harvests){
            System.out.println(harvest);
        }
    }
}
