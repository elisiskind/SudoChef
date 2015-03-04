package sudochef.bluetooth;

import android.bluetooth.BluetoothClass;
import android.util.Log;

import java.io.IOException;

/**
 * Created by LH-21 on 3/3/2015.
 */
public class OvenControl extends DeviceControl {
    private final String TAG = "OvenControl";
    private static final String MAC = "30:14:11:25:21:84";
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


    public OvenControl() throws IOException {
        super(MAC);
    }

    public boolean preHeat(int temp) throws IOException {
        double roundedTemp;
        roundedTemp = Math.floor((temp / 5.0)) * 5;

        //Send Bake Instruct
        sendData(Bake);

        if(roundedTemp - 350 > 0)
        {
            //For every interation of 5, loop and send an instruct
            for (int i = 0; i < (roundedTemp - 350) / 5; i++) {
                sendData(Up); //Raise temp by 5
            }
        }
        else if(roundedTemp - 350 < 0)
        {
            //For every interation of 5, loop and send an instruct
            for (int i = 0; i < (Math.abs(roundedTemp - 350)) / 5; i++) {
                sendData(Down); //Raise temp by 5
            }
        }

        return true;
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
}
