package com.example.with_you;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.widget.Toast;

public class BackgroundProcess extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Ringtone ringtone= RingtoneManager.getRingtone(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));



        Toast.makeText(context, "BackGround Process", Toast.LENGTH_SHORT).show();

        ringtone.play();

        SystemClock.sleep(2000);

        ringtone.stop();
    }
}
