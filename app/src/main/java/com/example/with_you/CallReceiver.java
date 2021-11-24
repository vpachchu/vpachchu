package com.example.with_you;
import static android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED;

import android.content.res.AssetFileDescriptor;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class CallReceiver extends BroadcastReceiver {


//    @Override
//    public void onReceive(Context context, Intent intent) {
//        try {
//            String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//
//                showToast(context, "Call Started...");
//
//
//                    final Handler handler = new Handler(Looper.getMainLooper());
//
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//
//                            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//
//                                showToast(context, "Call Answered...");
//                                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//// Vibrate for 500 milliseconds
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
//                                } else {
//                                    //deprecated in API 26
//                                    v.vibrate(1000);
//                                }
//                            }
//                            else  {
//
//
//                                showToast(context, "Call not Answered...");
//
//
//
//                            }
//                        }
//                    }, 45000);
//
//                }
//
//
//
//            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                showToast(context, "Call Ringing..");
//            }
//            else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                showToast(context, "Call Ended...");}
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    void showToast(Context context,String message)
    {
        Toast toast= Toast.makeText(context,message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        try {
            final Handler handler = new Handler(Looper.getMainLooper());


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {


                        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                            showToast(context, "Call Answered...");

                            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(1000);
                            }
                        } else {
                            showToast(context, "Call not Answered...");

                        }

                    }

                }
            }, 45000);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
