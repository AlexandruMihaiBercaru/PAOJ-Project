package models;

import java.util.ArrayList;

public class SeedInventory extends Inventory {

    private ArrayList<SeedsLot> ownedSeeds;

    public SeedInventory(){
        ownedSeeds = new ArrayList<>();
    }

    @Override
    public void listInventory() {
        for(var seeds: ownedSeeds){
            System.out.println(seeds);
            System.out.println("-----------------");
        }
    }


    public ArrayList<SeedsLot> getOwnedSeeds() {
        return ownedSeeds;
    }

    public void setOwnedSeeds(ArrayList<SeedsLot> ownedSeeds) {
        this.ownedSeeds = ownedSeeds;
    }


}
