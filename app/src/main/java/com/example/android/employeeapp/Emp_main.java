package com.example.android.employeeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.android.employeeapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Emp_main extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem info, trans;
    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;
    Toolbar toolbar;
    SharedPreferences sp;
    public static final String MSP1 = "Login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_main);
        String id=getIntent().getExtras().getString("ID2");
        if(id==null)
            id=getIntent().getExtras().getString("IDpass");
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),id);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        info = (TabItem) findViewById(R.id.tab_info);
        trans = (TabItem) findViewById(R.id.tab_trans);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




        viewPager.setCurrentItem(0, true);
        //copy contents
        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);

        sp = getSharedPreferences(MSP1, Context.MODE_PRIVATE);



    }



    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(0,true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Emp_main.this);
            builder.setMessage("Are you sure to Logout Completely")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            SharedPreferences.Editor ed1 = sp.edit();
                            ed1.clear();
                            ed1.commit();
                            Toast.makeText(Emp_main.this,"Successfull Logout",Toast.LENGTH_SHORT).show();

                            Emp_info.SERVICE_RUN=false;

                            startActivity(new Intent(Emp_main.this,MainActivity.class));
                            //  return true;
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });

            final AlertDialog alert = builder.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
}