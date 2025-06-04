package models;

import java.util.Arrays;

public enum SaleUnit {
    PIECES("pcs"), KILOGRAM("kg");

    private final String literal;

    SaleUnit(String literal){
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static SaleUnit fromCode(String code){
        return Arrays.stream(values())
                .filter(u -> u.literal.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);

    }
}
