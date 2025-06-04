package models;

import java.util.ArrayList;

public class Orchard extends LandLot {

    private Crop crop;
    private int age;

    public Orchard(String landName, double area, String usage, String farmId, Crop crop, int age) {
        super(landName, area, usage, farmId);
        this.crop = crop;
        this.age = age;
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nTYPE OF LAND: " + this.getClass().getSimpleName() +
                "\nFRUITS GROWN: " + crop.getCommonName() ;
    }
}
