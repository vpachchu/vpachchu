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
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    private EditText personNameEdt, personKeywordEdt, personUserNameEdt, personMob01Edt,personMob02Edt,personMob03Edt;
    private Button registerBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // initializing all our variables.
        personNameEdt = findViewById(R.id.idEdtName);
        personKeywordEdt = findViewById(R.id.idEdtKeyWord);
        personUserNameEdt = findViewById(R.id.idEdtUserName);
        personMob01Edt = findViewById(R.id.idEdtMob01);
        personMob02Edt = findViewById(R.id.idEdtMob02);
        personMob03Edt = findViewById(R.id.idEdtMob03);
        registerBtn = (Button) findViewById(R.id.idBtnSignUP);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference().child("users");

                String personName = personNameEdt.getText().toString();
                String personKeyword = personKeywordEdt.getText().toString();
                String personUserName = personUserNameEdt.getText().toString();
                String personMob01 = personMob01Edt.getText().toString();
                String personMob02 = personMob02Edt.getText().toString();
                String personMob03 = personMob03Edt.getText().toString();

                if(personName.isEmpty()||personKeyword.isEmpty()||personKeyword.isEmpty()||personMob01.isEmpty()||personMob02.isEmpty()||personMob03.isEmpty())
                {
                    Toast.makeText(Signup.this, "Please Fill All the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // below line is to get data from all edit text fields.

                    UserHelperClass helperClass=new UserHelperClass(personName,personKeyword,personUserName,personMob01,personMob02,personMob03);
                    {
                        helperClass.setPersonName(personName);
                        //reference.child(String.valueOf(uid+1)).setValue(helperClass);
                        Toast.makeText(Signup.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                    }

                    reference.child(personUserName).setValue(helperClass);

                }


            }
        });


    }

    public void login()
    {



    }
}