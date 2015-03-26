package sudochef.inventory;

public enum Units {
    TEASPOON,
    TABLESPOON,
    FLUIDOUNCE,
    CUP,
    PINT,
    QUART,
    GALLON,
    LITER,
    POUND,
    OUNCE,

    METER;

    public static Units contains(String test) {

        for (Units c : Units.values()) {
            if (c.name().equals(test)) {
                return c;
            }
        }

        return null;
    }

}
