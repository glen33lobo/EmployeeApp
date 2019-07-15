package com.example.android.employeeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class All_Employers extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestQueue requestQueue;
    Integer ID[]=null;
    String U_NAME[]=null;
    String DESC[]=null;

    //String[] BRANCH_NAME={"MANOJ","KRIHDNS","ASKKD","dfsd","dfsdv"};
    //String[] Name_logo={"mj","ks","ac","grfer","gerg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employers);
        requestQueue= Volley.newRequestQueue(this);

        performretrieval();




    }




    public void performretrieval()
    {
        String url="http://www.thantrajna.com/sjec_task/Employee_details/Retrive_all_emp.php";
        JsonObjectRequest name=new JsonObjectRequest( Request.Method.POST,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //JSONObject obj= null;
                try {

                    JSONArray jsonArray=response.getJSONArray("val");
                    ID=new Integer[jsonArray.length()];
                    U_NAME=new String[jsonArray.length()];
                    DESC=new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        ID[i] = obj.getInt("ID");
                        U_NAME[i] = obj.getString("USERNAME");
                        DESC[i] = obj.getString("DESCRIPTION");
                    }

                    Toast.makeText(All_Employers.this, U_NAME[0]+" "+U_NAME[1], Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                recyclerView=(RecyclerView)findViewById(R.id.employers_list);
                recyclerView.setHasFixedSize(true);
                CustomAdapter radapter=new CustomAdapter(U_NAME,DESC);
                RecyclerView.LayoutManager rlayoutmanager=new LinearLayoutManager(All_Employers.this,RecyclerView.VERTICAL,false);
                recyclerView.setLayoutManager(rlayoutmanager);
                recyclerView.setAdapter(radapter);

                radapter.setonItemclicklistener(new CustomAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int pos) {
//                Intent i=new Intent(All.this,Sub_Domain_list.class);
//                i.putExtra("Position",pos);
//                startActivity(i);
                        Toast.makeText(All_Employers.this, "Selected", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(All_Employers.this, "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
//                params.put("uname",track_user);
                return params;
            }
        };
        requestQueue.add(name);




    }













    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ExampleViewHolder> {

        public String[] names_array;
        public String[] desc_array;

        private OnItemClickListener mlister;
        public interface OnItemClickListener{
            void onItemClick(int pos);
        }

        public void setonItemclicklistener(OnItemClickListener listener)
        {
            mlister=listener;
        }


        public CustomAdapter(String[] un_array,String[] desc_array)
        {
            this.names_array=un_array;
            this.desc_array=desc_array;

        }

        public static class ExampleViewHolder extends RecyclerView.ViewHolder{

            public TextView t_desc;
            public TextView t_name;

            public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                t_name=(TextView)itemView.findViewById(R.id.id_name);
                t_desc=(TextView)itemView.findViewById(R.id.descp);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener!=null){
                            int pos=getAdapterPosition();
                            if(pos!=RecyclerView.NO_POSITION){
                                listener.onItemClick(pos);
                            }
                        }
                    }
                });
            }
        }

        @NonNull
        @Override
        public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employer_details,viewGroup, false);
            ExampleViewHolder evh=new ExampleViewHolder(v,mlister);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ExampleViewHolder viewHolder, int i) {
            String ddesc=desc_array[i];
            String tname=names_array[i];

            viewHolder.t_desc.setText(ddesc);
            viewHolder.t_name.setText(tname);

        }


        @Override
        public int getItemCount() {
//            if(logo_array==null)
//                return 0;
            return desc_array.length;
        }

    }
}
