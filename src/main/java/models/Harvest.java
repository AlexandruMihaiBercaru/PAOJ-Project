package models;
import java.util.Date;

public class Harvest {
    private Crop harvestedCrop;
    private Date harvestedDate;
    private int yieldedQuantity;

    public Harvest(Crop harvestedCrop, Date harvestedDate, int yieldedQuantity) {
        this.harvestedCrop = harvestedCrop;
        this.harvestedDate = harvestedDate;
        this.yieldedQuantity = yieldedQuantity;
    }
}
