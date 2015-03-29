package sudochef.userInput;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sdp.sudochef.R;

/**
 * Created by LH-21 on 3/27/2015.
 */
public class AmountDialogFragment extends DialogFragment {

    LayoutInflater inflater;
    //set up interface to communicate with host activity
    public interface amountDialogListener {
        public void onOkay(String[] contactDetails);
        public void onCancel();
    }

    amountDialogListener dialogListener;
    int resource;
    EditText ItemNameVal;

    public AmountDialogFragment() {
        // TODO Auto-generated constructor stub
        resource = R.layout.field_single_string;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // make sure host activity implements the callback interface
        try {
            // Instantiate the dialogListener so we can send clicks to the host
            dialogListener = (amountDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity isn't implementing the interface, so throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement customDialogListener");
        }
    }

    /*execute when dialog cancelled*/
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        //on pressing back button, touch screen outside dialog, press cancel button in dialog
        Toast.makeText(getActivity(), "The dialog has been cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        inflater = getActivity().getLayoutInflater();
        View v= inflater.inflate(resource, null);
        // reference to the edittext
        //inflate the layout
        builder.setView(inflater.inflate(resource, null))
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // get the users details and pass back to the activity
                        //here we pass dummy details
                        final EditText ed = (EditText) getDialog().findViewById(R.id.AmountVal);
                        Editable temp = ed.getText();
                        dialogListener.onOkay(new String[]{temp.toString()});
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AmountDialogFragment.this.getDialog().cancel();
                        dialogListener.onCancel();
                    }
                });


        return builder.create();
    }
}

