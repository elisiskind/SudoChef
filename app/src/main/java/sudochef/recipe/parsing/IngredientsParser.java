package sudochef.recipe.parsing;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import sudochef.inventory.Product;

/**
 * Created by eli on 3/30/2015.
 */
public class IngredientsParser {

    private String TAG = "SC.IngredientsParser";
    private JSONArray ingredientsArray;
    private ArrayList<Product> products;

    public IngredientsParser (String ingredientsJSON) {
        try {
            ingredientsArray = new JSONArray(ingredientsJSON);
        } catch (Exception e) {
            Log.e(TAG, "JSON object creation failed");
            return;
        }

        products = new ArrayList<>(ingredientsArray.length());
    }

    public void parse() throws JSONException {
        for(int i = 0; i < ingredientsArray.length(); i++) {
            ProductParser parser = new ProductParser(ingredientsArray.getString(i));
            Log.v(TAG, ingredientsArray.getString(i));
            products.add(i, parser.parse());
        }
    }
}
