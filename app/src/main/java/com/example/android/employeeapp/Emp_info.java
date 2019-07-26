package com.example.android.employeeapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Emp_info extends Fragment {

    public static boolean SERVICE_RUN = false;
    //    private static final int PICK_FILE_REQUEST= 100;
    private static final int MY_CAMERA_REQUEST_CODE = 100;


    private static final int REQUEST_LOCATION = 1;
    public static final int RESULT_CODE = 11;
    LocationManager locationManager;
    String lattitude, longitude;

    Button upb,  work_create;
    ImageButton b;
    String data, name, sendfiledata, id, status_of_user;
    RequestQueue requestQueue, queue;
    EditText editText, nam;
    Cursor c;
    SQLiteDatabase db;
    LinearLayout linearLayout;
    Button loginout, uploadobj;
    private Toolbar toolbar;
    TextView datep, title;
    int chnger = 1, flag = 1;
    String[] ar;
    Handler h = new Handler();
    Intent intentser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootv = inflater.inflate(R.layout.fragment_emp_main, container, false);

        work_create = (Button) rootv.findViewById(R.id.create_work);
        title = (TextView) rootv.findViewById(R.id.title);
        upb = (Button) rootv.findViewById(R.id.upload_info);
        editText = (EditText) rootv.findViewById(R.id.Desciption);
        loginout = (Button) rootv.findViewById(R.id.loginout);
        datep = (TextView) rootv.findViewById(R.id.dateId);
        queue = Volley.newRequestQueue(getContext());
        intentser = new Intent(getContext(), ServiceClass.class);


        title.setVisibility(View.INVISIBLE);
        upb.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.INVISIBLE);


        work_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work_create.setVisibility(View.INVISIBLE);
                title.setVisibility(View.VISIBLE);
                upb.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
            }
        });


        requestQueue = Volley.newRequestQueue(getContext());

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            data = bundle.getString("ID2");
            if (data == null) {
                data = bundle.getString("IDpass");
                ar = data.split("@");
                id = ar[0];
                Toast.makeText(getContext(), " iiidd" + id, Toast.LENGTH_SHORT).show();
                checklogin(id);
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
                    editText.setVisibility(View.INVISIBLE);

                    startMyService(id);
                    loginout.setBackgroundResource(R.drawable.circle);
                    loginout.setText("LOGGED\nIN");
                } else if (status_of_user.equals("Logout")) {
                    work_create.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    upb.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);

                    loginout.setBackgroundResource(R.drawable.circlered);
                    loginout.setText("LOGGED\nOUT");

                }
                Toast.makeText(getActivity(), "" + status_of_user, Toast.LENGTH_SHORT).show();
            }


        }


//        uploadobj.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                name = nam.getText().toString();
//                sendFile();
////                startActivity(new Intent(Employee_Main.this,Emp_main.class));
//            }
//        });


        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_of_user.equals("Logout")) {

                    work_create.setVisibility(View.VISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    upb.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    insertlogin(id);
                    loginout.setBackgroundResource(R.drawable.circle);
                    loginout.setText("LOGGED\nIN");
                    status_of_user = "Login";
                    SERVICE_RUN = true;
                    ServiceClass.t=new Timer(String.valueOf(Looper.getMainLooper()));


                } else {

                    work_create.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    upb.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    insertlogout(id);
                    loginout.setBackgroundResource(R.drawable.circlered);
                    loginout.setText("LOGGED\nOUT");
                    status_of_user = "Logout";
                    SERVICE_RUN = false;
                }
            }
        });


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        upb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                work_create.setVisibility(View.VISIBLE);
                title.setVisibility(View.INVISIBLE);
                upb.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.INVISIBLE);
                if (editText.getText().toString().trim().length() > 0) {
                    update(data);
                } else {
                    Toast.makeText(getContext(), "Nothing to  update", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return rootv;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Toast.makeText(getContext(), " reqcode=1 ", Toast.LENGTH_SHORT).show();
                File f = new File(Environment.getExternalStorageDirectory().toString());
                Toast.makeText(getContext(), "" + f, Toast.LENGTH_SHORT).show();
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
//                    viewImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Toast.makeText(getContext(), " reqcode=1 ", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), " data " + data.toString(), Toast.LENGTH_SHORT).show();


                Uri imagepicked = data.getData();
                InputStream is = null;
                try {
                    is = getActivity().getContentResolver().openInputStream(imagepicked);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Toast.makeText(this, ""+imagepicked.getPath(), Toast.LENGTH_SHORT).show();
//                ImageView im = (ImageView) findViewById(R.id.pic);
                Bitmap bm = BitmapFactory.decodeStream(is);
//                im.setImageURI(imagepicked);
                String senddata = BitMapToString(bm);
//                Toast.makeText(this, "senddata:" + senddata, Toast.LENGTH_SHORT).show();
//                sendTheData(senddata);
            } else {
                Toast.makeText(getContext(), "Trouble picking image", Toast.LENGTH_SHORT).show();
            }


//                Uri selectedImage = data.getData();
//                String[] filePath = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                Log.w("path of image from gallery......******************.........", picturePath+"");
//                viewImage.setImageBitmap(thumbnail);
        }
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }


    private void sendFile() {
        String url = "https://yellowapp.000webhostapp.com/sendimage/savefile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "" + response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("file", sendfiledata);
                params.put("name", name);

                return params;
            }
        };
        queue.add(stringRequest);
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//
//
////        if(requestCode==PICK_IMAGE_REQUEST) {
////            if (resultCode == RESULT_OK) {
////                Uri imagepicked=data.getData();
////                InputStream is=null;
////                try{
////                    is=getContentResolver().openInputStream(imagepicked);
////                }catch(Exception e){
////                    e.printStackTrace();
////                }
////                bm= BitmapFactory.decodeStream(is);
////                im.setImageURI(imagepicked);
////                senddata=BitMapToString(bm);
////            } else {
////                Toast.makeText(this, "Trouble picking image", Toast.LENGTH_SHORT).show();
////            }
////
////        }else
//        Toast.makeText(this, " "+requestCode, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "val: "+data, Toast.LENGTH_SHORT).show();
//        if(requestCode==2){
//            if(resultCode==RESULT_OK){
//                Uri filepath=data.getData();
//                Toast.makeText(this, "v : ", Toast.LENGTH_SHORT).show();
//                InputStream isf=null;
//                int len=0;
//                byte[] b=null;
//                try{
//                    isf=getContentResolver().openInputStream(filepath);
//                    len=isf.available();
//                    b=new byte[len];
//                    isf.read(b);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                sendfiledata=bitToString(b);
//
//            }else{
//                Toast.makeText(Employee_Main.this, "Trouble picking file", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else
//        {
//            Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
//        }
//    }


    //this is convert the file bits obtained from input stream to string
    private String bitToString(byte[] b) {
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    //This is convert image Bitmap to string
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    private void sendTheData(final String senddata) {
        String url = "https://yellowapp.000webhostapp.com/sendimage/save.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "" + response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("image", senddata);
                params.put("name", name);

                return params;
            }
        };
        queue.add(stringRequest);
    }


    private void checklogin(final String id) {
        String url = "http://www.thantrajna.com/sjec_task/For_Employers/check_already_login.php";
        StringRequest name = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String x[] = response.split("@");
                String status_ = x[0];
                String da = x[1];
                if (status_.equals("Login")) {
                    startMyService(id);
                    work_create.setVisibility(View.VISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    upb.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    startMyService(id);
                    loginout.setBackgroundResource(R.drawable.circle);
                    loginout.setText("LOGGED\nIN");
                    status_of_user = "Login";
                    Toast.makeText(getActivity(), "with SP login", Toast.LENGTH_SHORT).show();
                } else if (status_.equals("Logout")) {

                    work_create.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    upb.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);

                    title.setVisibility(View.INVISIBLE);
                    upb.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    loginout.setBackgroundResource(R.drawable.circlered);
                    loginout.setText("LOGGED\nOUT");
                    Toast.makeText(getActivity(), "with SP logout", Toast.LENGTH_SHORT).show();
                    status_of_user = "Logout";
                }
                datep.setText(da);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checklogin(id);
                Toast.makeText(getActivity(), "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", id);
                System.out.println("id=" + id);
                return params;
            }
        };
        requestQueue.add(name);
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AskforPermission();

        } else {
            double latti, longi;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Toast.makeText(getActivity(), "Location: " + lattitude + " " + longitude, Toast.LENGTH_SHORT).show();

            } else if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            } else {

                System.out.println("Unble to Trace your location");
            }
        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


    public void AskforPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    }

    public void insertlogout(final String data) {

        String url = "http://www.thantrajna.com/sjec_task/For_Employers/insert_logout.php";
        StringRequest name = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "" + response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", data + "");
                params.put("LATITUDE", lattitude + "");
                params.put("LONGITUDE", longitude + "");
                return params;
            }
        };
        requestQueue.add(name);

    }

    public void update(final String data) {
        String url = "http://www.thantrajna.com/sjec_task/For_Employers/insert_workmain.php";
        StringRequest name = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "" + response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String description = editText.getText().toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", data + "");
                params.put("LATITUDE", lattitude + "");
                params.put("LONGITUDE", longitude + "");
                params.put("DESCRIPTION", description + "");
                return params;
            }
        };
        requestQueue.add(name);

    }


//    public void uploadfile()
//    {
//        ExampleBottomSheetDialog bottomSheetDialog=new ExampleBottomSheetDialog();
//        bottomSheetDialog.show(getFragmentManager(),"examplebottomsheet");
//
//    }

    public void loginout(View view) {
        if (flag == 1) {
//            tv.setBackgroundResource(R.mipmap.logout);
            flag = 0;
            upb.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.INVISIBLE);
            linearLayout.setBackgroundResource(R.drawable.loggedout1);

        } else if (flag == 0) {
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
//                    editText.setVisibility(View.VISIBLE);
//                }
//            });
//
//            alertDialog.show();
        }
    }


    public void startMyService(String response) {

        ResultReceiver r = new Emp_info.myreceiver(null);
        intentser.putExtra("receiver", r);
        intentser.putExtra("ID", id);
        SERVICE_RUN = true;
        Toast.makeText(getActivity(), " service start ", Toast.LENGTH_SHORT).show();
        System.out.println("Hello");
        getActivity().startService(intentser);
    }

    public void insertlogin(final String response) {
        String[] ar = response.split("@");
        final String id = ar[0];
        String url = "http://www.thantrajna.com/sjec_task/For_Employers/insert_login.php";
        StringRequest name = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), " hi :" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", id + "");
                params.put("LATITUDE", lattitude + "");
                params.put("LONGITUDE", longitude + "");
                return params;
            }
        };
        requestQueue.add(name);
        startMyService(id);
    }


    public class myreceiver extends ResultReceiver {

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
            if (resultCode == RESULT_CODE && resultData != null) {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        lattitude = resultData.getString("lat");
                        longitude = resultData.getString("lng");
//                        Toast.makeText(getContext(), lattitude + "  " + longitude, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectImage() {
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}