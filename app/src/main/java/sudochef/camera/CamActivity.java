package sudochef.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import sudochef.database.LookupEntry;
import sudochef.database.PLTHelper;
import sudochef.database.ProductDatabase;
import sudochef.database.ProductLookupTable;
import sudochef.inventory.Product;
import sudochef.inventory.ProductTime;
import sudochef.inventory.Units;
import sudochef.userInput.CustomActivity;
import sudochef.userInput.MultipleChoiceActivity;


public class CamActivity extends Activity {
    private static final String TAG = "CamActivity";
    ImageView imgFavorite;
    private Uri uri_file;
    static File imagePath;
    public List<LookupEntry> searchResults;
    private String SpecficWord;

    private static final int CAM_REQ = 2;
    private static final int MULTICHOICE_REQ = 3;
    private static final int FORM_REQ = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        open();
    }

    public void open(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        uri_file = getImageFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_file);
        startActivityForResult(intent, CAM_REQ);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Receiving Activity");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_CANCELED)
        {
            if(requestCode == CAM_REQ)
            {
                decodeImage();
            }
            else if(requestCode == MULTICHOICE_REQ)
            {
                unpackMultiChoice(data);
            }
            else if(requestCode == FORM_REQ)
            {
                unpackForm(data);
            }
        }
    }

    private void unpackForm(Intent data) {
//        int choice = data.getExtra("Output");
        String arr[] = data.getStringArrayExtra("Output");
        String generalName = arr[0];
        int amount = Integer.parseInt(arr[1]);
        Units unit = Units.CUP;
        ProductTime expireDate = new ProductTime(arr[3]);
        GregorianCalendar now = new GregorianCalendar();
        ProductTime ptNow = new ProductTime(now);

        new ProductLookupTable(this).addEntry(new LookupEntry(SpecficWord, generalName, "Type", expireDate.Subtract(ptNow)));
        putInProductDatabase(SpecficWord, generalName, amount, unit, expireDate);

    }

    private void putInProductDatabase(String SW, String Gen, int amt, Units unit, ProductTime exp) {
        new ProductDatabase(this).addProduct(new Product(SW, Gen, amt, unit, exp));
        super.finish();
    }

    private void unpackMultiChoice(Intent data)
    {
        int choice = data.getIntExtra("Output", 0);
        String generalName = searchResults.get(choice).generalWord;
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, searchResults.get(choice).TimeTilExpire);
        int amount = 0;
        Units unit = Units.CUP;
        ProductTime expireDate = new ProductTime(cal);
        putInProductDatabase(SpecficWord, generalName, amount, unit, expireDate);
    }

    private void decodeImage()
    {
        Bitmap bMap = BitmapFactory.decodeFile(uri_file.getPath());
        Bitmap crop_bMap = Bitmap.createScaledBitmap(bMap, 720, 560, Boolean.FALSE);
        Barcode image_trans = new Barcode(crop_bMap);

        //output
        if(image_trans.searchable && image_trans.scannable)
        {
            ProductDatabase producttlb = new ProductDatabase(this);
            ProductLookupTable plt = new ProductLookupTable(this);
            String generalName;
            SpecficWord = image_trans.getItem();
            searchResults = PLTHelper.Search(plt, image_trans.getItem());
            if(searchResults.size() == 1)
            {
                generalName = searchResults.get(0).generalWord;
                GregorianCalendar cal = new GregorianCalendar();
                cal.add(Calendar.DAY_OF_YEAR, searchResults.get(0).TimeTilExpire);
                int amount = 0;
                Units unit = Units.CUP;
                ProductTime expireDate = new ProductTime("11 11 1942");
                putInProductDatabase(SpecficWord, generalName, amount, unit, expireDate);
            }
            else if(searchResults.size() > 1)
            {
                Bundle b=new Bundle();

                List<String> in = new ArrayList<String>();
                for(LookupEntry e : searchResults){ in.add(e.generalWord); }
                String[] arrin = new String[in.size()];
                in.toArray(arrin);
                Log.d(TAG, arrin[0]);
                b.putStringArray("List", arrin);

                Intent intentMultiChoice = new Intent(CamActivity.this, MultipleChoiceActivity.class);
                intentMultiChoice.putExtras(b);
                startActivityForResult(intentMultiChoice, MULTICHOICE_REQ);
            }
            else if(searchResults.size() == 0)
            {
                Log.i(TAG, "Starting CustomDialog");
//                Intent intentForm = new Intent(CamActivity.this, CustomActivity.class);
//                startActivityForResult(intentForm, FORM_REQ);
                Bundle b=new Bundle();
                b.putBoolean("VerboseFlag", true);
                Intent intentGoListActivity = new Intent(CamActivity.this, CustomActivity.class);
                intentGoListActivity.putExtras(b);
                startActivityForResult(intentGoListActivity, FORM_REQ);
//                Bundle b=new Bundle();
//                List<String> in = new ArrayList<String>();
//                for(int i = 0; i<5; i++){ in.add("PoP"); }
//                String[] arrin = new String[in.size()];
//                in.toArray(arrin);
//                Log.d(TAG, arrin[0]);
//                b.putStringArray("List", arrin);
//                Intent intentMultiChoice = new Intent(CamActivity.this, MultipleChoiceActivity.class);
//                intentMultiChoice.putExtras(b);
//                startActivityForResult(intentMultiChoice, MULTICHOICE_REQ);
            }


        }
        else if(!image_trans.searchable && image_trans.scannable)
        {
            Toast T = Toast.makeText(this,"Cannot find in database, enter manually:" , Toast.LENGTH_LONG);
            T.show();
            super.finish();
        }
        else
        {
            Toast T = Toast.makeText(this,"could not read, scan again", Toast.LENGTH_LONG);
            T.show();
            super.finish();

        }
    }


    private static Uri getImageFileUri()
    {
        // Create a storage directory for the images
        // To be safe(er), you should check that the SDCard is mounted 
        // using Environment.getExternalStorageState() before doing this

        imagePath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BarCode");

//	        imagePath = new File(Environment.getExternalStorageDirectory(), "BarCode");
        Log.d("tag","Find "+imagePath.getAbsolutePath());
        if (! imagePath.exists()){
            if (! imagePath.mkdirs()){
                Log.d("CameraTestIntent", "failed to create directory");
                return null;
            }else{
                Log.d("tag","create new Barcode folder");
            }
        }

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = new File(imagePath,"Bar_"+ timeStamp + ".jpg");

        if(!image.exists()){
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //return image;

        // Create an File Uri
        return Uri.fromFile(image);
    }

}
