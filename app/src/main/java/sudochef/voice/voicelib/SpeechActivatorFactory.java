package sudochef.voice.voicelib;

import android.content.Context;

/**
 * help create {@link SpeechActivator}s based on a type from the resources
 * @author Greg Milette &#60;<a href="mailto:gregorym@gmail.com">gregorym@gmail.com</a>&#62;
 */
public class SpeechActivatorFactory
{
    public static SpeechActivator createSpeechActivator(Context context, 
            SpeechActivationListener callback, String type)
    {
        SpeechActivator speechActivator = null;

        speechActivator = new WordActivator(context, callback, "next");
        return speechActivator;
    }
    
    public static String getLabel(Context context, SpeechActivator speechActivator)
    {
        String label = "Listening";
//        if (speechActivator == null)
//        {
//            label = context.getString(R.string.speech_activation_button);
//        }
//        else if (speechActivator instanceof WordActivator)
//        {
//            label = context.getString(R.string.speech_activation_speak);
//        }
//        else if ((speechActivator instanceof MovementActivator))
//        {
//            label = context.getString(R.string.speech_activation_movement);
//        }
//        else if ((speechActivator instanceof ClapperActivator))
//        {
//            label = context.getString(R.string.speech_activation_clap);
//        }
//        else
//        {
//            label = context.getString(R.string.speech_activation_button);
//        }
        return label;
    }

}
