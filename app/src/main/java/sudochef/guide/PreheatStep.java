package sudochef.guide;

import android.util.Log;
import android.widget.Toast;

import sudochef.bluetooth.OvenControl;

public class PreheatStep extends Step {

    int temp;

    public PreheatStep(String text, int temp) {
        super(text);
        this.temp = temp;
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
            CharSequence text = "Failed to send preheat signal";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this.context, text, duration);
            toast.show();
        }
        Log.i("Preheat", "Sending preheat signal: " + temp);
    }

    @Override
    public String getText() {
        return this.instructionText;
    }

}
