package com.example.android.employeeapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umain);


        Bundle bundle = getIntent().getExtras();
        String trid = bundle.getString("id");
//            String trid=getIntent().getExtras().getString("id");
            Toast.makeText(this, "id" +trid, Toast.LENGTH_SHORT).show();

//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();



    }
}
