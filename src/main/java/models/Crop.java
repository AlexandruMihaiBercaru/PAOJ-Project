package models;

import utils.RandomStringGenerator;

public class Crop {

    private String cropId;
    private String scientificName;
    private String cultivar;
    private String commonName;
    private PlantLifeCycle type;


    public Crop(String scientificName, String cultivar, String commonName, String type) {
        this.scientificName = scientificName;
        this.cultivar = cultivar;
        this.commonName = commonName;
        this.type = PlantLifeCycle.getPlantTypeValue(type);
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

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public void setCropId() {
        this.cropId = RandomStringGenerator.newString(6);
    }

    @Override
    public String toString() {
        return "Crop{" +
                "cropId='" + cropId + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", cultivar='" + cultivar + '\'' +
                ", commonName='" + commonName + '\'' +
                ", type=" + type +
                '}';
    }
}
