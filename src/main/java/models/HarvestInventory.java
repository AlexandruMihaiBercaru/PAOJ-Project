package models;

import java.util.ArrayList;
import java.util.Comparator;

public class HarvestInventory extends Inventory {

    private ArrayList<Harvest> harvests;


    @Override
    public void listInventory() {
        for(var harvest: harvests){
            System.out.println(harvest);
        }
    }

    public ArrayList<Harvest> getHarvests() {
        return harvests;
    }

    public void setHarvests(ArrayList<Harvest> harvests) {
        harvests.sort(Comparator.comparing(Harvest::getHarvestDate).
                thenComparing(Harvest::getYieldedQuantity));
        this.harvests = harvests;
    }
}
