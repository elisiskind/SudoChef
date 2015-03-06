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
        if(!this.executed) {
            super.execute();
            Toast.makeText(getContext(), "Sending preheat signal to oven", Toast.LENGTH_SHORT).show();
            new AsyncPreheatSignal().execute(temp);
            Log.i("Preheat", "Sending preheat signal: " + temp);
        }
    }

    private void showToast(Boolean success) {

        Toast toast;
        int duration = Toast.LENGTH_SHORT;

        if(success) {
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
            showToast(result);
        }
    }

}
