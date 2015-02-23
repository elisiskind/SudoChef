package sudochef.guide;

import android.util.Log;

public class PreheatStep extends Step {

    int temp;

    public PreheatStep(int temp) {
        this.temp = temp;
    }

    @Override
    public void execute() {
        Log.i("Preheat", "Sending preheat signal: " + temp);
    }

    @Override
    public String getText() {
        return "Preheat oven to " + temp;
    }

}
