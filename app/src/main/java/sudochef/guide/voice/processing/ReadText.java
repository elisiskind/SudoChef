package sudochef.guide.voice.processing;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.Locale;

public class ReadText implements OnInitListener
{
    private static final String TAG = "ReadText";
	TextToSpeech tts;
    boolean initComplete = false;
    String queued;

	public ReadText(Context t)
	{
		tts = new TextToSpeech(t, this);
		tts.setLanguage(Locale.getDefault());
	}

    public void setQueue(String s){
        queued = s;
    }
	
	@Override
	public void onInit(int status) {
		if (status==TextToSpeech.SUCCESS) {
            initComplete = true;
			Log.d(TAG, "Text to speech Initialized");

			tts.setLanguage(Locale.getDefault());
            read(queued);
		
		} else {
			Log.d(TAG, "Text to speech FAILED init");
			tts = null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void read(String text)
	{
//        Log.d(TAG, "READING TEXT");
//		 if (!tts.isSpeaking()) {
//             Log.d(TAG, "Speaking" + text);
//		     tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//		 }
//		 else
//		 {
//			 Log.d(TAG, "tts is already speaking");
//		 }

        while(!initComplete){

        }

        new AsyncReadText().execute(text);
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

    private class AsyncReadText extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... text) {
            try {
                Log.d(TAG, "READING TEXT");
                while(tts.isSpeaking()){
                    wait();
                }
                if (!tts.isSpeaking()) {
                    Log.d(TAG, "Speaking" + text[0]);
                    tts.speak(text[0], TextToSpeech.QUEUE_FLUSH, null);
                }
                else
                {
                    Log.d(TAG, "tts is already speaking");
                }
            } catch (Exception e) {
            }

            return true;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean result) {
            Log.d(TAG, "Post Execute");
            try {

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
