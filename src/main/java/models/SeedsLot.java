package models;

public class SeedsLot {

    private int lotId;
    private Crop cropType;
    private String plantingPeriod;
    private String harvestingPeriod;
    private double pricePerUnit;
    private int plantsPerSqMeter;
    private int expectedYieldPerSqMeter;
    private int availableQuantity;

    private static int internalId = 1;


    public SeedsLot(String scientificName, String commonName, PlantType type, String cultivar,
                    String plantingPeriod, String harvestingPeriod, double pricePerUnit, int plantsPerSqMeter, int expectedYieldPerSqMeter, int availableQuantity) {
        this.lotId = internalId++;
        this.cropType = new Crop(scientificName, cultivar, commonName, type);
        this.plantingPeriod = plantingPeriod;
        this.harvestingPeriod = harvestingPeriod;
        this.pricePerUnit = pricePerUnit;
        this.plantsPerSqMeter = plantsPerSqMeter;
        this.expectedYieldPerSqMeter = expectedYieldPerSqMeter;
        this.availableQuantity = availableQuantity;
    }

    // copy constructor
    public SeedsLot(SeedsLot oldLot, int availableQuantity) {
        this.lotId = oldLot.getLotId();
        this.cropType = oldLot.getCropType();
        this.plantingPeriod = oldLot.getPlantingPeriod();
        this.harvestingPeriod = oldLot.getHarvestingPeriod();
        this.pricePerUnit = oldLot.getPricePerUnit();
        this.plantsPerSqMeter = oldLot.getPlantsPerSqMeter();
        this.expectedYieldPerSqMeter = oldLot.getExpectedYieldPerSqMeter();
        this.availableQuantity = availableQuantity;
    }

    public Crop getCropType() {
        return cropType;
    }

    public static SeedsLot parseSeed(String line){
        String[] tokens = line.split(",");
        PlantType plant = PlantType.getPlantTypeValue(tokens[2].charAt(0));
        SeedsLot seedsLot = new SeedsLot(tokens[0], tokens[1], plant, tokens[3], tokens[4],
                tokens[5], Double.parseDouble(tokens[6]), Integer.parseInt(tokens[7]), Integer.parseInt(tokens[8]), Integer.parseInt(tokens[9]));
        return seedsLot;
    }


    public void printInfo() {
        System.out.println(
                "LOTID: " + lotId + "\n" +
                "SCIENTIFIC NAME:" + this.cropType.getScientificName() + '\n' +
                "COMMON NAME: " + this.cropType.getCommonName() + '\n' +
                "CULTIVAR: " + this.cropType.getCultivar() + '\n' +
                "TYPE: " + this.cropType.getType().toString().toLowerCase() + "\n" +
                "PLANTING PERIOD: " + plantingPeriod + '\n' +
                "HARVESTING PERIOD: " + harvestingPeriod + '\n' +
                "PRICE PER UNIT: " + pricePerUnit + '\n' +
                "PLANTS PER m^2 (recommended): " + plantsPerSqMeter + '\n' +
                "EXPECTED YIELD PER m^2: " + expectedYieldPerSqMeter + '\n' +
                "AVAILABLE QUANTITY: " + availableQuantity+ '\n' +
                "----------------------------------------"
        );
    }

    public int getLotId() {
        return lotId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public String getPlantingPeriod() {
        return plantingPeriod;
    }

    public String getHarvestingPeriod() {
        return harvestingPeriod;
    }

    public int getPlantsPerSqMeter() {
        return plantsPerSqMeter;
    }

    public int getExpectedYieldPerSqMeter() {
        return expectedYieldPerSqMeter;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return
                "LOT OF PROVENANCE: " + lotId + "\n" +
                "SCIENTIFIC NAME:" + this.cropType.getScientificName() + '\n' +
                "COMMON NAME: " + this.cropType.getCommonName() + '\n' +
                "CULTIVAR: " + this.cropType.getCultivar() + '\n' +
                "STORED QUANTITY: " + availableQuantity+ '\n'
                ;
    }
}
