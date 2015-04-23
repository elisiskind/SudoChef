package sudochef.guide;

import android.content.Context;

abstract public class Step {

    protected String instructionText;
    protected Context context;
    protected Boolean executed;
    private String TAG = "SC.Step";

    public Step(String text){
        executed = false;
        instructionText = text;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    abstract public void execute();

    public Context getContext() {
        return context;
    }

    public String getText(){
        return instructionText;
    }

}
