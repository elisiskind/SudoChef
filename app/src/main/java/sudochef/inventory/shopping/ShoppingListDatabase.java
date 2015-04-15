package sudochef.inventory.shopping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import sudochef.inventory.Units;

public class ShoppingListDatabase extends SQLiteOpenHelper {
    private final String TAG = "SC.ShoppingDatabase";

    // Table names
    private static final String TABLE_SHOPPING = "ShoppingList";

    // Column names
    private static final String KEY_NAME = "name";
    private static final String KEY_AMT = "amount";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_CHECKED = "checked";
    private static final String KEY_RECIPEID = "id";
    private static final String KEY_RECIPENAME = "recipeName";


    public ShoppingListDatabase(Context context) {
        super(context, "/mnt/sdcard/SudoChef.db",null, 1); //TODO Change back to secure loc
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SHOPPING + "("
                + KEY_NAME + " STRING PRIMARY KEY,"
                + KEY_AMT + " DOUBLE,"
                + KEY_UNIT + " STRING, "
                + KEY_CHECKED + " STRING, "
                + KEY_RECIPEID + " STRING, "
                + KEY_RECIPENAME + " STRING"
                + ")";
        this.getWritableDatabase().execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public void addProduct(ShoppingProduct p)
    {
        deleteProduct(p.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, p.getName()); // Product Name
        values.put(KEY_AMT, p.getAmount()); //
        values.put(KEY_UNIT, p.getUnit().toString());
        values.put(KEY_CHECKED, p.isChecked() ? "true" : "false");
        values.put(KEY_RECIPEID, p.getRecipeID());
        values.put(KEY_RECIPENAME, p.getRecipeName());

        // Inserting Row
        db.insert(TABLE_SHOPPING, null, values);
        db.close(); // Closing database connection
    }

    public int deleteProduct(String p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SHOPPING, KEY_NAME + "=?", new String[]{p});
    }

    public int deleteProductsFromRecipe(String recipeID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SHOPPING, KEY_RECIPEID + "=?", new String[]{recipeID});
    }

    public int deleteChecked()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SHOPPING, KEY_CHECKED + "=?", new String[]{"true"});
    }



    // Getting All Contacts
    public ArrayList<ShoppingProduct> getAllProducts() {
        ArrayList<ShoppingProduct> products = new ArrayList<ShoppingProduct>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SHOPPING;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d(TAG, " " + cursor.getCount());
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst() ) {
            do {
                Log.d(TAG, " " + cursor.getCount());

                ShoppingProduct prod = new ShoppingProduct(
                        (cursor.getString(3).equals("true")),
                        cursor.getString(0),
                        Units.valueOf(cursor.getString(2)),
                        Double.parseDouble(cursor.getString(1)),
                        cursor.getString(4),
                        cursor.getString(5)
                       );

                // Adding contact to list
                products.add(prod);
            } while (cursor.moveToNext());
        }

        // return contact list
        return products;
    }

    public int setChecked(String name, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_CHECKED, checked ? "true" : "false");

        // updating row
        return db.update(TABLE_SHOPPING, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(name) });
    }

}
