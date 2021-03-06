package sudochef.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import sdp.sudochef.R;
import sudochef.camera.CamActivity;
import sudochef.guide.GuideActivity;
import sudochef.guide.bluetooth.DeviceListActivity;
import sudochef.inventory.shopping.ShoppingListActivity;
import sudochef.recipe.SavedRecipesActivity;
import sudochef.recipe.database.RecipeDatabase;
import sudochef.search.SearchActivity;

public class MainActivity extends ActionBarActivity {

    String TAG = "SC.Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"Switched config");

        View view = findViewById(R.id.main_root);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view.setBackgroundResource (R.drawable.background_land);
        } else {
            view.setBackgroundResource (R.drawable.background);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void startSearch(View view){
        Log.i(TAG, "Starting search");
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_in);
    }

    public void startRecipe(View view){
        Log.i(TAG, "Starting Guide Activity");
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_in);
    }

    public void viewSavedRecipes(View view){
        new RecipeDatabase(this).addRecipe("Scrambled Eggs and Toast", "demo", "");
        Log.i(TAG, "Starting Saved Recipes Activity");
        Intent intent = new Intent(this, SavedRecipesActivity.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_in);
    }

    public void viewShoppingList(View view){
        //new ShoppingListDatabase(this).deleteTable();
        Log.i(TAG, "Starting Shopping Activity");
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_in);
    }

    public void startBarCodeScan(View view){
        Log.i(TAG, "Starting Bar Code Scanner");
        Intent intent = new Intent(this, CamActivity.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_in);
    }

    public void startBluetooth(View view){
        Log.i(TAG, "Starting Bluetooth");
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_in);
    }
}
