package models;

public enum PlantLifeCycle {
    ANUAL('A'), BIENNIAL('B'), PERRENNIAL('P');

    private char symbol;

    PlantLifeCycle(char symbol){
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static PlantLifeCycle getPlantTypeValue(char sym){
        for(PlantLifeCycle pt : PlantLifeCycle.values()){
            if(pt.getSymbol() == sym){
                return pt;
            }
        }
        return null;
    }

}
