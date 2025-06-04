package models;
import utils.RandomStringGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Harvest {
    private String harvestId;
    private LandLot harvestedLand;
    private OwnedSeeds seedsUsed;
    protected Date harvestDate;
    protected int yieldedQuantity;
    protected boolean onSale;
    protected String farmId;

    public Harvest(LandLot harvestedLand, OwnedSeeds seedsUsed, Date harvestDate, int yieldedQuantity, String farmId) {
        this.harvestedLand = harvestedLand;
        this.seedsUsed = seedsUsed;
        this.harvestDate = harvestDate;
        this.yieldedQuantity = yieldedQuantity;
        this.farmId = farmId;
        this.onSale = false;
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }

    public void setHarvestId(){
        this.harvestId = RandomStringGenerator.newString(10);
    }

    public LandLot getHarvestedLand() {
        return harvestedLand;
    }

    public void setHarvestedLand(LandLot harvestedLand) {
        this.harvestedLand = harvestedLand;
    }

    public OwnedSeeds getSeedsUsed() {
        return seedsUsed;
    }

    public void setSeedsUsed(OwnedSeeds seedsUsed) {
        this.seedsUsed = seedsUsed;
    }

    public Date getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(Date harvestDate) {
        this.harvestDate = harvestDate;
    }

    public int getYieldedQuantity() {
        return yieldedQuantity;
    }

    public void setYieldedQuantity(int yieldedQuantity) {
        this.yieldedQuantity = yieldedQuantity;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean on_sale) {
        this.onSale = on_sale;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    @Override
    public String toString() {
        String pattern = "dd/mm/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Crop harvestedCrop = null;
        if(seedsUsed == null || harvestedLand instanceof Orchard){
            harvestedCrop = ((Orchard)harvestedLand).getCrop();
        }
        else {
            harvestedCrop = seedsUsed.getLotOfOrigin().getCrop();
        }
        return "==================================" +
                "\nCROP: " + harvestedCrop.getCommonName() + " " + harvestedCrop.getCultivar() +
                "\nDATE HARVESTED: " + df.format(harvestDate) +
                "\nYIELDED QUANTITY" + yieldedQuantity
                ;
    }
}
