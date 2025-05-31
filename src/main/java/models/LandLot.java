package models;

public class LandLot {

    private String parcelId;
    private double area;
    private String usage;

    public LandLot(String parcelId, double area, String usage) {
        this.parcelId = parcelId;
        this.area = area;
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "ID: " + parcelId + "\nSIZE: " + area + "\n";
    }
}
