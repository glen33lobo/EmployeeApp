package com.example.android.employeeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Employee_Main extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    public static final int RESULT_CODE =11 ;
    LocationManager locationManager;
    String lattitude,longitude;
    SharedPreferences sp;
    public static final String MSP1="Login";
    Button b,upb,uploadb;
    String data,data1;
    RequestQueue requestQueue;
    EditText editText;
    Cursor c;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee__main);

        b=(Button)findViewById(R.id.logout);
        upb=(Button)findViewById(R.id.update_desc);
        uploadb=(Button)findViewById(R.id.upload);
        editText=(EditText)findViewById(R.id.Desciption);
        sp=getSharedPreferences(MSP1, Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(Employee_Main.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            data = bundle.getString("ID2");
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor ed1 = sp.edit();
                ed1.clear();
                ed1.commit();
                Toast.makeText(Employee_Main.this,"Successfull Logout",Toast.LENGTH_SHORT).show();
               insertlogout(data);
                startActivity(new Intent(Employee_Main.this,MainActivity.class));


            }
        });

        upb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(editText.getText().toString().trim().length() > 0)
                {
                    update(data);
                }
                else {
                    Toast.makeText(Employee_Main.this,"Nothing to  update",Toast.LENGTH_SHORT).show();
                }
            }
        });

        uploadb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadfile();
            }
        });

    }

//    public void startMyService()
//    {
////        ServiceClass sc=new ServiceClass(this);
////        Intent intent=new Intent(this,sc.getClass()/*ServicesClass.class*/);
//        Intent intent=new Intent(this,ServiceClass.class);
//        ResultReceiver r=new Employee_Main().myreceiver(null);
////        intent.putExtra("ID",id);
////        intent.putExtra("lngg",b);
//        intent.putExtra("receiver",r);
//
//        startService(intent);
//    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AskforPermission();

        } else {
            double latti,longi;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Toast.makeText(this, "Location: "+lattitude+" "+longitude, Toast.LENGTH_SHORT).show();

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);

            } else  if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);


            } else  if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);

            }else {

                System.out.println("Unble to Trace your location");
                // Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }



    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Employee_Main.this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    public void callforgetlocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

    }

    public void AskforPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    }



    public class myreceiver extends ResultReceiver{

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public myreceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode==RESULT_CODE){

                if(resultData!=null)
                {
                    lattitude=resultData.getString("res_lat");
                    longitude=resultData.getString("res_lng");
                }
            }
        }

    }

    public void insertlogout(final String data)
    {
        String url="http://www.thantrajna.com/sjec_task/For_Employers/insert_logout.php";
        StringRequest name=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Employee_Main.this, ""+response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Employee_Main.this, "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("ID",data+"");
                params.put("LATITUDE",lattitude+"");
                params.put("LONGITUDE",longitude+"");
                return params;
            }
        };
        requestQueue.add(name);

    }

    public void update(final String data)
    {
        String url="http://www.thantrajna.com/sjec_task/For_Employers/insert_workmain.php";
        StringRequest name=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Employee_Main.this, ""+response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Employee_Main.this, "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                String description=editText.getText().toString();
                Map<String,String> params=new HashMap<String, String>();
                params.put("ID",data+"");
                params.put("LATITUDE",lattitude+"");
                params.put("LONGITUDE",longitude+"");
                params.put("DESCRIPTION",description+"");
                return params;
            }
        };
        requestQueue.add(name);

    }




    public void uploadfile()
    {
        Toast.makeText(Employee_Main.this,"Upload Section!",Toast.LENGTH_SHORT).show();



    }
}
