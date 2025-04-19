package models;

public enum PlantType {
    ANUAL('A'), BIENNIAL('B'), PERRENNIAL('P');

    private char symbol;

    PlantType(char symbol){
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static PlantType getPlantTypeValue(char sym){
        for(PlantType pt : PlantType.values()){
            if(pt.getSymbol() == sym){
                return pt;
            }
        }
        return null;
    }

}
