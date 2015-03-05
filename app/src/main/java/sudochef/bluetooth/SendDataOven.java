package sudochef.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import sdp.sudochef.R;

public class SendDataOven extends Activity {
    Button TOAST, BROIL, PROBE, BAKE, CONV, WARM, TEMP, UP, DOWN, TIMER, ENTER, STOP;
    //Memeber Fields
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

//    OvenControl oven = null;
    // UUID service - This is the type of Bluetooth device that the BT module is
    // It is very likely yours will be the same, if not google UUID for your manufacturer
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module
    public String newAddress = null;

    /** Called when the activity is first created. */
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data_oven);
        //Initialising buttons in the view
        //mDetect = (Button) findViewById(R.id.mDetect);
        TOAST = (Button) findViewById(R.id.TOAST);
        BROIL = (Button) findViewById(R.id.BROIL);
        PROBE = (Button) findViewById(R.id.PROBE);
        BAKE = (Button) findViewById(R.id.BAKE);
        CONV = (Button) findViewById(R.id.CONV);
        WARM = (Button) findViewById(R.id.WARM);
        TEMP = (Button) findViewById(R.id.TEMP);
        UP = (Button) findViewById(R.id.UP);
        DOWN = (Button) findViewById(R.id.DOWN);
        TIMER = (Button) findViewById(R.id.TIMER);
        ENTER = (Button) findViewById(R.id.ENTER);
        STOP = (Button) findViewById(R.id.STOP);
//        try {
//            oven = new OvenControl();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        TOAST.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0xFF);
//                    oven.preHeat(400);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        BROIL.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0xF0);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        PROBE.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x0F);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        BAKE.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x18);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        CONV.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x10);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        WARM.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0xA0);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        TEMP.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x08);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        UP.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x03);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        DOWN.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x05);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        TIMER.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x0C);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        ENTER.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x2C);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });

        STOP.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // sendData("1");
                try{
                    outStream.write(0x3F);
                }catch (IOException e) {
                    //if the sending fails this is most likely because device is no longer there
                    Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getBaseContext(), "low", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void onResume() {
        super.onResume();
        // connection methods are best here in case program goes into the background etc

        //Get MAC address from DeviceListActivity
        Intent intent = getIntent();
        newAddress = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        // Set up a pointer to the remote device using its address.
        BluetoothDevice device = btAdapter.getRemoteDevice(newAddress);

        //Attempt to create a bluetooth socket for comms
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e1) {
            Toast.makeText(getBaseContext(), "ERROR - Could not create Bluetooth socket", Toast.LENGTH_SHORT).show();
        }

        // Establish the connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();        //If IO exception occurs attempt to close socket
            } catch (IOException e2) {
                Toast.makeText(getBaseContext(), "ERROR - Could not close Bluetooth socket", Toast.LENGTH_SHORT).show();
            }
        }

        // Create a data stream so we can talk to the device
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR - Could not create bluetooth outstream", Toast.LENGTH_SHORT).show();
        }
        //When activity is resumed, attempt to send a piece of junk data ('x') so that it will fail if not connected
        // i.e don't wait for a user to press button to recognise connection failure
//        sendData("x");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Pausing can be the end of an app if the device kills it or the user doesn't open it again
        //close all connections so resources are not wasted

        //Close BT socket to device
        try     {
            btSocket.close();
        } catch (IOException e2) {
            Toast.makeText(getBaseContext(), "ERROR - Failed to close Bluetooth socket", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "ERROR - Device does not support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        try {
            //attempt to place data on the outstream to the BT device
            outStream.write(msgBuffer);
        } catch (IOException e) {
            //if the sending fails this is most likely because device is no longer there
            Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }



}
