package sudochef.guide;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import sudochef.guide.bluetooth.OvenControl;

public class ToastStep extends Step {

    private String TAG = "SC.ToastStep";

    public ToastStep(String text) {
        super(text);
    }


    @Override
    public void execute() {
        if(!this.executed) {
            this.executed = true;
            Log.d("SC.ToastStep","SUP");
            Toast.makeText(getContext(), "Sending preheat signal to oven", Toast.LENGTH_SHORT).show();
            new AsyncToastSignal().execute();
            Log.i("Preheat", "Sending toast signal");
        }
    }

    private void showToast(Boolean success) {

        Toast toast;
        int duration = Toast.LENGTH_SHORT;

        if(success) {
            Log.d(TAG, "Oven set to toast");

            CharSequence text = "Oven set to toast";
            toast = Toast.makeText(getContext(), text, duration);

        } else {
            Log.e(TAG, "Failed to toast");

            CharSequence text = "Failed to send toast signal";
            toast = Toast.makeText(getContext(), text, duration);
        }

        toast.show();

    }


    private class AsyncToastSignal extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... temp) {
            Log.d(TAG, "Starting Async toast oven signal");

            Boolean success;

            try {
                OvenControl ovenController = new OvenControl();
                ovenController.toast();
                ovenController.close();
                success = true;
                Log.d(TAG, "Sent a toast signal");
            } catch (Exception e) {
                Log.d(TAG, "Toast exception caught");
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
