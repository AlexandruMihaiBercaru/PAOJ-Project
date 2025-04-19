package models;

public class Crop {

    private String Name;
    private Seed usedSeed;
    private int expectedYieldPerUnit; // kg per hectar

    public Crop(String Name, Seed usedSeed, int expectedYieldPerUnit) {
        this.Name = Name;
        this.usedSeed = usedSeed;
        this.expectedYieldPerUnit = expectedYieldPerUnit;
    }

    public Seed getUsedSeed() {
        return usedSeed;
    }

    public String getName() {
        return Name;
    }

    public int getExpectedYieldPerUnit() {
        return expectedYieldPerUnit;
    }
}
