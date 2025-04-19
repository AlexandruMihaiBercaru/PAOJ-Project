package models;

public class SeedInventory extends Inventory {

    private Seed[] seedLots;


    @Override
    void listInventory() {
        for(var seeds: seedLots){
            System.out.println(seeds);
        }
    }


}
