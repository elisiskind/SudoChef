package sudochef.recipe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import sdp.sudochef.R;
import sudochef.guide.GuideActivity;
import sudochef.inventory.Product;
import sudochef.inventory.shopping.ShoppingListDatabase;
import sudochef.inventory.shopping.ShoppingProduct;
import sudochef.recipe.database.RecipeDatabase;
import sudochef.recipe.parsing.HTMLParser;
import sudochef.recipe.parsing.IngredientsParser;
import sudochef.search.HTTPGet;
import sudochef.search.yummly.Config;

/**
 * @author Eli Siskind
 *
 * This class is the activity called when a recipe is chosen. During this activity the recipe is fetched from Yummly and parsed.
 */
public class ChooseRecipeActivity extends Activity {

    private String recipeId;
    private HTMLParser parser;
    private ProgressDialog progressDialog;
    private String TAG = "SC.Choose";
    private boolean saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_recipe);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeId = extras.getString("recipeId");
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading. Please wait...");
            progressDialog.setCancelable(false);
            fetch(recipeId);
        } else {
            // Something went wrong
            Log.e(TAG, "No recipe id found");
        }

        saved = new RecipeDatabase(this).contains(recipeId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        saved = new RecipeDatabase(this).contains(recipeId);
    }

    /**
     * Fetches the recipe JSON from Yummly
     * @param id The unique id of the recipe to be fetched
     */
    private void fetch(String id) {
        String url = Config.YUMMLY_GET_RECIPE_BASE_URL + id +"?_app_id="
                + Config.APP_ID + "&_app_key=" + Config.APP_KEY;
        progressDialog.show();
        new AsyncRecipeFetch().execute(url);
    }

    /**
     * Starts the asynchronous html parsing
     * @param recipe The JSON of the recipe
     * @throws Exception
     */
    private void parseHTML(String recipe) throws Exception {
        Log.d(TAG, "HTTP GET returned string: " + recipe);
        new AsyncRecipeParse().execute(recipe);


    }

    /**
     * Displays a preview of the steps of the recipe.
     */
    private void display(){
        List<String> steps = parser.getSteps();
        LinearLayout root = (LinearLayout) findViewById(R.id.choose_recipe_layout);

        // Display each step separately by adding views to the root layout object
        for(String step : steps) {
            TextView view = new TextView(root.getContext());
            view.setText(step);
            // Change the background to gray
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
            view.setPadding(10, 10, 10, 10);
            view.setBackground(getResources().getDrawable(R.drawable.list_item));
            view.setTextSize(18);
            root.addView(view);
        }

        progressDialog.dismiss();
    }

    /**
     * Fetches the recipe object from Yummly and sends it to the ParseHTML method
     */
    private class AsyncRecipeFetch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return new HTTPGet().stringGET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                parseHTML(result);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates an HTMLParser object which fetches the HTML source of the recipe and splits it into steps.
     */
    private class AsyncRecipeParse extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... recipe) {

            // Fetch the HTML of the recipe
            parser = new HTMLParser(recipeId, recipe[0]);

            // Extract the directions from the HTML
            parser.findDirections();
            return  "";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            try {
                // Display the result
                display();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void startRecipe(View view){
        Intent intent = new Intent(this, GuideActivity.class);
        List<String> steps = parser.getSteps();
        intent.putExtra("recipe",  steps.toArray(new String[steps.size()]));
        startActivity(intent);
    }

    public void saveRecipe(View view) {
        if(!saved) {
            Log.i(TAG, "Saving recipe");
            RecipeDatabase savedRecipes = new RecipeDatabase(this);
            savedRecipes.addRecipe(parser.getName(), recipeId, parser.getImageURL());
            saved = true;
            //TODO: change button
        } else {
            Log.i(TAG, "Recipe already saved");
            //TODO: unsave
        }
    }

    public void saveAndAddToShoppingList(View view) {

        this.saveRecipe(view);

        ShoppingListDatabase shoppingList = new ShoppingListDatabase(this);

        try {
            ArrayList<Product> ingredients = new IngredientsParser(parser.getIngredients()).parse();
            for(Product p : ingredients) {
                ShoppingProduct product = new ShoppingProduct(p, recipeId, parser.getName());
                if(product != null)
                    shoppingList.addProduct(product);
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Ingredients parsing did not work");
        };
    }


}
