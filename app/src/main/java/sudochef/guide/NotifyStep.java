package sudochef.guide;

public class NotifyStep extends Step {

    private int time;


    private String actionVerb;
    private String actionCompleted;

    public NotifyStep(String text, int timeInSec, String actionVerb, String actionCompleted)
    {
        super(text);
        time = timeInSec;
        this.actionVerb = actionVerb;
        this.actionCompleted = actionCompleted;
        this.stepType = StepType.NOTIFY;
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getText() {
        return actionVerb + " for " + readableTime();
    }

    public String getNotificationText() {
        return "Done " + actionCompleted;
    }

    private String readableTime() {
        int seconds = (int) time % 60;
        int minutes = (int) ((time / 60) % 60);
        int hours   = (int) ((time / (60*60)) % 24);

        String output = "";

        if(hours > 0){
            String unit = (hours > 1) ? " hours" : " hour";
            output += hours + unit;
            if(minutes > 0) output += " and ";
        }
        if(minutes > 0){
            String unit = (minutes > 1) ? " minutes" : " minute";
            output += minutes + unit;
        }
        if(seconds > 0){
            String unit = (seconds > 1) ? " seconds" : " second";
            output += seconds + unit;
            if(minutes > 0) output += " and ";
        }

        return output;
    }

    public int getTime() {
        return time;
    }
}
