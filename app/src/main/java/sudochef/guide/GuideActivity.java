package sudochef.guide;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import sdp.sudochef.R;
import sudochef.guide.Step.StepType;
//import sudochef.main.NotificationPublisher;


public class GuideActivity extends Activity {

    private Recipe recipe;
    private ViewFlipper viewFlipper;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewAnimatorSteps);
        makeTestRecipe();
        count = 0;
        recipe.begin();
    }

    private void makeTestRecipe() {
        recipe = new Recipe();

        PreheatStep preheat = new PreheatStep(450);
        recipe.addStep(preheat);

        String s = "This is a sample instruction step. It will be read aloud.";
        InstructionStep inst = new InstructionStep(s);
        recipe.addStep(inst);

        NotifyStep notify = new NotifyStep(30, "Simmer soup", "Simmering soup");
        recipe.addStep(notify);
    }

    public void begin(View v) {
        executeNextStep(v);
    }

    public void executeNextStep(View v) {

        Step s;
        if (recipe.hasNext()) {

            s = recipe.nextStep();

            // Add next step to flipper
            String text = s.getText();
            viewFlipper.addView(makeWidget(text, count++));

            // Advance flipper
            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
            viewFlipper.showNext();

            if(s.getType() == StepType.NOTIFY) {
                NotifyStep ns = (NotifyStep) s;
                scheduleNotification(getNotification(ns.getNotificationText()), ns.getTime() * 1000);
                Log.d("NOTIFY", "Setting notification");
            }
            s.execute();
        } else {
            // Add end step to flipper
            viewFlipper.addView(makeWidget("Recipe completed", count++));

            // Advance flipper
            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
            viewFlipper.showNext();
        }
    }

    private LinearLayout makeWidget(String s, int step) {
        // Linear layout
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        // Create title and description
        TextView title = new TextView(this);
        title.setText("Step " + step);
        TextView description = new TextView(this);
        description.setText(s);
        description.setTextAppearance(this, R.style.boldText);

        // Add children and return
        content.addView(title);
        content.addView(description);
        content.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                executeNextStep(v);
            }

        });
        return content;
    }

    private void scheduleNotification(Notification notification, int delay) {
        /* TODO */
    /*
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        */
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Sudo Chef");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }
}
