package sudochef.recipe.database;

/**
 * Created by eli on 4/14/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sdp.sudochef.R;
import sudochef.recipe.ChooseRecipeActivity;
import sudochef.search.HTTPGet;

/**
 * This is a class that converts the array of recipe results into the views that are displayed by SearchActivity
 */
public class SavedRecipeAdapter extends ArrayAdapter<SavedRecipe> {
    Context context;
    int layoutResourceId;
    public String id;
    ArrayList<SavedRecipe> items = null;
    private String TAG = "SC.SIAdapater";

    public SavedRecipeAdapter(Context context, int layoutResourceId, ArrayList<SavedRecipe> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Log.d("SC.Adapter", "Getting the view");

        View row = convertView;
        ItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            holder.name = (TextView)row.findViewById(R.id.recipe_title);
            holder.thumb = (ImageView)row.findViewById(R.id.recipe_thumb);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        SavedRecipe recipe = items.get(position);
        holder.name.setText(recipe.getName());
        row.findViewById(R.id.delete_recipe).setTag(recipe.getId());
        row.setTag(R.id.tag_id, recipe.getId());

        if(!recipe.getImageURL().isEmpty())
        {
            holder.thumb.setTag(recipe.getImageURL());
            new ThumbLoader().doInBackground(holder.thumb);
        }

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = (String) v.getTag(R.id.tag_id);
                Intent intent = new Intent(getContext(), ChooseRecipeActivity.class);
                intent.putExtra("recipeId", id);
                getContext().startActivity(intent);
            }
        });

        return row;
    }

    static class ItemHolder
    {
        TextView name;
        ImageView thumb;
    }


    private class ThumbLoader extends AsyncTask<ImageView, Void, Integer> {
        ImageView thumb;

        @Override
        protected Integer doInBackground(ImageView... params) {
            Log.d(TAG, "Async HTTP GET for thumb sent.");
            thumb = params[0];
            Bitmap bm = new HTTPGet().bitmapGET((String)thumb.getTag());
            if(bm != null) {
                thumb.setImageBitmap(bm);
                scaleImage(thumb, 30);
                return 0;
            } else {
                return 1;
            }

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Integer result) {
            Log.d(TAG, "Async HTTP GET for thumb returned.");
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
