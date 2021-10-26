package com.example.with_you;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_CODE_SPEECH_INPUT = 20000;
    private static final int REQUEST_CALL=1;
    private Button login, BackgroundProcessBtn, guardian;
    private TextView test,test2;
    private final int MIN_TIME = 1000;//1 sec
;    private final int MIN_DISTANCE = 1;//1 meter

    MediaPlayer player;
  //  private int STORAGE_PERMISSION_CODE=1;
    private LocationManager manager;
    TelephonyManager telephonyManager = null;
    PhoneStateListener listener = new PhoneStateListener();

    DatabaseReference databaseReference,databaseReference1,reference,referenceFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager =(LocationManager)getSystemService(LOCATION_SERVICE);

      test=findViewById(R.id.testtext);
      test2=findViewById(R.id.testtext2);
      //  BackgroundProcessBtn=(Button)findViewById(R.id.button4);
        login=(Button) findViewById(R.id.button);
        guardian=(Button)findViewById(R.id.guardian);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)
                !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }

        guardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGuardian();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }

                    });



        databaseReference1=FirebaseDatabase.getInstance().getReference().child("user-location");



//       BackgroundProcessBtn.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               startBGProcess();
//           }
//       });


//     if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
//     {
//         //Toast.makeText(MainActivity.this, "Permission Already Given!", Toast.LENGTH_SHORT).show();
//
//         ActivityCompat.requestPermissions(this,
//                 new String[]{Manifest.permission.READ_PHONE_STATE},1);
//     }
//     else
//     {
//         callPermission();
//     }



    }

    private void getLocationUpdates() {
        if (manager != null) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                    }
                    Toast.makeText(this, "Please enable GPS and Network Provider", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLocationUpdates();
            }
            else {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                }
            }
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                    getLocationUpdates();
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

                    String username=users.getPersonUserName();

                    //Map loading


                    referenceFB = FirebaseDatabase.getInstance().getReference().child("user-location").child(username);







                    test2.setText(username);




                        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                        }
                        else
                        {
                            if(Mob01.trim().length()>0 && Mob02.trim().length()>0 && Mob03.trim().length()>0)
                            {

//                                String dial = "tel:" + Mob01;
//                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
//                                Toast.makeText(MainActivity.this, "number dialed", Toast.LENGTH_SHORT).show();
                                int loop=3;
                                while (loop>0) {
                                    final Handler handler = new Handler(Looper.getMainLooper());


                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String dial = "tel:" + Mob01;
                                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                            Toast.makeText(MainActivity.this, "number dialed", Toast.LENGTH_SHORT).show();

                                        }
                                    }, 30000);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String dial2 = "tel:" + Mob02;
                                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial2)));
                                            Toast.makeText(MainActivity.this, "number dialed", Toast.LENGTH_SHORT).show();

                                        }
                                    }, 80000);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String dial3 = "tel:" + Mob03;
                                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial3)));
                                            Toast.makeText(MainActivity.this, "number dialed", Toast.LENGTH_SHORT).show();

                                        }
                                    }, 110000);
                                }
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                    }
//                                }, 150000);



                        }
                    }


                    //String mn01="tel: "+Mob01;
//                    Intent callintent=new Intent(Intent.ACTION_CALL);
//                    callintent.setData(Uri.parse("tel:0772540515"));
                    //test2.setText(mob01);
                    String SMS="This is an Emergency! Please Help me to prevent from This situation! Track my location Using With you! application, enter UserName : '"+username+"'";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                        {
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(Mob01, null, SMS, null, null);
                                smsManager.sendTextMessage(Mob02, null, SMS, null, null);
                                smsManager.sendTextMessage(Mob03, null, SMS, null, null);
                                Toast.makeText(MainActivity.this, "Message is sent", Toast.LENGTH_SHORT).show();

                               // playSound

                                AssetFileDescriptor afd = getAssets().openFd("BirdNotificationTone.mp3");
                                MediaPlayer player = new MediaPlayer();
                                player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                                player.prepare();
                                player.start();

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
    //@Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode==REQUEST_CALL)
//        {
//            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//
//            }
//        }
//    }

    private void openLogin() {
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
//        String mn01="tel: "+772540515;
//        Intent intent=new Intent(Intent.ACTION_CALL);
//        intent.setData(Uri.parse(mn01));

    }

    private void openGuardian() {

       Intent intent=new Intent(this,Guardian.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location!=null)
        {
            saveLocation(location);
        }
        else
        {
            Toast.makeText(this, "Enable GPS", Toast.LENGTH_SHORT).show();

        }

    }

    private void saveLocation(Location location) {
       String username= test2.getText().toString();
        databaseReference1.child(username).setValue(location);
    }

//    private void readChanges() {
//
//        String Username=test2.getText().toString();
//
//        reference = FirebaseDatabase.getInstance().getReference().child("user-location").child(Username);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    try {
//                        MyLocation location = snapshot.getValue(MyLocation.class);
//                        if (location != null) {
//                           // myMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
//                        }
//                    } catch (Exception e) {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//
//    }
}