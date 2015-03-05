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
            new OvenControl().preHeat(temp);

            CharSequence text = "Oven preheated to " + this.temp;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this.context, text, duration);
            toast.show();

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
                new OvenControl().preHeat(temp[0]);
            } catch (Exception e) {
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
            Log.d(TAG, "Async HTTP GET for yummmly search call returned.");
            try {

                CharSequence text = "Oven preheated to " + getTemp();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
