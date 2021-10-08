package com.example.with_you;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
 Button signup,login;
 EditText loginusername,loginkeyword;
 DatabaseHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup=(Button) findViewById(R.id.button3);
        login=(Button) findViewById(R.id.button2);
        loginusername=(EditText) findViewById(R.id.loginusername);
        loginkeyword=(EditText) findViewById(R.id.loginkeyword);
        DB=new DatabaseHelper(this);

        signup.setOnClickListener(view -> {
            Intent intent= new Intent(getApplicationContext(),Signup.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            String logusername=loginusername.getText().toString();
            String logkeyword=loginkeyword.getText().toString();

            if(logusername.equals("")||logkeyword.equals(""))
            {
                Toast.makeText(Login.this, "Fill all the fields!", Toast.LENGTH_SHORT).show();
            }
            else
            {
               isUser();
            }



        });

    }

    private void isUser() {
        String userEnteredUserName=loginusername.getText().toString().trim();
        String userEnteredKeyword=loginkeyword.getText().toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users");

        Query checkUser=reference.orderByChild("personUserName").equalTo(userEnteredUserName);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    loginusername.setError(null);

                    String kwFromDB=snapshot.child(userEnteredUserName).child("personKeyword").getValue(String.class);

                    if(kwFromDB.equals(userEnteredKeyword))
                    {
                        loginkeyword.setError(null);
                       // loginkeyword.setErrorEnabled(False);

                        String Name =snapshot.child(userEnteredUserName).child("personName").getValue(String.class);
                        String UserName =snapshot.child(userEnteredUserName).child("personUserName").getValue(String.class);
                        String Mob01 =snapshot.child(userEnteredUserName).child("personMob01").getValue(String.class);
                        String Mob02 =snapshot.child(userEnteredUserName).child("personMob02").getValue(String.class);
                        String Mob03 =snapshot.child(userEnteredUserName).child("personMob03").getValue(String.class);

                        Toast.makeText(Login.this, "Logged in as "+Name, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);

                        intent.putExtra("personName",Name);
                        intent.putExtra("personUserName",UserName);
                        intent.putExtra("personMob01",Mob01);
                        intent.putExtra("personMob02",Mob02);
                        intent.putExtra("personMob03",Mob03);
                        startActivity(intent);



                    }
                    else
                    {
                        loginkeyword.setError("wrong Password");
                    }
                }
                else
                {
                    loginusername.setError("No Such User Exists");
                    loginusername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}