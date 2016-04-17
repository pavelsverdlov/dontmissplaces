package svp.com.opengpstracker.streaming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import svp.com.dontmissplaces.R;
import svp.com.opengpstracker.util.Constants;

public class VoiceOver extends BroadcastReceiver implements TextToSpeech.OnInitListener
{
    private static VoiceOver sVoiceOver = null;
    private static final String TAG = "OGT.VoiceOver";

    public static synchronized void initStreaming(Context ctx)
    {
        if( sVoiceOver != null )
        {
            shutdownStreaming(ctx);
        }
        sVoiceOver = new VoiceOver(ctx);

        IntentFilter filter = new IntentFilter(Constants.STREAMBROADCAST);
        ctx.registerReceiver(sVoiceOver, filter);
    }

    public static synchronized void shutdownStreaming(Context ctx)
    {
        if( sVoiceOver != null )
        {
            ctx.unregisterReceiver(sVoiceOver);
            sVoiceOver.onShutdown();
            sVoiceOver = null;
        }
    }

    private TextToSpeech mTextToSpeech;
    private int mVoiceStatus = -1;
    private Context mContext;

    public VoiceOver(Context ctx)
    {
        mContext = ctx.getApplicationContext();
        mTextToSpeech = new TextToSpeech(mContext, this);
    }

    public void onInit(int status)
    {
        mVoiceStatus = status;
    }

    private void onShutdown()
    {
        mVoiceStatus = -1;
        mTextToSpeech.shutdown();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if( mVoiceStatus == TextToSpeech.SUCCESS )
        {
            int meters = intent.getIntExtra(Constants.EXTRA_DISTANCE, 0);
            int minutes = intent.getIntExtra(Constants.EXTRA_TIME, 0);
            String myText = context.getString(R.string.voiceover_speaking, minutes, meters);
            mTextToSpeech.speak(myText, TextToSpeech.QUEUE_ADD, null);
        }
        else
        {
            Log.w(TAG, "Voice stream failed TTS not ready");
        }
    }
}