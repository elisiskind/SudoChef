package sudochef.guide.voice.processing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class VoiceBroadcastReciever extends BroadcastReceiver {

    private static final String TAG =
            "VoiceBroadcastReciever";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "BC Recieved");
		
	}

}
