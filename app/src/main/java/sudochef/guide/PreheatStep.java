package sudochef.guide;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import sudochef.bluetooth.OvenControl;

public class PreheatStep extends Step {

    public int temp;

    private String TAG = "SC.PreheatStep";

    public PreheatStep(String text, int temp) {
        super(text);
        this.temp = temp;
    }

    public int getTemp() {
        return this.temp;
    }

    @Override
    public void execute() {
        new AsyncPreheatSignal().execute(temp);
        Log.i("Preheat", "Sending preheat signal: " + temp);
    }

    @Override
    public String getText() {
        return this.instructionText;
    }

    private class AsyncPreheatSignal extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... temp) {
            Log.d(TAG, "Starting Async preheat oven signal at " + temp[0] + " degrees.");

            Boolean success;

            try {
                OvenControl ovenController = new OvenControl();
                ovenController.preHeat(temp[0]);
                ovenController.close();
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
                Log.d(TAG, "The oven preheated in theory");

                CharSequence text = "Oven preheated to " + getTemp();
                toast = Toast.makeText(getContext(), text, duration);

            } else {
                Log.e(TAG, "Failed to preheat");

                CharSequence text = "Failed to send preheat signal";
                toast = Toast.makeText(getContext(), text, duration);
            }

            toast.show();

        }
    }

}
