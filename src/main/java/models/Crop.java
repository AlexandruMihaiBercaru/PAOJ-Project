package models;

public class Crop {

    private String scientificName;
    private String cultivar;
    private String commonName;
    private PlantType type;


    public Crop(String scientificName, String cultivar, String commonName, PlantType type) {
        this.scientificName = scientificName;
        this.cultivar = cultivar;
        this.commonName = commonName;
        this.type = type;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getCultivar() {
        return cultivar;
    }

    public String getCommonName() {
        return commonName;
    }

    public PlantType getType() {
        return type;
    }


}
