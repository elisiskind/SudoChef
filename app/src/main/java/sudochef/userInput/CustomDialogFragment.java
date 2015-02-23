package sudochef.userInput;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.Toast;

import sdp.sudochef.R;


/**
 * Created by clive on 2/12/14.
 */
public class CustomDialogFragment extends DialogFragment {

    //set up interface to communicate with host activity
    public interface customDialogListener {
        public void onOkay(String contactDetails);
        public void onCancel();
    }

    customDialogListener dialogListener;
    int resource;

    public CustomDialogFragment() {
        // TODO Auto-generated constructor stub
        resource = R.layout.field_verbose;
    }

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // make sure host activity implements the callback interface
        try {
            // Instantiate the dialogListener so we can send clicks to the host
            dialogListener = (customDialogListener) activity;
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        
        //inflate the layout
        builder.setView(inflater.inflate(resource, null))
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // get the users details and pass back to the activity
                        //here we pass dummy details
                        dialogListener.onOkay("jack@sprat.com|2345678912");
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomDialogFragment.this.getDialog().cancel();
                        dialogListener.onCancel();
                    }
                });
        
        
        return builder.create();
    }
}

