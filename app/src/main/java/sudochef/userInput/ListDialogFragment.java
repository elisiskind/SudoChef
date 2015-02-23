package sudochef.userInput;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by clive on 2/12/14.
 */
public class ListDialogFragment extends DialogFragment {

    public interface listDialogListener {
        public void onClick(int i);
    }

    listDialogListener dialogListener;
    String [] input;
    
    public ListDialogFragment(String [] s)
    {
    	input = s;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ensure that the host activity implements the callback interface
        try {
            // Instantiate the listDialogListener so we can send clicks to the host
            dialogListener = (listDialogListener) activity;
        } catch (ClassCastException e) {
            // if the activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement listDialogListener");
        }
    }

    /*execute when dialog cancelled*/
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        //on pressing back button or touching screen outside dialog
        Toast.makeText(getActivity(), "The dialog has been cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        set the title for the dialog
        builder.setTitle("Select one product")
//                set up the items for the list - we use an array
                .setItems(input, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //send the selected index to the activity
                        dialogListener.onClick(i);
                    }
                });

        return builder.create();
    }
}
