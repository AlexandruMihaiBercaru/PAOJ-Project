package models;

import utils.RandomStringGenerator;

public class LandLot {

    private String landId;
    protected String landName;
    private double area;
    private String landUsage;
    protected String farmId;

    public LandLot(String landName, double area, String usage, String farmId) {
        this.landName = landName;
        this.area = area;
        this.landUsage = usage;
        this.farmId = farmId;
    }

    public String getLandId() {
        return landId;
    }

    public void setLandId(String landId) {
        this.landId = landId;
    }

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getLandUsage() {
        return landUsage;
    }

    public void setLandUsage(String landUsage) {
        this.landUsage = landUsage;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public void setLandId(){
        this.landId = RandomStringGenerator.newString(10);
    }


    @Override
    public String toString() {
        return  "===================================\n" +
                "ID: " + landName + "\nSIZE: " + area + "\n";
    }
}
