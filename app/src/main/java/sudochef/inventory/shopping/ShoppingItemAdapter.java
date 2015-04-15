package sudochef.inventory.shopping;

/**
 * Created by eli on 4/14/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import sdp.sudochef.R;

/**
 * This is a class that converts the array of recipe results into the views that are displayed by SearchActivity
 */
public class ShoppingItemAdapter extends ArrayAdapter<ShoppingProduct> {
    Context context;
    int layoutResourceId;
    ArrayList<ShoppingProduct> items = null;
    private String TAG = "SC.SIAdapater";

    public ShoppingItemAdapter(Context context, int layoutResourceId, ArrayList<ShoppingProduct> items) {
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
            holder.txtName = (TextView)row.findViewById(R.id.item_title);
            holder.txtAmount = (TextView)row.findViewById(R.id.item_amount);
            holder.checkBox = (CheckBox)row.findViewById(R.id.item_checked);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        ShoppingProduct product = items.get(position);

        // set first letter uppercase
        String name = product.getName();

        try {
            char[] stringArray = name.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            name = new String(stringArray);
        } catch (Exception e) {

        }

        holder.txtName.setText(name);

        double amt = product.getAmount();
        String unit = amt == 1 ? product.getUnit().getSingularName() : product.getUnit().getPluralName();

        if(amt > 0) {
            String amountString = (amt == (long) amt) ? String.format("%d", (long) amt) : String.format("%s", amt);
            holder.txtAmount.setText(amountString + " " + unit);
        } else {
            holder.txtAmount.setText(" ");
        }
        holder.checkBox.setChecked(product.isChecked());

        holder.checkBox.setTag(product.getName());


        return row;
    }

    static class ItemHolder
    {
        TextView txtName;
        TextView txtAmount;
        CheckBox checkBox;
    }
}
