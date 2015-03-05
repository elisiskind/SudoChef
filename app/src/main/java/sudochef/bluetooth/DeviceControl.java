package sudochef.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by LH-21 on 3/3/2015.
 */
public abstract class DeviceControl {
    private final String TAG = "DeviceControl";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //Constructor
    public DeviceControl(String MAC) throws IOException {

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        BluetoothDevice device = btAdapter.getRemoteDevice(MAC);
        //Attempt to create a bluetooth socket for comms
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e1) {
            Log.d(TAG, "ERROR - Could not create Bluetooth socket");
            throw e1;
        }

        // Establish the connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();        //If IO exception occurs attempt to close socket
                throw e;
            } catch (IOException e2) {
                Log.d(TAG, "ERROR - Could not close Bluetooth socket");
                throw e2;
            }
        }

        // Create a data stream so we can talk to the device
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.d(TAG, "ERROR - Could not create bluetooth outstream");
            btSocket.close();
            throw e;
        }
    }

    protected void sendData(int message) throws IOException {

        try {
            //attempt to place data on the outstream to the BT device
            Log.d(TAG, "Sending:" + message);
            outStream.write(message);
        } catch (IOException e) {
            //if the sending fails this is most likely because device is no longer there
            Log.d(TAG, "Could not write to bluetooth buffer");
            throw e;
        }
    }

    private void checkBTState() throws IOException{
        // Check device has Bluetooth and that it is turned on
        if(btAdapter==null) throw new IOException();
        else {
            if (btAdapter.isEnabled()) {

            } else {
                throw new IOException();
            }
        }
    }

    public void close(){
        try {
            btSocket.close();
        } catch (IOException e) {
            Log.d(TAG, "Could not close socket");
        }
        try {
            outStream.close();
        } catch (IOException e) {
            Log.d(TAG, "Could not close stream");
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        this.close();
    }

}
