package models;

public class HarvestInventory extends Inventory {

    private Harvest[] harvests;


    @Override
    void listInventory() {
        for(var harvest: harvests){
            System.out.println(harvest);
        }
    }
}
