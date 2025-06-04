package models;
import utils.RandomStringGenerator;

import java.time.LocalDate;

public class OwnedSeeds {

    private SeedsLot lotOfOrigin;
    private String seeds_id;
    private LocalDate buyingDate;
    private int quantityBought;
    private int quantityAvailable;
    private String farmId;

    public OwnedSeeds(SeedsLot lotOfOrigin, LocalDate buyingDate, int quantityBought, int quantityAvailable, String farmId) {
        this.lotOfOrigin = lotOfOrigin;
        this.buyingDate = buyingDate;
        this.quantityBought = quantityBought;
        this.quantityAvailable = quantityAvailable;
        this.farmId = farmId;
    }

    public SeedsLot getLotOfOrigin() {
        return lotOfOrigin;
    }

    public void setLotOfOrigin(SeedsLot lotOfOrigin) {
        this.lotOfOrigin = lotOfOrigin;
    }

    public LocalDate getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(LocalDate buyingDate) {
        this.buyingDate = buyingDate;
    }

    public int getQuantityBought() {
        return quantityBought;
    }

    public void setQuantityBought(int quantityBought) {
        this.quantityBought = quantityBought;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public void setSeedsId() {
        this.seeds_id = RandomStringGenerator.newString(10);
    }

    public void setSeedsId(String seedsId){
        this.seeds_id = seedsId;
    }

    public String getSeedsId() {
        return seeds_id;
    }

    public String getFarmId() {
        return farmId;
    }

    @Override
    public String toString() {
        return "==================================\n" +
                "\nSeed ID: " + seeds_id + "\n" +
                "\nCrop type: " + lotOfOrigin.getCrop().getCommonName() + " " + lotOfOrigin.getCropType().getCultivar() +
                "\nLot ID: " + lotOfOrigin.getLotId() +
                "\nBought on: " + buyingDate +
                "\nAvailable: " + quantityAvailable + " " + lotOfOrigin.getSaleUnit().getLiteral()
                ;
    }
}
