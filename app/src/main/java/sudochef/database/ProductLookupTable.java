package sudochef.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductLookupTable extends SQLiteOpenHelper {
    private static final String TABLE_PRODUCTLOOKUP = "ProductLookuptlb";
    // Product Table Columns names
    private static final String KEY_SW = "SearchWords";
    private static final String KEY_GW = "GeneralWord";
    private static final String KEY_TYPE = "Type";
    private static final String KEY_TTE = "TimeToExpire";

    public ProductLookupTable(Context context) {
        super(context, "/mnt/sdcard/SudoChef.db",null, 1); //TODO Change back to secure loc
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTLOOKUP + "("
                + KEY_SW + " STRING PRIMARY KEY," + KEY_GW + " STRING,"
                + KEY_TYPE + " INTEGER," + KEY_TTE + " STRING" + ")";
        this.getWritableDatabase().execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    public void addEntry(LookupEntry p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SW, p.searchWord); // Product Name
        values.put(KEY_GW, p.generalWord); // 
        values.put(KEY_TYPE, p.type); // 
        values.put(KEY_TTE, p.TimeTilExpire); // 

        // Inserting Row
        db.insert(TABLE_PRODUCTLOOKUP, null, values);
        db.close(); // Closing database connection
    }

    public int deleteEntry(String p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCTLOOKUP, KEY_SW + "=?", new String[]{p});
    }

    public LookupEntry search(String s)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        LookupEntry entry = null;
        Cursor cursor = db.query(TABLE_PRODUCTLOOKUP,
                new String[] { KEY_SW, KEY_GW, KEY_TYPE, KEY_TTE },
                KEY_SW + "=?",
                new String[] {s},
                null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        if(cursor.getCount() != 0)
        {
            entry = new LookupEntry(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)));
        }
        return entry;
    }
}
