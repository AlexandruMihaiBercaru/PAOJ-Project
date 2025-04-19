package models;

public class Seed {

    private String scientificName;
    private String commonName;
    private PlantType type;
    private String cultivar;
    private String plantingPeriod;
    private String harvestingPeriod;
    private double pricePerUnit;
    private int availableQuantity;


    public Seed(String scientificName, String commonName, PlantType type, String cultivar,
                String plantingPeriod, String harvestingPeriod, double pricePerUnit, int availableQuantity) {
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.type = type;
        this.cultivar = cultivar;
        this.plantingPeriod = plantingPeriod;
        this.harvestingPeriod = harvestingPeriod;
        this.pricePerUnit = pricePerUnit;
        this.availableQuantity = availableQuantity;
    }


    public static Seed parseSeed(String line){
        String[] tokens = line.split(",");
        PlantType plant = PlantType.getPlantTypeValue(tokens[2].charAt(0));
        Seed seed = new Seed(tokens[0], tokens[1], plant, tokens[3], tokens[4],
                tokens[5], Double.parseDouble(tokens[6]), Integer.parseInt(tokens[7]));
        return seed;
    }
}
