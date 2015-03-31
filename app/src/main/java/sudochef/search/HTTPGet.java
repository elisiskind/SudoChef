package sudochef.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HTTPGet {

    static InputStream inputStream;

    private void GET(String url) {
        inputStream = null;
        try {


            Log.d("SC.HTTPGET", "Creating HTTP client object");
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            Log.d("SC.HTTPGET", "Sending request: " + url);
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            Log.d("SC.HTTPGET", "Receiving response");
            inputStream = httpResponse.getEntity().getContent();

            Log.d("SC.HTTPGET", "Response returned");

        } catch (Exception e) {
            Log.e("InputStream", e.getLocalizedMessage());
        }
    }

    public String stringGET(String url) {
        GET(url);
        Log.d("SC.HTTPGET", "Converting input stream");
        String response = convertInputStreamToString();
        Log.d("SC.HTTPGET", "Response: " + response);
        return response;
    }

    public Bitmap bitmapGET(String url) {
        GET(url);
        return BitmapFactory.decodeStream(inputStream);
    }

    private static String convertInputStreamToString() {
        String result = null;
        try {
            if(inputStream == null) {
                Log.w("SC.HTTPGET", "Input stream was null");
            }
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}