package com.example.android.employeeapp;

import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Emp_upload extends Fragment {

    ListView listView;
    String names[];
    static int[] ID1=null;
    String time[]=null;
    String latitude[]=null;
    String longitude[]=null;
    RequestQueue requestQueue;
    String data1,ar[],id;
    int value;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootv=inflater.inflate(R.layout.fragment_emp_upload,container,false);

        Toast.makeText(getActivity(), " second page", Toast.LENGTH_LONG).show();


        listView=rootv.findViewById(R.id.activity_list);



        data1 = getArguments().getString("id");

            if (data1 == null) {
                data1 = getArguments().getString("IDpass");
                ar= data1.split("@");
                id = ar[0];
                Toast.makeText(getContext(), "" + id, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), " data " + data1, Toast.LENGTH_LONG).show();
                ar = data1.split("@");
                id = ar[0];

            }

        System.out.println("ar+"+ar);

        System.out.println("ID+"+id);
        requestQueue = Volley.newRequestQueue(getContext());
//        dialog.show();
        retrievalofdetails();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //   Toast.makeText(EmpActivities.this, "Clicked "+data1, Toast.LENGTH_SHORT).show();


                Intent intent=new Intent(getContext(),UMainActivity.class);
                Toast.makeText(getActivity(), position+" "+ID1.length, Toast.LENGTH_SHORT).show();
                intent.putExtra("id",ID1[position]);
                startActivity(intent);
                //code for map;


            }
        });
        return rootv;
    }



    class emp_descpAdaptor extends ArrayAdapter<String>{

        Context context;
        String name[];
        int ID[];
        String time[];

        emp_descpAdaptor (Context c, String name[],int ID[],String time[]){
            super(c, R.layout.list_details,R.id.emp_name,name);
            this.context=c;
            this.name=name;
            this.ID=ID;
            this.time=time;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.emplist,parent,false);
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
        Toast.makeText(getActivity(), " retrieve", Toast.LENGTH_LONG).show();

        String url="http://www.thantrajna.com/sjec_task/Employee_details/Retreive_details.php";
//        StringRequest name=new StringRequest( Request.Method.POST,url, new Response.Listener() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(EmpActivities.this, ""+response, Toast.LENGTH_SHORT).show();
//                //JSONObject obj= null;
//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    JSONArray jsonArray=jsonObject.getJSONArray("val");
//                    names=new String[jsonArray.length()];
//                    ID=new Integer[jsonArray.length()];
//                    time=new String[jsonArray.length()];
//                    latitude=new String[jsonArray.length()];
//                    longitude=new String[jsonArray.length()];
//                    for(int i=0;i<jsonArray.length();i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        ID[i] = obj.getInt("EMP_ID");
//                        latitude[i] = obj.getString("LATITUDE");
//                        longitude[i] = obj.getString("LONGITUDE");
//                        names[i] = obj.getString("DESCRIPTION");
//                        time[i] = obj.getString("DATE");
//                    }
//
//
//                    //  Toast.makeText(EmpActivities.this, names[0]+" "+names[1], Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                // emp_descpAdaptor adaptor=new emp_descpAdaptor(EmpActivities.this,names,ID,time);
//                //listView.setAdapter(adaptor);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(EmpActivities.this, "Err: " + error, Toast.LENGTH_SHORT).show();
//
//            }
//        })
//        {@Override
//            protected Map<String,String> getParams()
//            {
//                Map<String,String> params=new HashMap<String, String>();
//                params.put("id","4");
//                return params;
//            }
//        };

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            int i;
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("val");
                            names=new String[jsonArray.length()];
                            ID1=new int[jsonArray.length()];
                            time=new String[jsonArray.length()];
                            latitude=new String[jsonArray.length()];
                            longitude=new String[jsonArray.length()];
                            for(i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                ID1[i] = obj.getInt("TR_ID");
                                latitude[i] = obj.getString("LATITUDE");
                                longitude[i] = obj.getString("LONGITUDE");
                                names[i] = obj.getString("DESCRIPTION");
                                time[i] = obj.getString("DATE");
                            }
                            Toast.makeText(getActivity(), "+"+ID1[2], Toast.LENGTH_LONG).show();
                            value=i+1;

                            System.out.println("In response");
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        emp_descpAdaptor adaptor=new emp_descpAdaptor(getContext(),names,ID1,time);
                        listView.setAdapter(adaptor);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("In error + "+error.toString());

            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("id",id);
//                Toast.makeText(getContext(), "id "+ id, Toast.LENGTH_SHORT).show();
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }


}