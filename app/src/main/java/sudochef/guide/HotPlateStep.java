package sudochef.guide;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import sudochef.guide.bluetooth.HotplateControl;

public class HotPlateStep extends Step {

    public int setting;

    public final static int OFF = 0;
    public final static int LOW = 1;
    public final static int HIGH = 2;

    private String TAG = "SC.HotPlateStep";

    public HotPlateStep(String text, int setting) {
        super(text);
        this.setting = setting;
    }

    public String getTemp() {
        switch(this.setting) {
            case OFF: return "off";
            case LOW: return "low";
            case HIGH: return "high";
        }

        return "";
    }

    @Override
    public void execute() {
        Log.d("SC.HotPlateStep", "SUP");

        if(!this.executed) {
          //  super.execute();

            Toast.makeText(getContext(), "Sending preheat signal to hot plate", Toast.LENGTH_SHORT).show();
            new AsyncPreheatSignal().execute(setting);
            Log.i("Preheat", "Sending hot plate preheat signal: " + setting);
        }
    }

    private class AsyncPreheatSignal extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... setting) {
            Log.d(TAG, "Starting Async preheat hot plate signal: " + setting[0]);

            Boolean success;

            try {
                HotplateControl hpController = new HotplateControl();
                switch(setting[0]) {
                    case OFF: hpController.off();
                        break;
                    case LOW: hpController.low();
                        break;
                    case HIGH: hpController.high();
                        break;
                }
                hpController.close();
                success = true;
                Log.d(TAG, "Sent a preheat signal");
            } catch (Exception e) {
                Log.d(TAG, "Preheat exception caught");
                success = false;
            }

            return success;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean result) {

            Toast toast;
            int duration = Toast.LENGTH_SHORT;

            if(result) {
                Log.d(TAG, "The hot plate preheated in theory");

                CharSequence text = "Hot plate set to " + getTemp();
                toast = Toast.makeText(getContext(), text, duration);

            } else {
                Log.e(TAG, "Failed to preheat hot plate");

                CharSequence text = "Failed to send preheat signal to hot plate";
                toast = Toast.makeText(getContext(), text, duration);
            }

            toast.show();

        }
    }

}
