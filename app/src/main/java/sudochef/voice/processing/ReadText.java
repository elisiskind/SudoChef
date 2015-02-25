package sudochef.voice.processing;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class ReadText implements OnInitListener
{
	TextToSpeech tts;

	public ReadText(Context t)
	{
		tts = new TextToSpeech(t, this);
		tts.setLanguage(Locale.getDefault());
	}
	
	@Override
	public void onInit(int status) {
		if (status==TextToSpeech.SUCCESS) {
			Log.d("SUDO", "Text to speech SUCCESS");
		
			tts.setLanguage(Locale.getDefault());
		
		} else {
			Log.d("SUDO", "Text to speech FAILED");
			tts = null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void read(String text)
	{
		Log.d("SUDO", "READING TEXT");
		 if (!tts.isSpeaking()) {
		        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		 }
		 else
		 {
			 Log.d("SUDO", "tts is speaking");
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
}
