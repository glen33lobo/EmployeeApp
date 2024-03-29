package com.example.android.employeeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UMainActivity extends AppCompatActivity {

    RequestQueue queue;
    private static final int PICK_IMAGE_REQUEST= 99;
    private static final int PICK_FILE_REQUEST= 100;
    String senddata,name,sendfiledata;
    Bitmap bm,bmf;
    ImageView im;
    Button bt,filebtn,savef;
    EditText et;
    ProgressDialog progressDialog;
    String trid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umain);


        queue = Volley.newRequestQueue(this);
        im=(ImageView)findViewById(R.id.img);
        bt=(Button)findViewById(R.id.btn);
        filebtn=(Button)findViewById(R.id.filebtn);
        savef=(Button)findViewById(R.id.savefile);

        progressDialog=new ProgressDialog(UMainActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading..");

        savef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                sendFile();
            }
        });

        filebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChoser();
            }
        });

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChoser();
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    progressDialog.show();
                    sendTheData();

            }
        });

//initial part
        Bundle bundle = getIntent().getExtras();
        trid = bundle.getString("id");
            Toast.makeText(this, "id" +trid, Toast.LENGTH_SHORT).show();



        //initial part




    }
    private void sendFile() {
        String url="http://www.thantrajna.com/sjec_task/upload_files/upload.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(UMainActivity.this, "res:"+response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(UMainActivity.this, "er:"+error, Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("file",sendfiledata);
                params.put("tr_id",trid);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    //code to choose file from mobile
    private void fileChoser() {
        Intent i=new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"pick file"),PICK_FILE_REQUEST);
    }

    //volley code to send image
    private void sendTheData() {
        String url="http://www.thantrajna.com/sjec_task/upload_files/upload1.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(UMainActivity.this, " res:"+response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(UMainActivity.this, "er:"+error, Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("file",senddata);
                params.put("tr_id",trid);

                return params;
            }
        };
        queue.add(stringRequest);
    }




    //chose image from gallery
    private void imageChoser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"pick image"),PICK_IMAGE_REQUEST);
    }
    //to handel the statActivityForResult() called in Chooser methods
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri imagepicked=data.getData();
                InputStream is=null;
                try{
                    is=getContentResolver().openInputStream(imagepicked);
                }catch(Exception e){
                    e.printStackTrace();
                }
                bm= BitmapFactory.decodeStream(is);
                im.setImageURI(imagepicked);
                senddata=BitMapToString(bm);
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Trouble picking image", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode==PICK_FILE_REQUEST){
            if(resultCode==RESULT_OK){
                Uri filepath=data.getData();
                InputStream isf=null;
                int len=0;
                byte[] b=null;
                try{
                    isf=getContentResolver().openInputStream(filepath);
                    len=isf.available();
                    b=new byte[len];
                    isf.read(b);
                }catch (Exception e){
                    e.printStackTrace();
                }

                sendfiledata=bitToString(b);

            }else{
                Toast.makeText(this, "Trouble picking file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //this is convert the file bits obtained from input stream to string
    private String bitToString(byte[] b) {
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    //This is convert image Bitmap to string
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}


