package sudochef.guide;

public class Step {

    protected String instructionText;
    protected StepType stepType;

    enum StepType {
        PREHEAT, INSTRUCTION, NOTIFY, FEEDBACK, HOTPLATE, END
    }

    public Step(String text){
        instructionText = text;
        this.stepType = StepType.INSTRUCTION;
    }


    public void execute(){

    };

    public String getText(){
        return instructionText;
    }

    public StepType getType() {
        return stepType;
    }
}
