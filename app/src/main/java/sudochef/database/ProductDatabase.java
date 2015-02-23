package sudochef.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import sudochef.inventory.Product;
import sudochef.inventory.ProductTime;
import sudochef.inventory.Units;

public class ProductDatabase extends SQLiteOpenHelper {
    private static final String TABLE_INVENTORY = "Inventory";
    // Product Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_GENNAME = "generalname";
    private static final String KEY_AMT = "amount";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_DATEEXPIRE = "date";

    public ProductDatabase(Context context) {
        super(context, "/mnt/sdcard/SudoChef.db",null, 1); //TODO Change back to secure loc
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INVENTORY + "("
                + KEY_NAME + " STRING PRIMARY KEY," + KEY_GENNAME + " STRING," + KEY_AMT + " REAL,"
                + KEY_UNIT + " INTEGER," + KEY_DATEEXPIRE + " STRING" + ")";
        this.getWritableDatabase().execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public void addProduct(Product p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, p.name); // Product Name
        values.put(KEY_GENNAME, p.generalName); // Product Name
        values.put(KEY_AMT, p.amount); // 
        values.put(KEY_UNIT, p.amountUnit.ordinal()); // 
        values.put(KEY_DATEEXPIRE, p.date.FormatToString()); // 

        // Inserting Row
        db.insert(TABLE_INVENTORY, null, values);
        db.close(); // Closing database connection
    }

    public int deleteProduct(String p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INVENTORY, KEY_NAME + "=?", new String[]{p});
    }

    public Product getProduct(String s)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INVENTORY,
                new String[] { KEY_NAME, KEY_GENNAME, KEY_AMT, KEY_UNIT, KEY_DATEEXPIRE },
                KEY_NAME + "=?",
                new String[] {s},
                null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        ProductTime date = new ProductTime(cursor.getString(4));

        Product prod = new Product(cursor.getString(0),
                cursor.getString(1),
                Integer.parseInt(cursor.getString(2)),
                Units.values()[Integer.parseInt(cursor.getString(3))],
                date);

        return prod;
    }

    public int updateProduct(Product p)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, p.name);
        values.put(KEY_GENNAME, p.generalName);
        values.put(KEY_AMT, p.amount);
        values.put(KEY_UNIT, p.amountUnit.ordinal());
        values.put(KEY_DATEEXPIRE, p.date.FormatToString());

        // updating row
        return db.update(TABLE_INVENTORY, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(p.name) });
    }

    // Getting All Contacts
    public List<Product> getAllProducts() {
        List<Product> contactList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INVENTORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ProductTime date = new ProductTime(cursor.getString(4));

                Product prod = new Product(cursor.getString(0),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)),
                        Units.values()[Integer.parseInt(cursor.getString(3))],
                        date);

                // Adding contact to list
                contactList.add(prod);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public List<Product> allProdExpireIn(ProductTime expireTime) {
        List<Product> contactList = new ArrayList<Product>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INVENTORY;// + " WHERE " + KEY_AMT + " < " + expireTime;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ProductTime date = new ProductTime(cursor.getString(4));
                if(expireTime.getCal().compareTo(date.getCal()) == -1){ continue; }


                Product prod = new Product(cursor.getString(0),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)),
                        Units.values()[Integer.parseInt(cursor.getString(3))],
                        date);
                // Adding contact to list
                contactList.add(prod);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


}
