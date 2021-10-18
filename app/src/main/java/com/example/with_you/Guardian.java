package com.example.with_you;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Guardian extends AppCompatActivity {
    EditText username;
    private Button track;
    TextView testt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian2);
        username = findViewById(R.id.guserusername);
        track = (Button) findViewById(R.id.trackbtn);
        testt=findViewById(R.id.textView3);

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameEntered = username.getText().toString();

                if (usernameEntered.equals("")) {
                    Toast.makeText(Guardian.this, "Fill the fields!", Toast.LENGTH_SHORT).show();
                } else {
                            checkUsername();
                      }
            }


            private void checkUsername() {

                String usernameEntered = username.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user-location");

                Query checkUser = reference.child(usernameEntered);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            username.setError(null);

                            // String kwFromDB=snapshot.child(usernameEntered).child("personKeyword").getValue(String.class);

                            Toast.makeText(Guardian.this, "fetching the result...", Toast.LENGTH_SHORT).show();
                            MyLocation location = snapshot.getValue(MyLocation.class);
                            String Latitude = String.valueOf(location.getLatitude());
                            String Longitude = String.valueOf(location.getLongitude());
                           // String Latitude =snapshot.child(usernameEntered).child("latitude").getValue(String.class);
                           // String Longitude =  snapshot.child(usernameEntered).child("longitute").getValue(String.class);

                         //   testt.setText( Longitude);
                          //  System.out.print(Latitude+"--------------------------------------------------");
                            Intent intent=new Intent(getApplicationContext(),GuardianTrack.class);

                            intent.putExtra("latitude",Latitude);
                            intent.putExtra("longitude",Longitude);
                            intent.putExtra("name",usernameEntered);

                            startActivity(intent);

                        } else {
                            username.setError("No Such User Exists");
                            username.requestFocus();
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }
}
