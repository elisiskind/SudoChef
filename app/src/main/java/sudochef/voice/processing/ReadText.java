package sudochef.voice.processing;

import java.util.Locale;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class ReadText /*extends AsyncTask<String, int, int>*/ implements OnInitListener
{
    private static final String TAG = "ReadText";
	TextToSpeech tts;

	public ReadText(Context t)
	{
		tts = new TextToSpeech(t, this);
		tts.setLanguage(Locale.getDefault());
	}
	
	@Override
	public void onInit(int status) {
		if (status==TextToSpeech.SUCCESS) {
			Log.d(TAG, "Text to speech Initialized");
		
			tts.setLanguage(Locale.getDefault());
		
		} else {
			Log.d(TAG, "Text to speech FAILED init");
			tts = null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void read(String text)
	{
        Log.d(TAG, "READING TEXT");
        while(tts.isSpeaking()){}
		 if (!tts.isSpeaking()) {
             Log.d(TAG, "Speaking" + text);
		     tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		 }
		 else
		 {
			 Log.d(TAG, "tts is already speaking");
		 }
	}
	
	protected void finalize() {
		if (tts!=null) {
		
			tts.stop();
			
			tts.shutdown();
			
		}
	}

    public void kill()
    {
        if (tts!=null) {

            tts.stop();

            tts.shutdown();

        }
    }

//    @Override
//    protected int doInBackground(String... params) {
//        Log.d(TAG, "READING TEXT");
//        while(tts.isSpeaking()){}
//        if (!tts.isSpeaking()) {
//            Log.d(TAG, "Speaking" + params[0]);
//            tts.speak(params[0], TextToSpeech.QUEUE_FLUSH, null);
//        }
//        else
//        {
//            Log.d(TAG, "tts is already speaking");
//        }
//    }
}
