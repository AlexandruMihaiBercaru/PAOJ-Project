package models;

import java.util.ArrayList;

public class SeedInventory extends Inventory {

    private ArrayList<OwnedSeeds> ownedSeeds;

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


    public ArrayList<OwnedSeeds> getOwnedSeeds() {
        return ownedSeeds;
    }

    public void setOwnedSeeds(ArrayList<OwnedSeeds> ownedSeeds) {
        this.ownedSeeds = ownedSeeds;
    }


}
