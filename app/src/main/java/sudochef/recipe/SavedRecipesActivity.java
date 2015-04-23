package sudochef.recipe;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sdp.sudochef.R;
import sudochef.recipe.database.RecipeDatabase;
import sudochef.recipe.database.SavedRecipe;
import sudochef.recipe.database.SavedRecipeAdapter;

public class SavedRecipesActivity extends ListActivity {

    private ArrayList<SavedRecipe> items;
    private ListView listView;
    private ArrayAdapter<SavedRecipe> adapter;
    private String TAG = "SC.SavedRecipeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);


        items = new RecipeDatabase(this).listSavedRecipes();
        Log.d(TAG, "Found " + items.size() + " items.");
        listView = getListView();
        adapter = new SavedRecipeAdapter(this, R.layout.saved_recipe_row, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void delete(View view) {
        RecipeDatabase db = new RecipeDatabase(this);

        // Recipe id is stored as tag in the button
        String id = (String) view.getTag();

        // Delete recipe from database with that id
        db.deleteRecipe(id);

        // Refresh list items
        items = db.listSavedRecipes();
        Log.d(TAG, "Found " + items.size() + " items.");
        listView = getListView();
        adapter = new SavedRecipeAdapter(this, R.layout.saved_recipe_row, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
