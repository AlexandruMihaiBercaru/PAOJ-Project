package models;

import utils.RandomStringGenerator;

public class SeedsLot {

    private String lotId;
    private Crop cropType;
    private String plantingPeriod;
    private String harvestingPeriod;
    private double pricePerUnit;
    private SaleUnit saleUnit;
    private int plantsPerSqMeter;
    private int expectedYieldPerSqMeter;
    private int availableQuantity;




    public SeedsLot(Crop crop, String plantingPeriod,
                    String harvestingPeriod, double pricePerUnit,
                    String saleUnit, int plantsPerSqMeter,
                    int expectedYieldPerSqMeter, int availableQuantity) {

        this.cropType = crop;
        this.plantingPeriod = plantingPeriod;
        this.harvestingPeriod = harvestingPeriod;
        this.pricePerUnit = pricePerUnit;
        this.saleUnit = SaleUnit.fromCode(saleUnit);
        this.plantsPerSqMeter = plantsPerSqMeter;
        this.expectedYieldPerSqMeter = expectedYieldPerSqMeter;
        this.availableQuantity = availableQuantity;
    }


    // copy constructor
    public SeedsLot(SeedsLot oldLot, int availableQuantity) {
        //this.lotId = oldLot.getLotId();
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

//    public static SeedsLot parseSeed(String line){
//        String[] tokens = line.split(",");
//        PlantLifeCycle plant = PlantLifeCycle.getPlantTypeValue(tokens[2].charAt(0));
//        SeedsLot seedsLot = new SeedsLot(tokens[0], tokens[1], plant, tokens[3], tokens[4],
//                tokens[5], Double.parseDouble(tokens[6]), Integer.parseInt(tokens[7]), Integer.parseInt(tokens[8]), Integer.parseInt(tokens[9]));
//        return seedsLot;
//    }




    public void printInfo() {
        System.out.println(
                "================================================" +
                "LOT_ID: " + lotId + "\n" +
                "SCIENTIFIC NAME:" + this.cropType.getScientificName() + '\n' +
                "COMMON NAME: " + this.cropType.getCommonName() + '\n' +
                "CULTIVAR: " + this.cropType.getCultivar() + '\n' +
                "TYPE: " + this.cropType.getType().toString().toLowerCase() + "\n" +
                "PLANTING PERIOD: " + plantingPeriod + '\n' +
                "HARVESTING PERIOD: " + harvestingPeriod + '\n' +
                "PRICE PER UNIT: " + pricePerUnit + '\n' +
                "SALE UNIT: " + saleUnit + '\n' +
                "PLANTS PER m^2 (recommended): " + plantsPerSqMeter + '\n' +
                "EXPECTED YIELD PER m^2: " + expectedYieldPerSqMeter + '\n' +
                "AVAILABLE QUANTITY: " + availableQuantity+ '\n'
        );
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public void setLotId(){
        this.lotId = RandomStringGenerator.newString(10);
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

    public Crop getCrop() {
        return cropType;
    }

    public SaleUnit getSaleUnit(){
        return saleUnit;
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
