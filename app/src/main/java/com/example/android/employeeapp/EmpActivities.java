package com.example.android.employeeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmpActivities extends AppCompatActivity {

    ListView listView;
    String names[]=null;
    Integer ID[]=null;
    String time[]=null;
    String latitude[]=null;
    String longitude[]=null;
    RequestQueue requestQueue;
    int data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_activities);

        listView=findViewById(R.id.activity_list);
//        Intent intent = getIntent();
//        data = intent.getStringExtra("ID");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            data = bundle.getInt("id");
            //Toast.makeText(EmpActivities.this, "Clicked "+data, Toast.LENGTH_SHORT).show();
        }
        requestQueue = Volley.newRequestQueue(EmpActivities.this);
        retrievalofdetails();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(EmpActivities.this, "Clicked "+data, Toast.LENGTH_SHORT).show();

               //code for map;


            }
        });
    }

    class emp_descpAdaptor extends ArrayAdapter<String>{

        Context context;
        String name[];
        Integer ID[];
        String time[];

        emp_descpAdaptor (Context c, String name[],Integer ID[],String time[]){
            super(c, R.layout.list_details,R.id.emp_name,name);
            this.context=c;
            this.name=name;
            this.ID=ID;
            this.time=time;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.list_details,parent,false);
            TextView title=row.findViewById(R.id.emp_name);
            TextView desc=row.findViewById(R.id.emp_descp);
            TextView t=row.findViewById(R.id.emp_time);
            title.setText(names[position]);
            desc.setText(ID[position]+"");
            t.setText(time[position]);
            return row;
        }
    }

    public void retrievalofdetails()
    {
        String url="http://www.thantrajna.com/sjec_task/Employee_details/Retreive_details.php";
        JsonObjectRequest name=new JsonObjectRequest( Request.Method.POST,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //JSONObject obj= null;
                try {

                    JSONArray jsonArray=response.getJSONArray("val");
                    names=new String[jsonArray.length()];
                    ID=new Integer[jsonArray.length()];
                    time=new String[jsonArray.length()];
                    latitude=new String[jsonArray.length()];
                    longitude=new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        ID[i] = obj.getInt("EMP_ID");
                        latitude[i] = obj.getString("LATITUDE");
                        longitude[i] = obj.getString("LONGITUDE");
                        names[i] = obj.getString("DESCRIPTION");
                        time[i] = obj.getString("DATE");
                    }


                        //  Toast.makeText(EmpActivities.this, names[0]+" "+names[1], Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                emp_descpAdaptor adaptor=new emp_descpAdaptor(EmpActivities.this,names,ID,time);
                listView.setAdapter(adaptor);


//                recyclerView=(RecyclerView)findViewById(R.id.employers_list);
//                recyclerView.setHasFixedSize(true);
//                All_Employers.CustomAdapter radapter=new All_Employers.CustomAdapter(U_NAME,DESC);
//                RecyclerView.LayoutManager rlayoutmanager=new LinearLayoutManager(All_Employers.this,RecyclerView.VERTICAL,false);
//                recyclerView.setLayoutManager(rlayoutmanager);
//                recyclerView.setAdapter(radapter);
//
//                radapter.setonItemclicklistener(new All_Employers.CustomAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int pos) {
//                        Intent i=new Intent(All_Employers.this,EmpActivities.class);
//                        i.putExtra("ID",ID[pos]);
//                        //    Toast.makeText(All_Employers.this, ""+pos+" "+ID[pos], Toast.LENGTH_SHORT).show();
//                        startActivity(i);
//                        //startActivity(new Intent(All_Employers.this,EmpActivities.class));
//                    }
//                });




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmpActivities.this, "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("ID",data+"");
                return params;
            }
        };
        requestQueue.add(name);

    }


}

