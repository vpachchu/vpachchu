package com.example.with_you;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            showToast(context,"Call started...");
        }
        else  if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
            showToast(context,"Call ended...");

        }
    }

    void showToast(Context context,String message)
    {
        Toast toast= Toast.makeText(context,message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
