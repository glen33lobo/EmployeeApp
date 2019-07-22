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
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Employee_Main extends AppCompatActivity {


    public static boolean SERVICE_RUN=false;

    private static final int REQUEST_LOCATION = 1;
    public static final int RESULT_CODE =11 ;
    LocationManager locationManager;
    String lattitude,longitude;
    SharedPreferences sp;
    public static final String MSP1="Login";
    Button upb,uploadb;
    ImageButton b;
    String data,data1;
    RequestQueue requestQueue;
    EditText editText;
    Cursor c;
    SQLiteDatabase db;
    int flag=1;
    LinearLayout linearLayout;
    Button loginout;
    private Toolbar toolbar;
    TextView datep;
    int chnger=1;
    String[] ar;
    String id,status_of_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee__main);


        upb=(Button)findViewById(R.id.update_desc);
        uploadb=(Button)findViewById(R.id.upload);
        editText=(EditText)findViewById(R.id.Desciption);
        sp=getSharedPreferences(MSP1, Context.MODE_PRIVATE);
        loginout=(Button) findViewById(R.id.loginout);
        datep=(TextView)findViewById(R.id.dateId);

        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(Employee_Main.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            data = bundle.getString("ID2");
            if(data==null) {
                data = bundle.getString("IDpass");
                ar=data.split("@");
                id=ar[0];
                Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
                checklogin(id);
            }
            else
            {
                Toast.makeText(this, " data "+data, Toast.LENGTH_SHORT).show();
                ar=data.split("@");
                id=ar[0];
                status_of_user=ar[1];
                datep.setText(ar[2]);

                if(status_of_user.equals("Login"))
                {
                    loginout.setBackgroundResource(R.drawable.circle);
                    loginout.setText("LOGGED\nIN");
                }
                else if(status_of_user.equals("Logout"))
                {
                    loginout.setBackgroundResource(R.drawable.circlered);
                    loginout.setText("LOGGOUT");
                    loginout.setText("LOGGED\nOUT");
                }
                Toast.makeText(this, ""+status_of_user, Toast.LENGTH_SHORT).show();
            }


        }



        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_of_user.equals("Logout")) {
                    insertlogin(id);
                    loginout.setBackgroundResource(R.drawable.circle);
                    loginout.setText("LOGGED\nIN");
                    status_of_user="Login";
                    SERVICE_RUN=true;
                }
                else
                {
                    insertlogout(id);
                    loginout.setBackgroundResource(R.drawable.circlered);
                    loginout.setText("LOGGED\nOUT");
                    status_of_user="Logout";
                    SERVICE_RUN=false;
                }
            }
        });



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//
//
//            }
//        });

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Employee_Main.this);
            builder.setMessage("Are you sure to Logout Completely")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            SharedPreferences.Editor ed1 = sp.edit();
                            ed1.clear();
                            ed1.commit();
                            Toast.makeText(Employee_Main.this,"Successfull Logout",Toast.LENGTH_SHORT).show();

                            SERVICE_RUN=false;

                            startActivity(new Intent(Employee_Main.this,MainActivity.class));
                          //  return true;
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

        return super.onOptionsItemSelected(item);
    }

















    private void checklogin(final String id) {
        String url = "http://www.thantrajna.com/sjec_task/For_Employers/check_already_login.php";
        StringRequest name = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String x[]=response.split("@");
                String status_=x[0];
                String da=x[1];
                if(status_.equals("Login"))
                {
                    loginout.setBackgroundResource(R.drawable.circle);
                    loginout.setText("LOGGED\nIN");
                    status_of_user="Login";
                }
                else if(status_.equals("Logout"))
                {
                    loginout.setBackgroundResource(R.drawable.circlered);
                    loginout.setText("LOGGED\nOUT");
                    status_of_user="Logout";
                }
                datep.setText(da);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Employee_Main.this, "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", id);
                return params;
            }
        };
        requestQueue.add(name);
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

            }else {

                System.out.println("Unble to Trace your location");
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

    public void loginout(View view)
    {
        if(flag==1)
        {
//            tv.setBackgroundResource(R.mipmap.logout);
            flag=0;
            upb.setVisibility(View.INVISIBLE);
            uploadb.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.INVISIBLE);
            linearLayout.setBackgroundResource(R.drawable.loggedout1);

        }
        else if(flag==0)
        {
//            final AlertDialog.Builder alert=new AlertDialog.Builder(Employee_Main.this);
//            View mView=getLayoutInflater().inflate(R.layout.custom_dialog,null);
//            final EditText txt_pass=(EditText)mView.findViewById(R.id.password);
//            Button btn_cancel=(Button)mView.findViewById(R.id.btn_cancel);
//            Button btn_ok=(Button)mView.findViewById(R.id.btn_ok);
//
//            alert.setView(mView);
//
//            final  AlertDialog alertDialog=alert.create();
//            alertDialog.setCanceledOnTouchOutside(false);
//
//            btn_cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    alertDialog.dismiss();
//                }
//            });
//
//            btn_ok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(Employee_Main.this, "Done", Toast.LENGTH_SHORT).show();
//                    alertDialog.dismiss();
//
//                    tv.setBackgroundResource(R.mipmap.login);
//                    flag=1;
//                    upb.setVisibility(View.VISIBLE);
//                    uploadb.setVisibility(View.VISIBLE);
//                    editText.setVisibility(View.VISIBLE);
//                }
//            });
//
//            alertDialog.show();
        }
    }


    public void startMyService(String response)
    {
        Intent intent=new Intent(Employee_Main.this,ServiceClass.class);
//        ResultReceiver r=new myreceiver(null);
//        intent.putExtra("receiver",r);
        intent.putExtra("ID",response);
        SERVICE_RUN=true;
        startService(intent);
    }

    public void insertlogin(final String response)
    {
        String[] ar=response.split("@");
        final String id=ar[0];
        String url="http://www.thantrajna.com/sjec_task/For_Employers/insert_login.php";
        StringRequest name=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Employee_Main.this, " hi :"+response, Toast.LENGTH_SHORT).show();
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
                params.put("ID",id+"");
                params.put("LATITUDE",lattitude+"");
                params.put("LONGITUDE",longitude+"");
                return params;
            }
        };
        requestQueue.add(name);
        startMyService(id);
    }
}
