package sudochef.recipe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class RecipeDatabase extends SQLiteOpenHelper {
    private final String TAG = "ProductDatabase";
    private static final String TABLE_RECIPES = "SavedRecipes";
    // Product Table Columns names
    private static final String KEY_NAME = "id";
    private static final String KEY_ID = "name";
    private static final String KEY_IMAGEURL = "image";

    public RecipeDatabase(Context context) {
        super(context, "/mnt/sdcard/SudoChef.db",null, 1); //TODO Change back to secure loc
        String CREATE_RECIPE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + "("
                + KEY_NAME + " STRING PRIMARY KEY," + KEY_ID + " STRING," + KEY_IMAGEURL + " STRING" + ")";
        this.getWritableDatabase().execSQL(CREATE_RECIPE_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public void addRecipe(String name, String id, String imageUrl)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_ID, id);
        values.put(KEY_IMAGEURL, imageUrl);

        // Inserting Row
        db.insert(TABLE_RECIPES, null, values);
        db.close(); // Closing database connection
    }

    public int deleteRecipe(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_RECIPES, KEY_ID + "=?", new String[]{id});
    }

    // Getting All Contacts
    public ArrayList<SavedRecipe> listSavedRecipes() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int index = 0;
        ArrayList<SavedRecipe> recipeList = new ArrayList<SavedRecipe>(cursor.getCount());

        Log.d(TAG, " " + cursor.getCount());
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst() ) {
            do {
                Log.d(TAG, " " + cursor.getCount());
                recipeList.add(index++, new SavedRecipe(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }

        // return contact list
        return recipeList;
    }

    public boolean contains(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES,
                new String[] { KEY_NAME, KEY_ID, KEY_IMAGEURL },
                KEY_ID + "=?",
                new String[] {id},
                null, null, null, null);

        if(cursor == null) return false;
        return (cursor.getCount() > 0);
    }
}
