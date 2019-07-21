package com.example.android.employeeapp;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

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

public class ServiceClass extends IntentService {


    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private Timer t;//=new Timer(String.valueOf(Looper.getMainLooper()));
    String lattitude,longitude;
    RequestQueue requestQueue;
    private Context context;
    String id;
    private TimerTask task;
    Handler mHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue= Volley.newRequestQueue(this);
        t=new Timer(String.valueOf(Looper.getMainLooper()));
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
        mHandler.post(new DisplayToast(this, "Hello World!"));

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
        System.out.println("\n\n\n\nservice running\n\n\n\n");
        String url="http://www.thantrajna.com/sjec_task/For_Employers/Track_loc_upload.php";
        StringRequest name=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("ID",id+"");
                params.put("Latitude",lattitude);
                params.put("Longitude",longitude);
                return params;
            }
        };
        requestQueue.add(name);
    }

    public class DisplayToast implements Runnable {
        private final Context mContext;
        String mText;

        public DisplayToast(Context mContext, String text){
            this.mContext = mContext;
            mText = text;
        }

        public void run(){
            Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
        }
    }
}
