package signage.itcrew.com.signagemobile2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    private ImageView imageView;
    private Integer value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        int waitTime = 2000;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent mainIntent = new Intent().setClass(Splash.this, signage.itcrew.com.signagemobile2.LoginActivity.class);
                startActivity(mainIntent);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, waitTime);


    }

}
