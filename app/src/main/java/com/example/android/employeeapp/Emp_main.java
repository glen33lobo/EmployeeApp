package com.example.android.employeeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.android.employeeapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Emp_main extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem info,trans;
    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_main);
        sectionsPagerAdapter= new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout= findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        info=(TabItem)findViewById(R.id.tab_info);
        trans=(TabItem)findViewById(R.id.tab_trans);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //copy contents
    }
}