package sudochef.guide.bluetooth;

import android.util.Log;

import java.io.IOException;

/**
 * Created by LH-21 on 3/3/2015.
 */
public class OvenControl extends DeviceControl {
    private final String TAG = "OvenControl";
    private static final String MAC = "30:14:11:28:24:44";
    private static final int Toast = 0xFF;
    private static final int Broil = 0xF0;
    private static final int Probe = 0x0F;
    private static final int Bake = 0x18;
    private static final int Convection = 0x10;
    private static final int Warm = 0xA0;
    private static final int Temp = 0x08;
    private static final int Up = 0x03;
    private static final int Down = 0x05;
    private static final int Timer = 0x0C;
    private static final int Enter = 0x2C;
    private static final int Stop = 0x3C;

    private static int OvenTemp = 350;
    private static int OvenTime = 30;


    public OvenControl() throws IOException {
        super(MAC);
    }

    public boolean preHeat(int temp) throws IOException {
        int roundedTemp;
        roundedTemp = (int) Math.floor((temp / 25.0)) * 25;

        //Send Bake Instruct
        Log.d(TAG, "Bake");
        sendData(Bake);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        if(roundedTemp - OvenTemp > 0)
        {
            //For every interation of 5, loop and send an instruct
            for (int i = 0; i < (roundedTemp - OvenTemp); i+=25) {
                Log.d(TAG, "Up");
                sendData(Up); //Raise temp by 5
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
        else if(roundedTemp - OvenTemp < 0)
        {
            //For every interation of 5, loop and send an instruct
            for (int i = 0; i < (Math.abs(roundedTemp - OvenTemp)); i+=25) {
                Log.d(TAG, "Down");
                sendData(Down); //decrease temp by 5
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }

        Log.d(TAG, "Enter");
        sendData(Enter);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        OvenTemp = roundedTemp;
        return true;
    }
   public boolean stopOven() throws IOException {
       sendData(Stop);
       return true;
   }
   public boolean timer(int time) throws IOException{
       Log.d(TAG, "Timer");
       sendData(Timer);
       try {
           Thread.sleep(1000);
       } catch (InterruptedException e) {
       }

       if(time - OvenTemp > 0)
       {
           //For every interation of 5, loop and send an instruct
           for (int i = 0; i < (time - OvenTime); i+=1) {
               Log.d(TAG, "Up");
               sendData(Up); //Raise temp by 1
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
               }
           }
       }
       else if(time - OvenTime < 0)
       {
           //For every interation of 5, loop and send an instruct
           for (int i = 0; i < (Math.abs(time - OvenTime)); i+=1) {
               Log.d(TAG, "Down");
               sendData(Down); //decrease time by 1
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
               }
           }
       }

       Log.d(TAG, "Enter");
       sendData(Enter);
       try {
           Thread.sleep(1000);
       } catch (InterruptedException e) {
       }
       OvenTime = time;
        return true;
   }


    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
}
