package signage.itcrew.com.signagemobile2;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.MediaController;
import android.widget.TextView;
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
    TextView banner;
    private String jsonResponse, jsonmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_campaign);

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
                videoView.start();
                mp.setLooping(true);
            }
        });
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