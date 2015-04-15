package sudochef.inventory.shopping;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

import sdp.sudochef.R;

public class ShoppingListActivity extends ListActivity {

    private ArrayList<ShoppingProduct> items;
    private ShoppingItemAdapter adapter;
    private ListView listView;
    private String TAG = "SC.SLActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        items = new ShoppingListDatabase(this).getAllProducts();
        Log.d(TAG, "Found " + items.size() + " items.");
        listView = getListView();
        adapter = new ShoppingItemAdapter(this, R.layout.list_item_row, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
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

    public void check(View view) {
        String name = (String)view.getTag();
        ShoppingListDatabase db = new ShoppingListDatabase(this);
        db.setChecked(name, ((CheckBox)view).isChecked());
    }

    public void clearChecked(View view) {
        ShoppingListDatabase db = new ShoppingListDatabase(this);
        db.deleteChecked();
        items = db.getAllProducts();
        adapter.notifyDataSetChanged();
    }

}
