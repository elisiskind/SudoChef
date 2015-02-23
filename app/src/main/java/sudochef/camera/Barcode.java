package sudochef.camera;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.UPCAReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class Barcode
{
    Bitmap binary_File;
    Bitmap grey_File;
    Bitmap Gaussian;
    int coor_x, coor_y, height, width, threshold;
    upcQuery q;
    boolean scannable, searchable;

    Barcode(Bitmap src)
    {
//		grey_File = makeGreyScale(src);
        width = src.getWidth();
        height = src.getHeight();
        decoding(src);
    }

    private void decoding(Bitmap src)
    {
        int seconds;
        int milli;
        double start;
        scannable = true;
        searchable = false;
        int[] intArray = new int[src.getWidth()*src.getHeight()];
        src.getPixels(intArray, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        LuminanceSource source = new RGBLuminanceSource(src.getWidth(), src.getHeight(),intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));


        Reader reader = new UPCAReader();
        Result result = null;
        Hashtable<DecodeHintType, Object> hint = new Hashtable<DecodeHintType, Object>();
        hint.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        try
        {

            result = reader.decode(bitmap, hint);

            lookup(result);
        }
        catch(ChecksumException e){}
        catch (NotFoundException e) {
            scannable = false;
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    private void lookup(Result r)
    {
        boolean upcdatabase = true;
        q = new upcQuery(Long.parseLong(r.getText(), 10), upcdatabase);
        Log.d("tag", "status" + q.getStatus());
        if(!q.getStatus())
        {
            q = new upcQuery(Long.parseLong(r.getText(), 10), false);
        }
        if(q.getStatus())
        {
            scannable = true;
            searchable = true;
        }
        else
        {
            scannable = true;
            searchable = false;
        }
    }

    private void Gaussian_1D(Bitmap src)
    {
        double factor = 159;
        int A, R, G, B;
        int sumR, sumG, sumB;
        int SIZE = 5;
        int[] pixels = new int[SIZE];
        int [] Matrix = {2,4,5,4,2}; // sigma = 1.4
        int [] result = new int [width * height];
        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < width; x++)
            {
                result[x + y * width] = src.getPixel(x,y);
                result[x + ((height-y)-1) * width] = src.getPixel(x,(height-y)-1);
            }
        }

        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < height; y++)
            {
                result[x + y * width] = src.getPixel(x,y);
                result[(width-x)-1 + y * width] = src.getPixel((width-x)-1,y);
            }
        }

        for(int y = 0; y < height - 6; ++y)
        {
            for(int x = 0; x < width - 6; ++x)
            {
                for(int j = 0; j < SIZE; j++) {
                    pixels[j] = src.getPixel(x + j, y);
                }

                // get alpha of center pixel
                A = Color.alpha(pixels[2]);

                // init color sum
                sumR = sumG = sumB = 0;

                // get sum of RGB on matrix
                for(int j = 0; j < SIZE; ++j) {
                    sumR += (Color.red(pixels[j]) * Matrix[j]);
                    sumG += (Color.green(pixels[j]) * Matrix[j]);
                    sumB += (Color.blue(pixels[j]) * Matrix[j]);
                }


                // get final Red
                R = (int)(sumR / factor);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                // get final Green
                G = (int)(sumG / factor);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                // get final Blue
                B = (int)(sumB / factor);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // apply new pixel
//				Gaussian.setPixel(x + 2, y, Color.argb(A, R, G, B));
                result[x+2 + y*width] = Color.argb(A, R, G, B);
            }
        }
        for(int x = 0; x < width - 6; ++x)
        {
            for(int y = 0; y < height - 6; ++y)
            {
                for(int j = 0; j < SIZE; j++) {
                    pixels[j] = result[x + width * (y + j)];
                }

                // get alpha of center pixel
                A = Color.alpha(pixels[2]);

                // init color sum
                sumR = sumG = sumB = 0;

                // get sum of RGB on matrix
                for(int j = 0; j < SIZE; ++j) {
                    sumR += (Color.red(pixels[j]) * Matrix[j]);
                    sumG += (Color.green(pixels[j]) * Matrix[j]);
                    sumB += (Color.blue(pixels[j]) * Matrix[j]);
                }


                // get final Red
                R = (int)(sumR / factor);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                // get final Green
                G = (int)(sumG / factor);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                // get final Blue
                B = (int)(sumB / factor);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // apply new pixel
//				Gaussian.setPixel(x, y + 2, Color.argb(A, R, G, B));
                result[x + width * (y+2)] = Color.argb(A, R, G, B);
            }
        }
        Gaussian = Bitmap.createBitmap(result, width, height, src.getConfig());
    }


    /*
    *	SLOW DO NOT USE
    */
    private  void computeGaussian(Bitmap src) {
        double factor = 159;
        int A, R, G, B;
        int sumR, sumG, sumB;
        int SIZE = 3;
        int[][] pixels = new int[SIZE][SIZE];
        int [][] Matrix = {{2,4,5,4,2},{4,9,12,9,4},{5,12,15,12,5},{4,9,12,9,4},{2,4,5,4,2}}; // sigma = 1.4

        for(int y = 0; y < height - 6; ++y) {
            for(int x = 0; x < width - 6; ++x) {

                // get pixel matrix
                for(int i = 0; i < SIZE ; i++) {
                    for(int j = 0; j < SIZE; j++) {
                        pixels[i][j] = src.getPixel(x + i, y + j);
                    }
                }

                // get alpha of center pixel
                A = Color.alpha(pixels[1][1]);

                // init color sum
                sumR = sumG = sumB = 0;

                // get sum of RGB on matrix
                for(int i = 0; i < SIZE; ++i) {
                    for(int j = 0; j < SIZE; ++j) {
                        sumR += (Color.red(pixels[i][j]) * Matrix[i][j]);
                        sumG += (Color.green(pixels[i][j]) * Matrix[i][j]);
                        sumB += (Color.blue(pixels[i][j]) * Matrix[i][j]);
                    }
                }

                // get final Red
                R = (int)(sumR / factor);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                // get final Green
                G = (int)(sumG / factor);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                // get final Blue
                B = (int)(sumB / factor);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // apply new pixel
                Gaussian.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
            }
        }
    }


    private Bitmap makeBinary(Bitmap b)
    {
        int width, height, threshold, pixel, red;
        height = b.getHeight();
        width = b.getWidth();
        threshold = 127;
        Bitmap bmpBinary = Bitmap.createBitmap(width, height,  Bitmap.Config.ARGB_8888);

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                //get one pixel color
                pixel = b.getPixel(x, y);
                red = Color.red(pixel);

                //get binary value
                if(red < threshold){
                    bmpBinary.setPixel(x, y, 0xFF000000);
                } else{
                    bmpBinary.setPixel(x, y, 0xFFFFFFFF);
                }

            }
        }
        return bmpBinary;
    }

    private Bitmap makeGreyScale(Bitmap b)
    {
        int width, height, pixel, red, blue, green, alpha;
        height = b.getHeight();
        width = b.getWidth();
        threshold = 127;
        Bitmap bmpGrey = Bitmap.createBitmap(width, height,  Bitmap.Config.ARGB_8888);

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                //get one pixel color
                pixel = b.getPixel(x, y);
                alpha = Color.alpha(pixel);
                red = Color.red(pixel);
                blue = Color.blue(pixel);
                green = Color.green(pixel);

                red = green = blue = (int)(0.299 * red + 0.587 * green + 0.114 * blue);
                // grey_value = (red+blue+green) / 3;
                bmpGrey.setPixel(x, y, Color.argb(alpha, red, green,blue));
            }
            @SuppressWarnings("unused")
            int temp = 4;
        }
        return bmpGrey;
    }

    private void saveFile()
    {
        try
        {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//			File file_bin = new File(Environment.getExternalStoragePublicDirectory(
//					Environment.DIRECTORY_PICTURES), "BarCode_binary.bmp");
//			FileOutputStream fos_bin = new FileOutputStream(file_bin);
//			
//			File file_grey = new File(Environment.getExternalStoragePublicDirectory(
//					Environment.DIRECTORY_PICTURES), "BarCode_grey.bmp");
//			FileOutputStream fos_grey = new FileOutputStream(file_grey);

            File file_Gauss = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "BarCode_Gaussian" + timeStamp + ".bmp");
            FileOutputStream fos_Gauss = new FileOutputStream(file_Gauss);

//			binary_File.compress(Bitmap.CompressFormat.JPEG, 90, fos_bin);
//			grey_File.compress(Bitmap.CompressFormat.JPEG, 90, fos_grey);
            Gaussian.compress(Bitmap.CompressFormat.JPEG, 90, fos_Gauss);

//			fos_bin.close();
//			fos_grey.close();
            fos_Gauss.close();

        } catch(IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    public String getItem()
    {
        return q.getItem();
    }

    public String getAll()
    {
        return q.result;
    }
}