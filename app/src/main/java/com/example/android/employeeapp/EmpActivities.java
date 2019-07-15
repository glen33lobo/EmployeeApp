package com.example.android.employeeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EmpActivities extends AppCompatActivity {

    ListView listView;
    String names[]={"Manoj","Chetan","Achar"};
    String descp[]={"Login","KMC","At office"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_activities);

        listView=findViewById(R.id.activity_list);

        emp_descpAdaptor adaptor=new emp_descpAdaptor(this,names,descp);
        listView.setAdapter(adaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EmpActivities.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class emp_descpAdaptor extends ArrayAdapter<String>{

        Context context;
        String name[];
        String descp[];

        emp_descpAdaptor (Context c, String name[],String descp[]){
            super(c, R.layout.list_details,R.id.emp_name,name);
            this.context=c;
            this.name=name;
            this.descp=descp;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.list_details,parent,false);
            TextView title=row.findViewById(R.id.emp_name);
            TextView desc=row.findViewById(R.id.emp_descp);

            title.setText(names[position]);
            desc.setText(descp[position]);

            return row;
        }
    }
}
