package signage.itcrew.com.signagemobile2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
/*
* 1 imagen
* 2 no se
* 3 no se
*
* */

public class Campaign extends AppCompatActivity {
    ProgressDialog pDialog;
    VideoView videoView;
    ImageView imageView;
    TextView banner;
    private double latitude, longitude;
    private String jsonResponse, jsonmessage;
    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_campaign);

        imageView = (ImageView) findViewById(R.id.imageView);


        //getting location
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission. ACCESS_COARSE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle("Location")
                        .setMessage("This application needs permission to use your location")
                        .setPositiveButton("OK ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Campaign.this,
                                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSION_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission. ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            }
        }else {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latitude =  location.getLatitude();
            longitude = location.getLongitude();

            //Log.w("error", "no hay error");
        }
        /*if(ContextCompat.checkSelfPermission(Campaign.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Campaign.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(Campaign.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            }
            else{
                LocationManager locationManager = (LocationManager) Campaign.this.getSystemService(Campaign.this.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                try{
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(Campaign.this, "Location not Found", Toast.LENGTH_SHORT).show();
                }
            }
        }*/

        //saving location in shared preferences
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LATITUDE", String.valueOf(latitude)).apply();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LONGITUDE", String.valueOf(longitude)).apply();

        // Create a progressbar
        pDialog = new ProgressDialog(Campaign.this);
        // Set progressbar message
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        videoView = (VideoView) findViewById(R.id.videoView);
        banner = (TextView)findViewById(R.id.textView);
        banner.setText("Bienvenido a Signage Mobile, una nueva forma de contacto empresarial");
        banner.setSelected(true);
        banner.getBackground().setAlpha(80);






        String url ="http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
        Uri vidUri = Uri.parse(url);

        videoView.setVideoURI(vidUri);

        videoView.start();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    Campaign.this);
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                imageView.setVisibility(View.INVISIBLE);
                videoView.start();
                mp.setLooping(false);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.bg1);
            }
        });
    }


    //request permission for location
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission. ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
/*
package com.itcrew.signage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Campaign extends AppCompatActivity {
    ProgressDialog pDialog;
    VideoView videoView;
    ImageView imageView;
    TextView textView;
    private String jsonResponse, jsonMedia, jsonMediaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_campaign);

        //Create a progressbar
        pDialog = new ProgressDialog(Campaign.this);

        //Set progressbar message
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        //Show progressbar
        pDialog.show();

        //Build videoView
        videoView = (VideoView) findViewById(R.id.videoView);

        //Build imageView
        imageView = (ImageView) findViewById(R.id.imageView);

        //Build textView
        textView = (TextView) findViewById(R.id.textView);

        //Build URL
        final String url = "http://10.0.2.2:8080/Signage/SignageResources/SignageRest/peticionCampana";


        //Build Parameter with JSON body
        JSONObject content = new JSONObject();
        try
        {
            content.put("campana_id", "1");
            content.put("request_type", "1");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Convert previous JSON into string
        final String requestBody = content.toString();


        //Send the request with JSON body
        final JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    //Get response status
                    jsonResponse = response.getString("status");
                    jsonMediaType = response.getString("request_type");

                    //If status is ok
                    if(jsonResponse.compareTo("ok") == 0)
                    {
                        // Then get base64 of media.
                        jsonMedia = response.getString("media");

                        // ThenDecode base64 and send the response file to convert it.
                        byte[] decodeValue = Base64.decode(jsonMedia.getBytes(), Base64.NO_WRAP);
                        convertBytesToFile(decodeValue);

                        //If media type is a video
                        if(jsonMediaType.compareTo("1") == 0) {

                            //Send the converted file to videoView and display it
                            Uri vidUri = Uri.parse(url);
                            videoView.setVideoURI(vidUri);
                            videoView.start();

                            try {
                                // Start the MediaController
                                MediaController mediacontroller = new MediaController(Campaign.this);
                                mediacontroller.setAnchorView(videoView);

                                // Get the URL from String VideoURL
                                Uri video = Uri.parse(url);
                                videoView.setMediaController(mediacontroller);
                                videoView.setVideoURI(video);

                                //////////////////////////////////////////////////////////////////////////////////
                                // Tendre que cambiar esto para que reciba el archivo convertido y no de una url//
                                //////////////////////////////////////////////////////////////////////////////////
                            } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                                e.printStackTrace();
                            }

                            videoView.requestFocus();
                            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                // Close the progress bar and play the video
                                public void onPrepared(MediaPlayer mp) {
                                    pDialog.dismiss();
                                    videoView.start();
                                    mp.setLooping(true);
                                }
                            });
                        }

                        //If media type is an image
                        else if (jsonMediaType.compareTo("2") == 0)
                        {

                        }

                        //If media type is text
                        else if(jsonMediaType.compareTo("3") == 0)
                        {

                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //Build the request JSON body
            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            //Build the request header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token-app", "0367F1D1-5463-44DD-8CD2-9E1368E207E8_20170426104639");
                params.put("api-key", "bcaf75d0a47bad12faa1272e_3ad024c8ab85f8c1fe91b61c");
                return params;
            }

            //Specify request content
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };
    }

    private void convertBytesToFile(byte[] bytearray) {
        try
        {
            File outputFile = File.createTempFile("file", "jpeg", getCacheDir());
            outputFile.deleteOnExit();
            FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
            fileoutputstream.write(bytearray);
            fileoutputstream.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
*/