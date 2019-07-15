package com.example.android.employeeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Employee_Main extends AppCompatActivity {

    SharedPreferences sp;
    public static final String MSP1="Login";
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee__main);

        b=(Button)findViewById(R.id.logout);
        sp=getSharedPreferences(MSP1, Context.MODE_PRIVATE);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed1 = sp.edit();
                ed1.clear();
                ed1.commit();
                Toast.makeText(Employee_Main.this,"Successfull Logout",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Employee_Main.this,MainActivity.class));
            }
        });
    }


}
