package sudochef.yummly;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import sdp.sudochef.R;
import sudochef.parser.ChooseRecipeActivity;


public class SearchActivity extends ListActivity
        implements AbsListView.OnScrollListener {

    String response;
    ProgressDialog progressDialog;
    int numResults = 0;
    int max = 0;
    int index = 0;
    SearchCall call;
    RecipeResult results[];
    ArrayList<RecipeResult> resultsList;
    SearchResultAdapter adapter;
    ListView listView;


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
                        Log.d("SC.Search", "Checking if more results available.");
                        if (listView.getLastVisiblePosition() >= listView.getCount() - 1) {
                            call.nextPage();

                            // Use async method for search
                            new AsyncSearch().execute(call.buildString());
                        }
                    } else {
                        Log.d("SC.Search", "Scroll state is not idle");
                    }
                } else {
                    Log.d("SC.Search", "End of results");
                }

                Log.d("SC.Search", "Scrolled");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        Log.d("SC.Search", "Scrolled");
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        if(numResults < max) {
            if (scrollState == SCROLL_STATE_IDLE) {
                Log.d("SC.Search", "Checking if more results available.");
                if (listView.getLastVisiblePosition() >= listView.getCount() - 1) {
                    call.nextPage();

                    // Use async method for search
                    new AsyncSearch().execute(call.buildString());
                }
            } else {
                Log.d("SC.Search", "Scroll state is not idle");
            }
        } else {
            Log.d("SC.Search", "End of results");
        }

        Log.d("SC.Search", "Scrolled");

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("SC.Search", "List item click");
        v.setBackgroundColor(0xFFAEAEAE);
        select(results[position].getId());
        v.setBackgroundColor(0xFFDEDEDE);
    }

    public void search(View view) throws Exception {
        reset();

        // Get input from button
        EditText searchInput = (EditText)findViewById(R.id.searchText);
        String query = searchInput.getText().toString();
        Log.d("SC.Search", "Starting search with query " + query);

        // Build search API call
        call = new SearchCall();
        call.setQuery(query);

        // Use async method for search
        new AsyncSearch().execute(call.buildString());
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void reset() {
        //getListView().removeAllViews();
        response = null;
        numResults = 0;
        index = 0;
        results = null;
        adapter = null;
    }

    public void searchComplete(String result) throws Exception {
        progressDialog.dismiss();
        Parser parser = new Parser(result);
        Response response = parser.ParseResponse();

        if(results == null)
        {
            results = new RecipeResult[response.getCount()];
        }

        for(int i = 0; i < response.getMatches().length; i++) {
            results[index++] = response.getMatches()[i];
        }

        Log.d("SC.Search", "Creating adapter");
        if(adapter == null) adapter = new SearchResultAdapter(this, R.layout.list_row, results);
        Log.d("SC.Search", "Setting adapter");
        getListView().setAdapter(adapter);

        Log.d("SC.Search", "Did it work?");

        for(int i = 0; i < results.length; i++){
            //   new ThumbLoader().execute(i);
        }
    }

    public void select(String id) {
        Log.d("SC.Search", "Recipe selected with ID: " + id);

        Intent intent = new Intent(this, ChooseRecipeActivity.class);
        intent.putExtra("recipeId", id);
        startActivity(intent);
    }

    private class AsyncSearch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.d("SC.Search", "Starting Async HTTP GET for yummmly search call.");
            return new HTTPGet().stringGET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("SC.Search", "Async HTTP GET for yummmly search call returned.");
            try {
                searchComplete(result);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private class ThumbLoader extends AsyncTask<Integer, Void, Integer> {
        int index;

        @Override
        protected Integer doInBackground(Integer... params) {
            Log.d("SC.Search", "Async HTTP GET for thumb sent.");
            index = params[0];
            results[index].loadThumb();
            return 1;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Integer result) {
            Log.d("SC.Search", "Async HTTP GET for thumb returned.");
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