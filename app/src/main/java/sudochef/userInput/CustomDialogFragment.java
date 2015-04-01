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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import sdp.sudochef.R;
import sudochef.inventory.CommonFood;


/**
 * Created by clive on 2/12/14.
 */
public class CustomDialogFragment extends DialogFragment {

    LayoutInflater inflater;
    //set up interface to communicate with host activity
    public interface customDialogListener {
        public void onOkay(String[] contactDetails);
        public void onCancel();
    }

    customDialogListener dialogListener;
    int resource;
    EditText ItemNameVal;

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
        inflater = getActivity().getLayoutInflater();
        View v= inflater.inflate(resource, null);

        // reference to the edittext
        //inflate the layout

        final AutoCompleteTextView ed1 = (AutoCompleteTextView) v.findViewById(R.id.ItemNameVal);
        ed1.setAdapter(new ArrayAdapter<String>(v.getContext(), R.layout.autocomplete, CommonFood.names()));
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // get the users details and pass back to the activity
                        //here we pass dummy details
                        final AutoCompleteTextView ed1 = (AutoCompleteTextView) getDialog().findViewById(R.id.ItemNameVal);
                        final EditText ed2 = (EditText) getDialog().findViewById(R.id.AmountVal);
                        final Spinner sp1 = (Spinner) getDialog().findViewById(R.id.Units_spinner);
                        final EditText ed4 = (EditText) getDialog().findViewById(R.id.ExpireVal);
                        Editable temp1 = ed1.getText();
                        Editable temp2 = ed2.getText();
                        Editable temp4 = ed4.getText();
                        dialogListener.onOkay(new String[]{temp1.toString(), temp2.toString(), sp1.getSelectedItem().toString(), temp4.toString()});
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

