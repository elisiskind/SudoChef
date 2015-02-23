package sudochef.camera;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class upcQuery{
    long UPC;
    URL url;
    String item;
    String result;
    boolean status;

    upcQuery(long code, boolean upc_d){

        UPC = code;
        try{
            loadURL(upc_d);
        }
        catch(MalformedURLException e){}

        try{
            getURL();
        }
        catch(IOException e){}

        try{
            getJSON(upc_d);
        }
        catch(JSONException e){
            status =false;
            Log.d("tag", "JSON EXECEPTION");
        }
    }

    private void loadURL(boolean upc_d) throws MalformedURLException
    {
        //outpan = 708db4ea2358e8792084d8b3663880d8    SEMANTICS = SEM3FB597820E3C5AA3F9A401BD46F7EFD04

//		url = new URL("http://www.outpan.com/api/get_product.php?barcode=" + UPC);						//outpan
        if(upc_d)
        {
            url = new URL("http://api.upcdatabase.org/json/b5d871234aaebb12dfdf8fd6c0b7e685/" + UPC);
        }
        else
        {
            url = new URL("http://eandata.com/feed/?v=3&keycode=A2EC545F547282EF&mode=json&find=" + UPC);
        }
    }

    private void getURL() throws IOException
    {
        DownloadWebPageTask worker = new DownloadWebPageTask();
        worker.execute(url);
        try {
            result = worker.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getJSON(boolean json_upc_d) throws JSONException
    {
        if(json_upc_d)
        {
            JSONObject jObject = new JSONObject(result);
            item = jObject.getString("itemname") + jObject.getString("description");
            status = true;
        }
        else
        {
            JSONObject jObject = new JSONObject(result);
            item = jObject.getJSONObject("product").getJSONObject("attributes").getString("product");
            status = true;
        }
    }

    public String getItem()
    {
        return item;
    }

    public boolean getStatus()
    {
        return status;
    }
    private class DownloadWebPageTask extends AsyncTask<URL, Void, String> {
        String r;

        @Override
        protected String doInBackground(URL... urls) {
            InputStream in = null;
            try {
                in = urls[0].openStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader Buff_Reader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = Buff_Reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            r = sb.toString();
            return r;
        }

        @Override
        protected void onPostExecute(String result) {
//	    	return result;
        }
    }

}