package signage.itcrew.com.signagemobile2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.*;

public class LoginActivity extends AppCompatActivity {

    private EditText stoken;
    private ProgressDialog pDialog;
    private String jsonResponse, jsonmessage, jsonUser;

    private static String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        //Get values from textfields
        stoken = (EditText) findViewById(R.id.stoken);
        //password = (EditText) findViewById(R.id.password);
        //password.setTransformationMethod(new PasswordTransformationMethod());


        //Stablish a loading message
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        Button btn_access = (Button) findViewById(R.id.loginButton);

        //Execute function when click
        btn_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJSONObjectRequest();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        stoken.getText().clear();

    }

    private void makeJSONObjectRequest(){
        showpDialog();

        //Establish url
        String url = "http://192.168.1.117:8080/Signage2.2/SignageResources/SignageRest/initSession";
        //String url = "http://10.0.2.2:8084/Signage2/SignageResources/SignageRest/initSession";


        JSONObject content = new JSONObject();

        //Build Parameter
        try {

            //String encodedPwd = encodeMD5(password.getText().toString());
            //byte[] encodeValue = Base64.encode(password.getText().toString().getBytes(), Base64.NO_WRAP);
            String pushtoken = FirebaseInstanceId.getInstance().getToken();
            Log.w("push token: ", pushtoken);
            content.put("stoken", stoken.getText().toString());
            //content.put("password", encodedPwd);
            //content.put("password", new String(encodeValue));
            content.put("so", "Android");
            content.put("tokenPush", pushtoken);
            content.put("device", "Device");
            //content.put("tokenDispositivo", "");
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        final String requestBody = content.toString();

        //Start request to Server
        final JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Get response
                try
                {
                    Log.i("JSON STATUS", jsonResponse = response.getString("status"));
                    Log.i("JSON ERROR", jsonmessage = response.getString("mensaje"));
                    Log.i("JSON USER", jsonUser = response.getString("usuario"));

                    if(jsonResponse.compareTo("ok") == 0) {
                        Toast.makeText(getApplicationContext(), jsonmessage, Toast.LENGTH_LONG).show();
                        //adding user to shared preferences
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USER", jsonUser).apply();
                        Intent mainIntent = new Intent().setClass(LoginActivity.this, signage.itcrew.com.signagemobile2.Campaign.class);
                        startActivity(mainIntent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), jsonmessage, Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        }){
            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token-app", "0367F1D1-5463-44DD-8CD2-9E1368E207E8_20170426104639");
                params.put("api-key", "bcaf75d0a47bad12faa1272e_3ad024c8ab85f8c1fe91b61c");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonrequest);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public static String encodeMD5(String pwd) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pwd.getBytes(), 0, pwd.length());
            pwd = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pwd.length() < 32) {
                pwd = "0" + pwd;
            }
            password = pwd;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }




}
