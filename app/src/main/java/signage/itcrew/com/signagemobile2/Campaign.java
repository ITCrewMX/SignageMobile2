package signage.itcrew.com.signagemobile2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
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

import java.io.File;
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
    private int mShortAnimationDuration, mLongAnimationDuration;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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


        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);


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
        int number_of_items = 0;
        File sdcard  = Environment.getRootDirectory();


        for(int i = 0; i < number_of_items; i++){

            //obtener archivo de memoria interna
            try {

                File file = new File(sdcard, String.valueOf(i) + ".mp4");

                // Start the MediaController
                MediaController mediacontroller = new MediaController(
                        Campaign.this);
                mediacontroller.setAnchorView(videoView);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(url);
                videoView.setMediaController(mediacontroller);
                videoView.setVideoPath(video.toString());

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        imageView.setVisibility(View.INVISIBLE);

                        videoView.animate()
                                .alpha(0f)
                                .setDuration(mLongAnimationDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        videoView.setVisibility(View.GONE);
                                    }
                                });

                        imageView.animate()
                                .alpha(1f)
                                .setDuration(mLongAnimationDuration)
                                .setListener(null);
                    }

                });
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
            } catch (Exception e) {

                File file = new File(sdcard, String.valueOf(i) + ".jpg");

                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                //Hide videoview
                videoView.setVisibility(View.INVISIBLE);

                imageView = (ImageView) findViewById(R.id.imageView);

                imageView.setImageBitmap(myBitmap); //assign image to imageview
                imageView.setAlpha(0f);
                imageView.setVisibility(View.VISIBLE);
            }
        }
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
