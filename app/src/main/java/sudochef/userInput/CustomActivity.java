package sudochef.userInput;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;



/**
 * Created by clive on 2/12/14.
 */
public class CustomActivity extends FragmentActivity implements CustomDialogFragment.customDialogListener {

    private String TAG = "CustomActivity";
    private String[] transportArray;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Creating Custom Activity");
        super.onCreate(savedInstanceState);
        showCustomDialog();

    }


    private void showCustomDialog() {
        DialogFragment newFragment = new CustomDialogFragment();
        newFragment.show(getSupportFragmentManager(), "CustomDialogFragment");
    }

    private void stuff(String[] ItemName)
    {

        Intent resultData = new Intent();
        resultData.putExtra("Output", ItemName);
        setResult(CustomActivity.RESULT_OK, resultData);
        finish();
    }

    @Override
    public void onOkay(String[] ItemName) {
        Log.d(TAG, "onOkay");
        stuff(ItemName);
    }


    @Override
    public void onCancel() {
        Toast.makeText(this, "You cancelled the dialog", Toast.LENGTH_SHORT).show();
    }
}
