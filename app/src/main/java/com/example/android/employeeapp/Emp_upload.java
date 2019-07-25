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
    String names[]=null;
    Integer ID[]=null;
    String time[]=null;
    String latitude[]=null;
    String longitude[]=null;
    RequestQueue requestQueue;
    String data1;
    int value;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootv=inflater.inflate(R.layout.fragment_emp_upload,container,false);


        dialog=new ProgressDialog(getActivity());
        dialog.setTitle("Please Wait");
        dialog.setMessage("Loading..");

        listView=rootv.findViewById(R.id.activity_list);
//        Intent intent = getIntent();
//        data = intent.getStringExtra("ID");


        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            data1 = bundle.getString("id");
            if (data1 == null) {
                data1 = bundle.getString("IDpass");
                String ar[] = data1.split("@");
                String id = ar[0];
                Toast.makeText(getContext(), "" + id, Toast.LENGTH_SHORT).show();
//                checklogin(id);
            } else {
                Toast.makeText(getActivity(), " data " + data, Toast.LENGTH_SHORT).show();
                ar = data.split("@");
                id = ar[0];
                status_of_user = ar[1];
                datep.setText(ar[2]);

                if (status_of_user.equals("Login")) {
                    work_create.setVisibility(View.VISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    upb.setVisibility(View.INVISIBLE);
                    uploadb.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);

                    startMyService(id);
                    loginout.setBackgroundResource(R.drawable.circle);
                    loginout.setText("LOGGED\nIN");
                } else if (status_of_user.equals("Logout")) {
                    work_create.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    upb.setVisibility(View.INVISIBLE);
                    uploadb.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);

                    loginout.setBackgroundResource(R.drawable.circlered);
                    loginout.setText("LOGGED\nOUT");

                }
                Toast.makeText(getActivity(), "" + status_of_user, Toast.LENGTH_SHORT).show();
            }
        }
        requestQueue = Volley.newRequestQueue(getContext());
        dialog.show();
        retrievalofdetails();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //   Toast.makeText(EmpActivities.this, "Clicked "+data1, Toast.LENGTH_SHORT).show();

                int i;
                for(i=0;i<value;i++)
                {
                    if(position==i) {
                        break;
                    }
                }
                Intent intent=new Intent(getContext(),MapsActivity.class);
                intent.putExtra("latid",latitude[i]);
                intent.putExtra("longid",longitude[i]);
                startActivity(intent);
                //code for map;


            }
        });
        return rootv;
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
                            ID=new Integer[jsonArray.length()];
                            time=new String[jsonArray.length()];
                            latitude=new String[jsonArray.length()];
                            longitude=new String[jsonArray.length()];
                            for(i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                ID[i] = obj.getInt("EMP_ID");
                                latitude[i] = obj.getString("LATITUDE");
                                longitude[i] = obj.getString("LONGITUDE");
                                names[i] = obj.getString("DESCRIPTION");
                                time[i] = obj.getString("DATE");
                            }

                            value=i+1;

                            //  Toast.makeText(EmpActivities.this, names[0]+" "+names[1], Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        EmpActivities.emp_descpAdaptor adaptor=new EmpActivities.emp_descpAdaptor(getContext(),names,ID,time);
                        listView.setAdapter(adaptor);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("id",data1.toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }


//    public void maps(View view)
//    {
//        Intent i=new Intent(EmpActivities.this,MapsActivity.class);
//        //i.putExtra("id",ID[pos]);
//        startActivity(i);
//    }
}