package com.example.android.employeeapp;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceClass extends IntentService {


    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Timer t;//=new Timer(String.valueOf(Looper.getMainLooper()));
    String lattitude,longitude;
    RequestQueue requestQueue;
    private Context context;
    String id;
    private TimerTask task;
    Handler mHandler;
    FusedLocationProviderClient mFusedLocationClient;
    Boolean run=false;


    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue= Volley.newRequestQueue(this);
        t=new Timer(String.valueOf(Looper.getMainLooper()));
        mFusedLocationClient = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(2000);
        locationRequest.setInterval(4000);
//        mFusedLocationClient.removeLocationUpdates()



    }


    public ServiceClass(Context context)
    {
        super("Sample");
        this.context=context;
    }

    public ServiceClass()
    {
        super("Sample");
        this.context=context;
        mHandler = new Handler();


    }
    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        requestQueue= Volley.newRequestQueue(this);
        System.out.println("yeah srvicing");

        if(Employee_Main.SERVICE_RUN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    System.out.println("heyyy srvicing");

                    return;
                }
            }
            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    System.out.println("srvicing");

                    super.onLocationResult(locationResult);
                    lattitude = locationResult.getLastLocation().getLatitude() + "";
                    longitude = locationResult.getLastLocation().getLongitude() + "";
                    id = intent.getStringExtra("ID");
                    performupload();
                    ResultReceiver rr = intent.getParcelableExtra("receiver");

                    Bundle b = new Bundle();
                    b.putString("lat", lattitude);
                    b.putString("lng", longitude);
                    rr.send(Employee_Main.RESULT_CODE, b);
                }
            }, getMainLooper());
        }else
        {

        }

        t.scheduleAtFixedRate(task=new TimerTask() {
            @Override
            public void run() {
                System.out.println("\n\n\n\n\n\n\n\nhere its timer\n\n\n\n\n\n\n\n\n\n\n\n");

                if(Employee_Main.SERVICE_RUN) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    id = intent.getStringExtra("ID");
                    getLocation();
                    System.out.println("\n\n\n\n\n\n\n\nhere its running\n\n\n\n\n\n\n\n\n\n\n\n");

                }
                else
                {
                    t.cancel();
                    t.purge();
                    System.out.println("\n\n\n\n\n\n\n\nhere its stopped\n\n\n\n\n\n\n\n\n\n\n\n");

                }

            }
        },0,3000);


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            } else  if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
            } else  if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
            }else{
                System.out.println("Unble to Trace your location");
            }
            performupload();
        }
    }


    public void performupload()
    {
        if(run==false) {
            run=true;
            System.out.println("\n\n\n\nservice running\n\n\n\n");
            String url = "http://www.thantrajna.com/sjec_task/For_Employers/Track_loc_upload.php";
            StringRequest name = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Result received");
                    run=false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    run=false;
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ID", id + "");
                    params.put("LATITUDE", lattitude);
                    params.put("LONGITUDE", longitude);
                    System.out.println(id + " " + lattitude + " " + longitude);
                    return params;
                }
            };
            requestQueue.add(name);
        }
    }

















}
