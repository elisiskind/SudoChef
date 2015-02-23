package sudochef.guide;

public class InstructionStep extends Step {

    String instructionText;

    public InstructionStep(String text) {
        instructionText = text;
        this.stepType = StepType.INSTRUCTION;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getText() {
        return instructionText;
    }


}
