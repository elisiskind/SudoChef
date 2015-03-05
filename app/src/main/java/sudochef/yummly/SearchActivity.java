package sudochef.yummly;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import sdp.sudochef.R;
import sudochef.database.ProductDatabase;
import sudochef.inventory.Product;
import sudochef.parser.ChooseRecipeActivity;


public class SearchActivity extends ListActivity {

    private ProgressDialog progressDialog;
    private int numResults = 0;
    private int max = 0;
    private int index = 0;
    private SearchCall call;
    private ArrayList<RecipeResult> resultsList;
    private SearchResultAdapter adapter;
    private ListView listView;

    private final String TAG = "SC.Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressDialog = new ProgressDialog(this);

        listView = getListView();

        getListView().setAdapter(adapter);
        getListView().setOnScrollListener(new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(numResults < max) {
                    if (scrollState == SCROLL_STATE_IDLE) {
                        Log.d(TAG, "Checking if more results available.");
                        if (listView.getLastVisiblePosition() >= listView.getCount() - 1) {

                            call.nextPage();

                            // Use async method for search
                            new AsyncSearch().execute(call.buildString());
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        final EditText searchBar = (EditText)findViewById(R.id.searchText);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        search(searchBar);
                    } catch (Exception e) {

                    }
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "List item click");
        select(resultsList.get(position).getId());
    }

    public void search(View view) throws Exception {
        reset();

        hideKeyboard();

        // Get input from button
        EditText searchInput = (EditText)findViewById(R.id.searchText);
        String query = searchInput.getText().toString();
        Log.d(TAG, "Starting search with query " + query);

        makeCall(query);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void searchByExpireDate(View view) throws  Exception
    {
        reset();
        Product expireSoon = new ProductDatabase(this).getClosestToExpire();

        EditText searchInput = (EditText)findViewById(R.id.searchText);
        searchInput.setText(expireSoon.generalName);
        makeCall(expireSoon.generalName);
    }

    private void makeCall(String query) throws Exception {
        // Build search API call
        call = new SearchCall();
        call.setQuery(query);

        // Use async method for search
        new AsyncSearch().execute(call.buildString());
        progressDialog.setMessage("Fetching some recipes for you...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void reset() {
        //getListView().removeAllViews();
        numResults = 0;
        if(resultsList != null) { resultsList.clear(); }
        max = 0;
        index = 0;
        adapter = null;
    }

    public void searchComplete(String result) throws Exception {

        progressDialog.dismiss();

        // Parse results
        Parser parser = new Parser(result);
        Response response = parser.ParseResponse();

        // Add results to results list
        updateResults(response);

        // Update array adapter to show new items
        updateAdapter();

        // Load thumbnails
        //loadThumbs();

        Log.d(TAG, "Results: " + numResults + " Max: " + max);

        // If we haven't filled the page, load more results
        if(index < 10 && numResults < max) {

            Log.d(TAG, "Loading another page");
            call.nextPage();

            // Use async method for search
            new AsyncSearch().execute(call.buildString());
        }
    }

    private void updateResults(Response response) {
        if(resultsList == null)
        {
           resultsList = new ArrayList<>();
        }

        for(int i = 0; i < response.getMatches().length; i++) {
            RecipeResult match = response.getMatches()[i];
            if(YummlySources.getAllowedString().contains(match.getSource())) {
                resultsList.add(index++, response.getMatches()[i]);
            }

            numResults++;
        }

        max = response.getCount();
    }

    private void updateAdapter() {
        if(adapter == null) {
            Log.d(TAG, "Creating adapter");
            adapter = new SearchResultAdapter(this, R.layout.list_row, resultsList);
            Log.d(TAG, "Setting adapter");
            getListView().setAdapter(adapter);
        }

        adapter.notifyDataSetChanged();
    }

    private void loadThumbs(){
        for(int i = 0; i < resultsList.size(); i++){
            new ThumbLoader().execute(i);
        }
    }

    public void select(String id) {
        Log.d(TAG, "Recipe selected with ID: " + id);

        Intent intent = new Intent(this, ChooseRecipeActivity.class);
        intent.putExtra("recipeId", id);
        startActivity(intent);
    }

    private class AsyncSearch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.d(TAG, "Starting Async HTTP GET for yummmly search call.");
            return new HTTPGet().stringGET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "Async HTTP GET for yummmly search call returned.");
            try {
                searchComplete(result);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void switchToSearch(View view){
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        switcher.showNext();
    }

    public void switchToSuggest(View view){
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        switcher.showPrevious();
    }



    private class ThumbLoader extends AsyncTask<Integer, Void, Integer> {
        int index;

        @Override
        protected Integer doInBackground(Integer... params) {
            Log.d(TAG, "Async HTTP GET for thumb sent.");
            index = params[0];
            resultsList.get(index).loadThumb();
            return 1;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Integer result) {
            Log.d(TAG, "Async HTTP GET for thumb returned.");
            ImageView thumb = (ImageView)(getListView().getChildAt(index));
            scaleImage(thumb, 100);
            thumb.refreshDrawableState();
        }
    }

    private void scaleImage(ImageView view, int boundBoxInDp)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

}