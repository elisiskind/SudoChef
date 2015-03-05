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
        try {
            new AsyncPreheatSignal().execute(temp);

        } catch(Exception e) {

        }
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

            try {
                OvenControl ovenController = new OvenControl();
                ovenController.preHeat(temp[0]);
                ovenController.close();
            } catch (Exception e) {

                Log.e(TAG, "Failed to preheat");

                CharSequence text = "Failed to send preheat signal";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();
            }

            return true;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean result) {
            Log.d(TAG, "The oven preheated in theory");

            CharSequence text = "Oven preheated to " + getTemp();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getContext(), text, duration);
            toast.show();
        }
    }

}
