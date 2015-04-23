package sudochef.guide;

import android.util.Log;

/**
 * Created by eli on 4/23/2015.
 */
public class InstructionStep extends Step{

    private String TAG = "SC.InstructionStep";

    public InstructionStep(String text) {
        super(text);
    }

    public void execute() {
        Log.i(TAG, "Executing instruction step");
        this.executed = true;
    }
}
