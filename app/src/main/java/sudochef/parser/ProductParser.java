package sudochef.parser;

import android.util.Log;

import org.json.JSONException;

import sudochef.inventory.Product;
import sudochef.inventory.Units;

/**
 * Created by eli on 3/30/2015.
 */
public class ProductParser {

    private String TAG = "SC.ProductParser";
    private String ingredientString;
    private double amt;
    private Units unit;
    private String name;

    public ProductParser (String ingredientsString) {
        this.ingredientString = ingredientsString;
    }

    public Product parse() throws JSONException {
            amt = extractAmount();
            unit = extractUnit();
            name = extractName();

            Log.v(TAG, amt + " - " + unit + " - " + name);

            return new Product(name, amt, unit, null);
    }

    /**
     * Extracts the portion of the string representing the amount, and removes that portion of the string from ingredientString
     * @return a double representing the amount
     */
    private double extractAmount() {
        StringBuilder amtStringBuilder = new StringBuilder();
        String amtString;
        int i = -1;
        int index = 0;
        char c;

        if(ingredientString.toLowerCase().startsWith("a ")) {
            ingredientString = ingredientString.substring(2, ingredientString.length());
            return 1;
        } else if (ingredientString.toLowerCase().startsWith("an ")) {
            ingredientString = ingredientString.substring(3, ingredientString.length());
            return 1;
        }

        while(++i < ingredientString.length()) {
            c = ingredientString.charAt(i);
            if(Character.isDigit(c) || Character.isSpaceChar(c) || c == '/') {
                amtStringBuilder.append(c);
            } else {
                break;
            }
        }

        amtString = amtStringBuilder.toString();
        ingredientString = ingredientString.substring(amtString.length(), ingredientString.length()); // Trim amount section from the string
        amtString = amtString.trim(); // Trim leading or trailing whitespace
        amtStringBuilder = new StringBuilder();

        if(amtString.isEmpty()) return 0;

        if(!amtString.contains("/")) {
            return Double.parseDouble(amtString);
        } else {
            double numparts[] = {0, 0, 0}; // 0 - integer part, 1 - numerator, 2 - denominator
            if(!amtString.contains(" ")) index++;
            i = -1;
            while (++i < amtString.length()) {
                c = amtString.charAt(i);
                if (Character.isDigit(c)) {
                    amtStringBuilder.append(c);
                } else {
                    numparts[index++] = Double.parseDouble(amtStringBuilder.toString());
                    amtStringBuilder = new StringBuilder();
                }
            }
            numparts[index++] = Double.parseDouble(amtStringBuilder.toString());
            return numparts[0] + (numparts[1] / numparts[2]);
        }
    }


    private Units extractUnit() {
        String unitString;
        Units unit;
        int splitPoint = ingredientString.indexOf(' ');
        if (splitPoint > -1) { // Check if there is more than one word.
            unitString = ingredientString.substring(0, splitPoint); // Extract first word.
        } else {
            return null; // only one word
        }

        if(unitString.contains("cup")) {
            unit = Units.CUP;
        } else if(unitString.toLowerCase().contains("teaspoon") || unitString.contains("tsp") || unitString.contains("ts")) {
            unit = Units.TEASPOON;
        } else if(unitString.toLowerCase().contains("tablespoon") || unitString.contains("tb") || unitString.contains("T")) { // Note: capital T is reserved for tablespoon abbrevs like T, Tbsp, Tb
            unit = Units.TABLESPOON;
        } else if(unitString.toLowerCase().contains("oz") || unitString.toLowerCase().contains("ounce")) {
            unit = Units.OUNCE; //TODO: differentiate between oz and fl oz
        } else if(unitString.startsWith("gram")) {
            unit = Units.GRAM;
        } else if(unitString.toLowerCase().contains("pinch") || unitString.contains("dash") || unitString.contains("touch")) {
            unit = Units.TEASPOON;
            amt /= 8; // A pinch is now 1/8 teaspoon
        } else if(unitString.toLowerCase().startsWith("stick")) {
            unit = Units.STICK;
        } else if(unitString.toLowerCase().startsWith("clove")) {
            unit = Units.CLOVE;
        } else if(unitString.toLowerCase().startsWith("head")) {
            unit = Units.HEAD;
        } else if(unitString.toLowerCase().startsWith("can")) {
            unit = Units.CAN;
        } else {
            unit = Units.UNIT;
        }

        if(unit != Units.UNIT) {
            ingredientString = ingredientString.substring(splitPoint + 1, ingredientString.length());
        }

        return unit;
    }

    private String extractName() {

        // Trim, remove word 'of' from beginning
        ingredientString = ingredientString.trim();
        if(ingredientString.startsWith("of ")) {
            ingredientString = ingredientString.substring(3, ingredientString.length());
        }

        // Some easy and common cases
        if(unit == Units.CLOVE && ingredientString.toLowerCase().contains("garlic")) {
            return "garlic";
        } else if (unit == Units.STICK && ingredientString.contains("butter")) {
            return "butter";
        } else if (unit == Units.UNIT && ingredientString.contains("egg")) {
            return "egg";
        } else if (unit == Units.UNIT && ingredientString.contains("onion")) {
            if(ingredientString.contains("red")) {
                return "red onion";
            } else {
                return "onion";
            }
        }

        // We couldn't find anything oh well
        return ingredientString;

    };

}
