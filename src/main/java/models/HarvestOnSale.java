package models;

import java.util.Date;

public class HarvestOnSale extends Harvest{

    private int quantityOnSale;
    private int quantitySold;
    private double salePrice;

    public HarvestOnSale(LandLot harvestedLand, OwnedSeeds seedsUsed, Date harvestDate, int yieldedQuantity, String farmId) {
        super(harvestedLand, seedsUsed, harvestDate, yieldedQuantity, farmId);
        setOnSale(true);

    }

    public int getQuantityOnSale() {
        return quantityOnSale;
    }

    public void setQuantityOnSale(int quantityOnSale) {
        this.quantityOnSale = quantityOnSale;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }


    @Override
    public String toString() {
        return  super.toString() +
                "\nAVAILABLE: " + (quantityOnSale - quantitySold) + "KG" +
                "\nPRICE: " + salePrice
                ;
    }
}
