package sudochef.recipe.parsing;

import android.util.Log;

import java.util.ArrayList;

import sudochef.guide.InstructionStep;
import sudochef.guide.PreheatStep;
import sudochef.guide.Step;

/**
 * Created by eli on 3/1/2015.
 */
public class KeywordParser {

    private String[] stringStepList;
    private String TAG = "SC.KeywordParser";

    public KeywordParser(String[] steps) {
        stringStepList = steps;
    }

    public Step[] parseRecipe() {

        ArrayList<Step> recipe = new ArrayList<>(stringStepList.length);

        for(String stepString : stringStepList ) {
            recipe.add(parse(stepString));
        }

        return recipe.toArray(new Step[recipe.size()]);
    }

    private Step parse(String stepString){

        Log.d(TAG, stepString);

        String lcStep = stepString.toLowerCase();

        /*
        if(lcStep.contains("bake")) {
            if(lcStep.contains("minutes") || lcStep.contains("hour")){
                String[] splitArray = lcStep.split("\\s+");

                int timeInMinutes = 0;

                for(int i = 0; i < splitArray.length; i++) {
                    if(splitArray[i].contains("hour")){

                    }
                }
                for(int i = 0; i < splitArray.length; i++){
                    if(splitArray[i].contains("minute")){

                    }
                }
            }
        } else */ if(lcStep.contains("preheat") && lcStep.contains("oven")) {

            int start = lcStep.indexOf("preheat");
            String temp = "";
            boolean inNumber = false;

            for(int i = start; i < lcStep.length(); i++){
                if(Character.isDigit(lcStep.charAt(i))){
                    Log.i(TAG, "Added char: " + lcStep.charAt(i));
                    temp += lcStep.charAt(i);
                    inNumber = true;
                } else if (inNumber == true) {
                    break;
                }
            }

            if(temp != "") { return new PreheatStep(stepString, Integer.parseInt(temp)); }
        }

        return new InstructionStep(stepString);
    }

}
