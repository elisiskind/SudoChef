package sudochef.guide;

public abstract class Step {

    enum StepType {
        PREHEAT, INSTRUCTION, NOTIFY, FEEDBACK, HOTPLATE, END
    }

    protected StepType stepType;

    abstract public void execute();
    abstract public String getText();

    public StepType getType() {
        return stepType;
    }
}
