package models;

import java.util.Date;

public class CultivatedLand extends LandParcel{

    private Crop crop;
    private Date plantingDate;

    public CultivatedLand(String parcelId, double size, String usage) {
        super(parcelId, size, usage);
    }

}
