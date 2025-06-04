package models;

public enum PlantLifeCycle {
    ANNUAL("annual"), BIENNIAL("biennial"), PERRENNIAL("perennial");

    private final String literal;

    PlantLifeCycle(String literal){
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static PlantLifeCycle getPlantTypeValue(String wd){
        for(PlantLifeCycle plantType : PlantLifeCycle.values()){
            if(plantType.getLiteral().equals(wd)){
                return plantType;
            }
        }
        return null;
    }

}
