package sudochef.guide;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import sdp.sudochef.R;
import sudochef.parser.KeywordParser;
import sudochef.guide.voice.processing.ReadText;
import sudochef.guide.voice.voicelib.SpeechActivationService;
//import sudochef.main.NotificationPublisher;


public class GuideActivity extends Activity {
    ReadText speaker;
    ReceiveMessages myReceiver = null;
    Boolean myReceiverIsRegistered = false;

    private String TAG = "SC.Guide";
    private ViewFlipper viewFlipper;
    private int index;
    private Step[] recipe;
    private Button next, prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speaker = new ReadText(this);
        myReceiver = new ReceiveMessages();
        String result = "Speak Hello";
        Intent i = SpeechActivationService.makeStartServiceIntent(GuideActivity.this, result);
        GuideActivity.this.startService(i);

        setContentView(R.layout.activity_guide);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewAnimatorSteps);
        next = (Button) findViewById(R.id.next_button);
        prev = (Button) findViewById(R.id.back_button);

        // Get recipe from extras (or create sample one)
        constructRecipe();
        addStepsToFlipper();
        start();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!myReceiverIsRegistered) {
            registerReceiver(myReceiver, new IntentFilter("sudochef.guide.voice.processing"));
            myReceiverIsRegistered = true;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (myReceiverIsRegistered) {
            unregisterReceiver(myReceiver);
            myReceiverIsRegistered = false;
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        String result = "Speak Hello";
        Intent i = SpeechActivationService.makeStartServiceIntent(GuideActivity.this, result);
        GuideActivity.this.stopService(i);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        speaker.kill();
    }
    public class ReceiveMessages extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String wordSaid = intent.getStringExtra("SUCCESS WORD");
            if(wordSaid.equals("next")) {
                next(null);
            }
            else if(wordSaid.equals("back")){
                prev(null);
            }
            else if(wordSaid.equals("repeat")){
                repeat(null);
            }
            String result = "Speak Hello";
            Intent i = SpeechActivationService.makeStartServiceIntent(GuideActivity.this, result);
            GuideActivity.this.startService(i);
        }
    }

    private void constructRecipe() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] recipeSteps = (String[]) extras.get("recipe");
            recipe = new KeywordParser(recipeSteps).parseRecipe();
        } else {
            // DO a test recipe instead
            makeTestRecipe();
        }
    }

    private void addStepsToFlipper() {
        int stepNum = 1;

        for(Step step : recipe) {
            step.setContext(getApplicationContext());
            viewFlipper.addView(makeWidget(step.getText(), stepNum++));
        }
    }

    private void makeTestRecipe() {
        recipe = new Step[3];

        recipe[0] = new PreheatStep("Preheat oven to 450.", 450);
        recipe[1] = new Step("Chop onions, garlic, and tomatoes and add to saucepan.");
        recipe[2] = new HotPlateStep("Boil two quarts of water.", HotPlateStep.HIGH);
    }

    public void start() {
        index = 0;
        prev.setEnabled(false);
        speaker.setQueue(recipe[index].getText());
        recipe[index].execute();
    }

    public void prev(View v) {
        if(index == -1) {
            start();
            return;
        }

        if(index > 0) {
            index--;
            flipBackward();
            next.setEnabled(true);
            speaker.read(recipe[index].getText());
        }

        if(index == 0) {
            prev.setEnabled(false);
        }
    }

    public void repeat(View v){
        speaker.read(recipe[index].getText());
    }

    public void next(View v) {
        if(index < recipe.length) {
            index++;
            flipForward();
            prev.setEnabled(true);
            speaker.read(recipe[index].getText());
            recipe[index].execute();
        }

        if(index == recipe.length - 1) {
            next.setEnabled(false);
        }
    }

    private void flipForward() {
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
        viewFlipper.showNext();
    }

    private void flipBackward() {
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
        viewFlipper.showPrevious();
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
                next(v);
            }

        });
        return content;
    }

}
