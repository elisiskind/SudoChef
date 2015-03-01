package sudochef.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import sdp.sudochef.R;
import sudochef.userinput.MultipleChoiceActivity;


public class DeviceListActivity extends Activity {

    private static String TAG = "SC.DeviceList";
    public static String EXTRA_DEVICE_ADDRESS;
    // textview for connection status
    TextView textConnectionStatus;
    ListView pairedListView;


    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private static final int MULTICHOICE_REQ = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "Starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        textConnectionStatus = (TextView) findViewById(R.id.connecting);
        textConnectionStatus.setTextSize(40);

        // Initialize array adapter for paired devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);

        // Find and set up the ListView for paired devices
        pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
        Log.d(TAG, "Done with onCreate");
        Bundle b=new Bundle();
        String[] arrin = new String[2];
        arrin[0]="OVEN";
        arrin[1]="HOTPLATE";
        b.putStringArray("List", arrin);

        Intent intentMultiChoice = new Intent(DeviceListActivity.this, MultipleChoiceActivity.class);
        intentMultiChoice.putExtras(b);
        startActivityForResult(intentMultiChoice, MULTICHOICE_REQ);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        
        //It is best to check BT status at onResume in case something has changed while app was paused etc
        checkBTState();

        mPairedDevicesArrayAdapter.clear();// clears the array so items aren't duplicated when resuming from onPause

        textConnectionStatus.setText(" "); //makes the textview blank

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to pairedDevices list
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Add previously paired devices to the array
//        if (pairedDevices.size() > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
//            for (BluetoothDevice device : pairedDevices) {
//                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//            }
//        } else {
//            mPairedDevicesArrayAdapter.add("no devices paired");
//        }
    }

    //method to check if the device has Bluetooth and if it is on.
    //Prompts the user to turn it on if it is off
    private void checkBTState()
    {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter=BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (!mBtAdapter.isEnabled()) {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
        {
            textConnectionStatus.setText("Connecting...");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Bundle b=new Bundle();
            String[] arrin = new String[2];
            arrin[0]="OVEN";
            arrin[1]="HOTPLATE";
            b.putStringArray("List", arrin);

            Intent intentMultiChoice = new Intent(DeviceListActivity.this, MultipleChoiceActivity.class);
            intentMultiChoice.putExtras(b);
            startActivityForResult(intentMultiChoice, MULTICHOICE_REQ);
            // Make an intent to start next activity while taking an extra which is the MAC address.
          //  Intent i = new Intent(DeviceListActivity.this,MultipleChoiceActivity.class);
           // i.putExtra(EXTRA_DEVICE_ADDRESS, address);
           // startActivity(i);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Receiving Activity");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_CANCELED)
            if (requestCode == MULTICHOICE_REQ) {
                int choice = data.getIntExtra("Output", 0);
                if (choice == 0) {

                    String address = "30:14:11:25:21:84";
                    Intent i = new Intent(DeviceListActivity.this, SendDataOven.class);
                    i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                    startActivity(i);
                } else if (choice == 1) {
                    String address = "30:14:11:25:21:84";
                    Intent i = new Intent(DeviceListActivity.this, SendDataActivity.class);
                    i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                    startActivity(i);
                }
            }
        this.finish();
    }

}