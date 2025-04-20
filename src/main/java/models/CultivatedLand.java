package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CultivatedLand extends LandParcel{

    private SeedsLot seedsUsed;
    private Date plantingDate;

    public CultivatedLand(String parcelId, double size, String usage) {
        super(parcelId, size, usage);
    }


    public void setSeedsUsed(SeedsLot lot) {
        this.seedsUsed = lot;
    }

    public void setPlantingDate(Date plantingDate) {
        this.plantingDate = plantingDate;
    }

    @Override
    public String toString() {
        String currentCrop = seedsUsed == null ? null : this.seedsUsed.getCropType().getCommonName();
        String pattern = "dd/mm/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return super.toString() +
                "TYPE OF LAND: " + this.getClass().getSimpleName() + "\n" +
                "CURRENT CROP: " + (currentCrop == null ? "none" : currentCrop)  + "\n" +
                "DATE PLANTED: " + (plantingDate == null ? "none" : df.format(plantingDate))
                ;
    }
}
