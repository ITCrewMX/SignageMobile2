package signage.itcrew.com.signagemobile2;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Xml;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class Downloader {

    private String jsonResponse, jsonMedia, jsonMediaType, full_path;
    private String url;
    private FileOutputStream fos;

    public void Downloader (final Context context)
    {
        url = "http://10.0.2.2:8080/Signage/SignageResources/SignageRest/peticionCampana";
        fos=null;
        updateCamapign(context);
    }

    public void updateCamapign(final Context context){

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
                String file_name = null;
                String file_type = null;
                String file_path = null;
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
                            try {
                                if (jsonMedia != null) {
                                    fos = context.openFileOutput("1.mp4", Context.MODE_PRIVATE);
                                    byte[] decodedString = android.util.Base64.decode(jsonMedia, android.util.Base64.DEFAULT);
                                    fos.write(decodedString);
                                    fos.flush();
                                    fos.close();
                                }

                            } catch (Exception e) {

                            } finally {
                                if (fos != null) {
                                    fos = null;
                                }
                            }
                        }

                        //If media type is an image
                        else if (jsonMediaType.compareTo("2") == 0)
                        {
                            try {
                                if (jsonMedia != null) {
                                    fos = context.openFileOutput("2.jpg", Context.MODE_PRIVATE);
                                    byte[] decodedString = android.util.Base64.decode(jsonMedia, android.util.Base64.DEFAULT);
                                    fos.write(decodedString);
                                    fos.flush();
                                    fos.close();
                                }

                            } catch (Exception e) {

                            } finally {
                                if (fos != null) {
                                    fos = null;
                                }
                            }
                        }

                        //If media type is text
                        else if(jsonMediaType.compareTo("3") == 0)
                        {
                            String banner = null;
                            //Add text to shared preferences with "BANNER" tag
                            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("BANNER", banner).apply();
                        }

                        final String xmlFile = "userData";

                        try {
                            FileOutputStream fos = new  FileOutputStream("campaign_data.xml");
                            FileOutputStream fileos= context.openFileOutput(xmlFile, Context.MODE_PRIVATE);
                            XmlSerializer xmlSerializer = Xml.newSerializer();
                            StringWriter writer = new StringWriter();
                            xmlSerializer.setOutput(writer);
                            xmlSerializer.startDocument("UTF-8", true);
                            xmlSerializer.startTag(null, "campaign_data");
                            xmlSerializer.startTag(null, "file_name");
                            xmlSerializer.text(file_name);
                            xmlSerializer.endTag(null, "file_name");
                            xmlSerializer.startTag(null,"file_type");
                            xmlSerializer.text(file_type);
                            xmlSerializer.endTag(null, "file_type");
                            xmlSerializer.startTag(null,"file_path");
                            xmlSerializer.text(file_path);
                            xmlSerializer.endTag(null, "file_path");
                            xmlSerializer.endTag(null, "campaign_data");
                            xmlSerializer.endDocument();
                            xmlSerializer.flush();
                            String dataWrite = writer.toString();
                            fileos.write(dataWrite.getBytes());
                            fileos.close();
                        }
                        catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
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
            File outputFile = File.createTempFile("file", "jpeg",   getCacheDir());
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