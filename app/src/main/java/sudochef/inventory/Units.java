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
    GRAM,

    METER;

    public static Units contains(String test) {

        for (Units c : Units.values()) {
            if (c.name().equals(test)) {
                return c;
            }
        }

        return null;
    }

    public static double convert(Units from, Units to, double amt)
    {
        double result = amt;
        switch (from) {
            case TEASPOON:
                switch (to){
                    case GALLON:
                        result = result / 4;
                    case QUART:
                        result = result / 2;
                    case PINT:
                        result = result / 2;
                    case CUP:
                        result = result / 8;
                    case FLUIDOUNCE:
                        result = result / 2;
                    case TABLESPOON:
                        result = result / 3;
                    case TEASPOON:
                        result = result;
                        break;
                    case LITER:
                        result = result * .00492892;
                        break;
//                    case POUND:
//                    case OUNCE:
                }
                break;
            case TABLESPOON:
                switch (to){
                    case GALLON:
                        result = result / 4;
                    case QUART:
                        result = result / 2;
                    case PINT:
                        result = result / 2;
                    case CUP:
                        result = result / 8;
                    case FLUIDOUNCE:
                        result = result / 2;
                    case TABLESPOON:
                        result = result;
                        break;
                    case TEASPOON:
                        result = result * 3;
                        break;
                    case LITER:
                    case POUND:
                    case OUNCE:
                }
                break;
            case FLUIDOUNCE:
                switch (to){
                    case GALLON:
                        result = result / 4;
                    case QUART:
                        result = result / 2;
                    case PINT:
                        result = result / 2;
                    case CUP:
                        result = result / 8;
                    case FLUIDOUNCE:
                        result = result;
                        break;
                    case TEASPOON:
                        result = result * 3;
                    case TABLESPOON:
                        result = result * 2;
                        break;
                    case LITER:
                    case POUND:
                    case OUNCE:
                }
                break;
            case CUP:
                switch (to){
                    case GALLON:
                        result = result / 4;
                    case QUART:
                        result = result / 2;
                    case PINT:
                        result = result / 2;
                    case CUP:
                        result = result;
                        break;
                    case TEASPOON:
                        result = result * 3;
                    case TABLESPOON:
                        result = result * 2;
                    case FLUIDOUNCE:
                        result = result * 8;
                        break;
                    case LITER:
                    case POUND:
                    case OUNCE:
                }
                break;
            case PINT:
                switch (to){
                    case GALLON:
                        result = result / 4;
                    case QUART:
                        result = result / 2;
                    case PINT:
                        result = result;
                        break;
                    case TEASPOON:
                        result = result * 3;
                    case TABLESPOON:
                        result = result * 2;
                    case FLUIDOUNCE:
                        result = result * 8;
                    case CUP:
                        result = result * 2;
                        break;
                    case LITER:
                    case POUND:
                    case OUNCE:
                }
                break;
            case QUART:
                switch (to){
                    case GALLON:
                        result = result / 4;
                    case QUART:
                        result = result;
                        break;
                    case TEASPOON:
                        result = result * 3;
                    case TABLESPOON:
                        result = result * 2;
                    case FLUIDOUNCE:
                        result = result * 8;
                    case CUP:
                        result = result * 2;
                    case PINT:
                        result = result * 2;
                        break;
                    case LITER:
                    case POUND:
                    case OUNCE:
                }
                break;
            case GALLON:
                switch (to){
                    case GALLON:
                        result = result;
                        break;
                    case TEASPOON:
                        result = result * 3;
                    case TABLESPOON:
                        result = result * 2;
                    case FLUIDOUNCE:
                        result = result * 8;
                    case CUP:
                        result = result * 2;
                    case PINT:
                        result = result * 2;
                    case QUART:
                        result = result * 4;
                        break;
                    case LITER:
                    case POUND:
                    case OUNCE:
                }
                break;
            case LITER:
                switch (to){
                    case TEASPOON:
                    case TABLESPOON:
                    case FLUIDOUNCE:
                    case CUP:
                    case PINT:
                    case QUART:
                    case GALLON:
                    case LITER:
                        result = amt;
                        break;
                    case POUND:
                    case OUNCE:
                }
                break;
            case POUND:
                switch (to){
                    case TEASPOON:
                    case TABLESPOON:
                    case FLUIDOUNCE:
                    case CUP:
                    case PINT:
                    case QUART:
                    case GALLON:
                    case LITER:
                    case POUND:
                        result = amt;
                        break;
                    case OUNCE:
                }
                break;
            case OUNCE:
                switch (to){
                    case TEASPOON:
                    case TABLESPOON:
                    case FLUIDOUNCE:
                    case CUP:
                    case PINT:
                    case QUART:
                    case GALLON:
                    case LITER:
                    case POUND:
                    case OUNCE:
                        result = amt;
                        break;
                }
                break;
            default:
                result = -1;

        }

        return result;
    }

}
