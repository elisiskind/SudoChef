package sudochef.yummly;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import sdp.sudochef.R;

/**
 * This is a class that converts the array of recipe results into the views that are displayed by SearchActivity
 */
public class SearchResultAdapter extends ArrayAdapter<RecipeResult> {
    Context context;
    int layoutResourceId;
    RecipeResult results[] = null;

    public SearchResultAdapter(Context context, int layoutResourceId, RecipeResult[] results) {
        super(context, layoutResourceId, results);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.results = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Log.d("SC.Adapter", "Getting the view");

        View row = convertView;
        ResultHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ResultHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.result_image);
            holder.txtTitle = (TextView)row.findViewById(R.id.result_title);
            holder.txtSource = (TextView)row.findViewById(R.id.result_source);
            holder.txtDesc = (TextView)row.findViewById(R.id.result_description);

            row.setTag(holder);
        }
        else
        {
            holder = (ResultHolder)row.getTag();
        }

        RecipeResult result = results[position];
        holder.txtTitle.setText(result.getName());
        holder.txtSource.setText(" " + result.getSource());
        holder.txtDesc.setText(result.descriptionString());
        holder.imgIcon.setImageBitmap(result.getThumb());

        return row;
    }

    static class ResultHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtSource;
        TextView txtDesc;
    }
}
