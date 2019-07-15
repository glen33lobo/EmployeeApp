package com.example.android.employeeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn=(Button)findViewById(R.id.login_button);
        emp_btn=(Button)findViewById(R.id.emp_login);
        user=(TextView)findViewById(R.id.uname);
        pass=(TextView)findViewById(R.id.upass);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        sp=getSharedPreferences(MSP, Context.MODE_PRIVATE);
        db=openOrCreateDatabase("Login_Details",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users(name varchar,pass varchar);");

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,All_Employers.class);
                startActivity(i);
            }
        });
        emp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String url="http://www.thantrajna.com/sjec_task/For_Employers/Emplogin_check.php";
                    StringRequest name=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("error")) {
                                Toast.makeText(MainActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "res: " + response, Toast.LENGTH_SHORT).show();

                                //registation
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
                                            startActivity(new Intent(MainActivity.this, Employee_Main.class));
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
                        } }, new Response.ErrorListener() {
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
                            params.put("username",user.getText().toString());
                            params.put("password",pass.getText().toString());
                            return params;
                        }
                    };
                    requestQueue.add(name);
                }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sp.contains(log)) {
            startActivity(new Intent(MainActivity.this,Employee_Main.class));
        }

    }

}
