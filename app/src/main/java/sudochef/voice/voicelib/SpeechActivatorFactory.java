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
        String[] words = {"next", "back", "repeat"};
        speechActivator = new WordActivator(context, callback, words);
        return speechActivator;
    }
    
    public static String getLabel(Context context, SpeechActivator speechActivator)
    {
        String label = "Listening";
        return label;
    }

}
