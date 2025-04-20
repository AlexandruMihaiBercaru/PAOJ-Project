package models;

public class LandParcel {

    private String parcelId;
    private double size;
    private String usage;

    public LandParcel(String parcelId, double size, String usage) {
        this.parcelId = parcelId;
        this.size = size;
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "ID: " + parcelId + "\nSIZE: " + size + "\n";
    }
}
