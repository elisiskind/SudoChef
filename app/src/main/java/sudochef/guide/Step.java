package sudochef.guide;

import android.content.Context;

public class Step {

    protected String instructionText;
    protected StepType stepType;
    protected Context context;
    protected Boolean executed;

    enum StepType {
        PREHEAT, INSTRUCTION, NOTIFY, FEEDBACK, HOTPLATE, END
    }

    public Step(String text){
        executed = false;
        instructionText = text;
        this.stepType = StepType.INSTRUCTION;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void execute(){
        executed = true;
    };

    public Context getContext() {
        return context;
    }

    public String getText(){
        return instructionText;
    }

    public StepType getType() {
        return stepType;
    }
}
