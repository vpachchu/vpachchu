package com.example.with_you;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    TextView name,username,keyword,mob01,mob02,mob03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name=findViewById(R.id.regname);
       // username=findViewById(R.id.regusername);
        mob01=findViewById(R.id.regmob01);
        mob02=findViewById(R.id.regmob02);
        mob03=findViewById(R.id.regmob03);
       // keyword=findViewById(R.id.regkeyword);

        showUserData();
    }

    private void showUserData() {

        Intent intent=getIntent();
        String user_name=intent.getStringExtra("personName");
        String user_mob01=intent.getStringExtra("personMob01");
        String user_mob02=intent.getStringExtra("personMob02");
        String user_mob03=intent.getStringExtra("personMob03");

        name.setText("Hi! "+user_name+" Welcome Back!");
        mob01.setText(user_mob01);
        mob02.setText(user_mob02);
        mob03.setText(user_mob03);

    }
}