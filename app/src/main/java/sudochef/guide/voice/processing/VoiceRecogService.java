package sudochef.guide.voice.processing;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

public class VoiceRecogService extends Service
{
    protected static AudioManager mAudioManager; 
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));

    protected boolean mIsListening;
    protected volatile boolean mIsCountDownOn;
    private static boolean mIsStreamSolo;

    public static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("SUDO", "onCreate");
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                         RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                                         this.getPackageName());
    }

    protected static class IncomingHandler extends Handler
    {
        private WeakReference<VoiceRecogService> mtarget;

        IncomingHandler(VoiceRecogService target)
        {
        	 Log.d("SUDO", "IncomingHandler");
            mtarget = new WeakReference<VoiceRecogService>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {
            final VoiceRecogService target = mtarget.get();
            Log.d("SUDO", "handleMessage");
            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:

                	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    {
                        // turn off beep sound  
                        if (!mIsStreamSolo)
                        {
                            mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);
                            mIsStreamSolo = true;
                        }
                    }
                     if (!target.mIsListening)
                     {
                     	Log.d("SUDO", "message start listening");
                         target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                         target.mIsListening = true;
                        //Log.d(TAG, "message start listening"); //$NON-NLS-1$
                     }
                     break;

                 case MSG_RECOGNIZER_CANCEL:
                    if (mIsStreamSolo)
                   {
                        mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
                        mIsStreamSolo = false;
                   }
                      target.mSpeechRecognizer.cancel();
                      target.mIsListening = false;
                      //Log.d(TAG, "message canceled recognizer"); //$NON-NLS-1$
                      break;
             }
       } 
    } 

    // Count down timer for Jelly Bean work around
    protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(5000, 5000)
    {

        @Override
        public void onTick(long millisUntilFinished)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFinish()
        {
            mIsCountDownOn = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try
            {
                mServerMessenger.send(message);
                message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {

            }
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("SUDO", "Service Destroyed");
        if (mIsCountDownOn)
        {
            mNoSpeechCountDown.cancel();
        }
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
        	 Log.d("SUDO", "BeginOfSpeech");
            // speech input will be processed, so there is no need for count down anymore
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }               
            //Log.d(TAG, "onBeginingOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {
    		Log.d("SUDO", "onBufferReceived");
    		Log.d("SUDO", "Buffer" + new String(buffer));
        }

        @Override
        public void onEndOfSpeech()
        {
            Log.d("SUDO", "onEndOfSpeech"); //$NON-NLS-1$
         }

        @Override
        public void onError(int error)
        {
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
             mIsListening = false;
             Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
             try
             {
                    mServerMessenger.send(message);
             }
             catch (RemoteException e)
             {

             }
//            Log.d("SUDO", "error"); //$NON-NLS-1$
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {
        	Log.d("SUDO", "Event");
        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {
        	Log.d("SUDO", "PartialResults");
        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                mIsCountDownOn = true;
                mNoSpeechCountDown.start();

            }
            Log.d("SUDO", "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            Log.d("SUDO", "onResults"); //$NON-NLS-1$

        }

        @Override
        public void onRmsChanged(float rmsdB)
        {
        	Log.d("SUDO", "RMS Changed");
        }

    }

	@Override
	public IBinder onBind(Intent intent) {
	    Log.d("SUDO", "onBind");  //$NON-NLS-1$

	    return mServerMessenger.getBinder();
	}
}
