package sudochef.userInput;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;



/**
 * Created by clive on 2/12/14.
 */
public class CustomActivity extends FragmentActivity implements CustomDialogFragment.customDialogListener {

    private String TAG = "dialogs";

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean v = this.getIntent().getBooleanExtra("VerboseFlag", true);
        
        /*button - show the custom dialog*/
//        Button buttonShowCustomDialog = (Button) findViewById(R.id.button);
//        buttonShowCustomDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
                showCustomDialog(v);
//            }
//        });
    }


    private void showCustomDialog(boolean v) {
        DialogFragment newFragment = new CustomDialogFragment();
        newFragment.show(getSupportFragmentManager(), "CustomDialogFragment");
    }

    @Override
    public void onOkay(String contactDetails) {
        Toast.makeText(this, "Contact details: " + contactDetails, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCancel() {
        Toast.makeText(this, "You cancelled the dialog", Toast.LENGTH_SHORT).show();
    }
}
