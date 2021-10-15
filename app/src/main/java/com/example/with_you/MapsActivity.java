package com.example.with_you;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.with_you.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private DatabaseReference reference;
    private LocationManager manager;
    private final int MIN_TIME = 1000;//1 sec
    private final int MIN_DISTANCE = 1;//1 meter

    Switch travalAlone;
    TextView name;
    String userName;

    Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Intent intent = getIntent();
        String user_username = intent.getStringExtra("personUserName");


        reference = FirebaseDatabase.getInstance().getReference().child("user-location").child(user_username);

        name=findViewById(R.id.regname);
        travalAlone=findViewById(R.id.travellingAloneMode);

        showUserData();


        travalAlone.setChecked(false);
        travalAlone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (travalAlone.isChecked()==true)
                {
                    readChanges();
                    getLocationUpdates();
                    sendMessage();

                }

            }
        });


        //FirebaseDatabase.getInstance().getReference().child("user-location").setValue("new data");

        /**   binding = ActivityMapsBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    private void sendMessage() {
        Intent intent=getIntent();
        String name=intent.getStringExtra("personName");
        String Mob01=intent.getStringExtra("personMob01");
        String Mob02=intent.getStringExtra("personMob02");
        String Mob03=intent.getStringExtra("personMob03");

        String SMS="Hi! I'm travelling alone! Please keep track on my location.. GPS link :";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED)
            {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(Mob01, null, SMS, null, null);
                    smsManager.sendTextMessage(Mob02, null, SMS, null, null);
                    smsManager.sendTextMessage(Mob03, null, SMS, null, null);
                    Toast.makeText(this, "Message is sent", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Faild to send message", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
            }
        }

    }

    private void showUserData() {

        Intent intent = getIntent();
        String user_name = intent.getStringExtra("personName");
        String user_username = intent.getStringExtra("personMob01");


        name.setText("Hi " + user_name+"," );
    }

    private void readChanges() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        MyLocation location = snapshot.getValue(MyLocation.class);
                        if (location != null) {
                            myMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    } catch (Exception e) {
                        Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    Toast.makeText(this, "Please enable GPS and Network Provider", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdates();
            } else {
                Toast.makeText(this, "Please enable GPS and Network Provider", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near srilanka, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        /**   LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         // TODO: Consider calling
         //    ActivityCompat#requestPermissions
         // here to request the missing permissions, and then overriding
         //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
         //                                          int[] grantResults)
         // to handle the case where the user grants the permission. See the documentation
         // for ActivityCompat#requestPermissions for more details.
         return;
         }
         Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         double latitude=location.getLatitude();
         double longitude= location.getLongitude();

         // Add a marker in srilanka and move the camera
         LatLng srilanka = new LatLng(latitude, longitude);*/
        LatLng srilanka = new LatLng(6.89, 80.59);

        myMarker= mMap.addMarker(new MarkerOptions().position(srilanka).title("Marker in srilanka"));
        mMap.setMinZoomPreference(9);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(srilanka));
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        if(location!=null)
        {
            saveLocation(location);
        }
        else
        {
            Toast.makeText(this, "Please enable GPS and Network Provider", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveLocation(Location location) {

        reference.setValue(location);

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}