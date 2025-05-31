package models;

public class Crop {

    private String scientificName;
    private String cultivar;
    private String commonName;
    private PlantLifeCycle type;


    public Crop(String scientificName, String cultivar, String commonName, PlantLifeCycle type) {
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

    public PlantLifeCycle getType() {
        return type;
    }


}
