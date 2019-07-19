package com.example.android.employeeapp;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    public static boolean SERVICE_RUN=false;
    private static final int REQUEST_LOCATION = 1;
    public static final int RESULT_CODE =11 ;
    LocationManager locationManager;
    String lattitude,longitude;
    Button login_btn;
    Button emp_btn;
    TextView user;
    TextView pass;
    RequestQueue requestQueue;
    SharedPreferences sp;
    public static final String MSP="Login";
    public static final String log =" ";
    SQLiteDatabase db;
    Cursor c;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        login_btn = (Button) findViewById(R.id.login_button);
        emp_btn = (Button) findViewById(R.id.emp_login);
        user = (TextView) findViewById(R.id.uname);
        pass = (TextView) findViewById(R.id.upass);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        sp = getSharedPreferences(MSP, Context.MODE_PRIVATE);
        db = openOrCreateDatabase("Login_Details", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users(name varchar,pass varchar);");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

//        login_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(MainActivity.this,All_Employers.class);
//                startActivity(i);
//            }
//        });
        emp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getText().toString().equals("Admin") && pass.getText().toString().equals("admin123")) {
                    Intent i=new Intent(MainActivity.this,All_Employers.class);
                    startActivity(i);

                } else {
                    String url = "http://www.thantrajna.com/sjec_task/For_Employers/Emplogin_check.php";
                    StringRequest name = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("error")) {
                                Toast.makeText(MainActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(MainActivity.this, "res: " + response, Toast.LENGTH_SHORT).show();

                                insertlogin(response);


                                //registation shared preferance
                                String nm = user.getText().toString();
                                String ps = pass.getText().toString();
                                Cursor c;
                                db.execSQL("INSERT INTO users values('" + nm + "','" + ps + "')");
                                //for Registration


                                //login
                                c = db.rawQuery("Select * from users", null);
                                c.moveToFirst();
                                String s6 = user.getText().toString();
                                String s7 = pass.getText().toString();
                                for (int i = 0; i < c.getCount(); i++) {
                                    String s3 = c.getString(0);
                                    String s4 = c.getString(1);
                                    //login


                                    try {
                                        if ((s6.matches(s3)) && (s7.matches(s4))) {
                                            SharedPreferences.Editor ed = sp.edit();
                                            ed.putString(log, "logged");
                                            ed.commit();
                                            Intent in = new Intent(MainActivity.this, Employee_Main.class);
                                            in.putExtra("ID2", response);
                                            startActivity(in);
                                            break;
                                        }
                                        c.moveToNext();
                                    } catch (Exception e) {

                                    }

//                        id=Integer.parseInt(response);
//                        Intent i=new Intent(MainActivity.this,Sample.class);
//                        i.putExtra("ID",id);
//                        startActivity(i);

                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Err: " + error, Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", user.getText().toString());
                            params.put("password", pass.getText().toString());
                            return params;
                        }
                    };
                    requestQueue.add(name);
                }
            }
        });

    }

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

          //      Toast.makeText(this, "Location1: "+lattitude+" "+longitude, Toast.LENGTH_SHORT).show();
//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);


            } else  if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

              //  Toast.makeText(this, "Location2: "+lattitude+" "+longitude, Toast.LENGTH_SHORT).show();
//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);

            }else {

                System.out.println("Unble to Trace your location");
                // Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (sp.contains(log)) {
            startActivity(new Intent(MainActivity.this,Employee_Main.class));
        }

    }

    public void startMyService(String response)
    {
        Toast.makeText(this, "  hi "+response, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,ServiceClass.class);
        Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();

        ResultReceiver r=new myreceiver(null);
        intent.putExtra("receiver",r);

        intent.putExtra("ID",response);
        Toast.makeText(this, "here 1", Toast.LENGTH_SHORT).show();
        SERVICE_RUN=true;
        startService(intent);
        Toast.makeText(this, "here 2", Toast.LENGTH_SHORT).show();

    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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


//        @Override
//        protected void onReceiveResult(int resultCode, final Bundle resultData) {
//            if (resultCode==RESULT_CODE){
//
//                if(resultData!=null)
//                {
//                    lattitude=resultData.getString("res_lat");
//                    longitude=resultData.getString("res_lng");
//                }
//            }
//        }

    }

    public void insertlogin(final String response)
    {

//        Toast.makeText(this, "updation", Toast.LENGTH_SHORT).show();
//        callforgetlocation();

//        Toast.makeText(MainActivity.this, " "+id+" ", Toast.LENGTH_SHORT).show();

//        Toast.makeText(this, "going to start", Toast.LENGTH_SHORT).show();
        String url="http://www.thantrajna.com/sjec_task/For_Employers/insert_login.php";
        StringRequest name=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, " hi :"+response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("ID",response+"");
                params.put("LATITUDE",lattitude+"");
                params.put("LONGITUDE",longitude+"");
                return params;
            }
        };
        requestQueue.add(name);
        startMyService(response);
    }
}
