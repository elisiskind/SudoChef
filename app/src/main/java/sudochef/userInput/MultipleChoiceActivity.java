package sudochef.userInput;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class MultipleChoiceActivity extends FragmentActivity implements ListDialogFragment.listDialogListener {

    private String[] transportArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=this.getIntent().getExtras();
        transportArray=b.getStringArray("List");
        showListDialog();
    }
    
    private void showListDialog() {
        DialogFragment dialog = new ListDialogFragment(transportArray);
        dialog.show(getSupportFragmentManager(), "ListDialogFragment");
    }
    
	@Override
	public void onClick(int i) {
		// TODO Auto-generated method stub
//        Toast.makeText(this, "selected transport: "
//                + transportArray[i], Toast.LENGTH_SHORT).show();
        Intent resultData = new Intent();
        
        resultData.putExtra("Output", i);
        setResult(MultipleChoiceActivity.RESULT_OK, resultData);
        finish();
	}

}
