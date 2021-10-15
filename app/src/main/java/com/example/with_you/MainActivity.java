package com.example.with_you;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 20000;
    private Button login, BackgroundProcessBtn;
    private TextView test,test2;
    MediaPlayer player;
  //  private int STORAGE_PERMISSION_CODE=1;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      test=findViewById(R.id.testtext);
//        test2=findViewById(R.id.testtext2);
      //  BackgroundProcessBtn=(Button)findViewById(R.id.button4);
        login=(Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }

                    });


//       BackgroundProcessBtn.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               startBGProcess();
//           }
//       });


     if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
     {
         Toast.makeText(MainActivity.this, "Permission Already Given!", Toast.LENGTH_SHORT).show();
     }
     else
     {
         callPermission();
     }

    }

    private void callPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
        }


    }

//    public void startBGProcess()
//    {
//
//        Intent intent = new Intent(this, BackgroundProcess.class);
//        intent.setAction("Background Process");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 10, pendingIntent);
//
//        finish();
//
//    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        int action,keycode;

        action=event.getAction();
        keycode=event.getKeyCode();

        switch (keycode) {
            case KeyEvent.KEYCODE_VOLUME_UP: {
                if (KeyEvent.ACTION_UP ==action)
                {
                    voiceInput();
                   // test.setText("Hello");
                }
            }

        }

        return super.dispatchKeyEvent(event);




    }

    private void voiceInput() {
        //Intent to show

        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Say the Keyword!");

        try{
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);

        } catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode== RESULT_OK && null!=data)
                {
                    ArrayList<String>result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    test.setText(result.get(0));

                    retrieveData();
                }
                break;
            }
        }



    }

    private void retrieveData() {
        String keyword= test.getText().toString();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");

        Query checkKeyword=databaseReference.orderByChild("personKeyword").equalTo(keyword);

        checkKeyword.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    UserHelperClass users=dataSnapshot.getValue(UserHelperClass.class);
                    String Mob01=users.getPersonMob01();
                    String Mob02=users.getPersonMob02();
                    String Mob03=users.getPersonMob03();

                    String mn01="tel: "+Mob01;
                    Intent callintent=new Intent(Intent.ACTION_CALL);
                    callintent.setData(Uri.parse(mn01));
                    //test2.setText(mob01);
                    String SMS="This is an Emergency! Please Help me to prevent from This situation! Track my location : ";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                        {
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(Mob01, null, SMS, null, null);
                                smsManager.sendTextMessage(Mob02, null, SMS, null, null);
                                smsManager.sendTextMessage(Mob03, null, SMS, null, null);
                                Toast.makeText(MainActivity.this, "Message is sent", Toast.LENGTH_SHORT).show();

                                playSound();

                                //Call Action







                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                        }
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void playSound(View v) {
        if (player==null)
        {
            player = MediaPlayer.create(this,R.raw.BirdNotificationTone.mp3);
        }

    }

    private void openLogin() {
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
//        String mn01="tel: "+772540515;
//        Intent intent=new Intent(Intent.ACTION_CALL);
//        intent.setData(Uri.parse(mn01));

    }
    }