package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArableLand extends LandLot {

    private OwnedSeeds seedsUsed;
    private Date plantingDate;
    private boolean isCurrentlyCultivated;

    public ArableLand(String landName, double area, String usage, String farmId, OwnedSeeds seedsUsed, Date plantingDate, boolean isCurrentlyCultivated) {
        super(landName, area, usage, farmId);
        this.seedsUsed = seedsUsed;
        this.plantingDate = plantingDate;
        this.isCurrentlyCultivated = isCurrentlyCultivated;
    }


    public void setSeedsUsed(OwnedSeeds lot) {
        this.seedsUsed = lot;
    }

    public void setPlantingDate(Date plantingDate) {
        this.plantingDate = plantingDate;
    }

    public OwnedSeeds getSeedsUsed() {
        return seedsUsed;
    }

    public Date getPlantingDate() {
        return plantingDate;
    }

    public boolean isCurrentlyCultivated() {
        return isCurrentlyCultivated;
    }

    public void setCurrentlyCultivated(boolean currentlyCultivated) {
        isCurrentlyCultivated = currentlyCultivated;
    }

    @Override
    public String toString() {
        String currentCrop = this.seedsUsed == null ? null : this.seedsUsed.getLotOfOrigin().getCropType().getCommonName();
        String pattern = "dd/mm/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return super.toString() +
                "TYPE OF LAND: " + this.getClass().getSimpleName() + "\n" +
                "CURRENT CROP: " + (currentCrop == null ? "none" : currentCrop)  + "\n" +
                "DATE PLANTED: " + (plantingDate == null ? "none" : df.format(plantingDate)) + "\n"
                ;
    }
}
