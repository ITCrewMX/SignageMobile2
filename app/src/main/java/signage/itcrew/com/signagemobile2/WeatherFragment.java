package signage.itcrew.com.signagemobile2;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    //private FusedLocationProviderClient mFusedLocationClient;
    Typeface weatherFont;
    String latitude, longitude;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weathericons-regular-webfont.ttf");

        cityField = (TextView) view.findViewById(R.id.city_field);
        updatedField = (TextView) view.findViewById(R.id.updated_field);
        detailsField = (TextView) view.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) view.findViewById(R.id.current_temperature_field);
        //humidity_field = (TextView)view.findViewById(R.id.humidity_field);
        //pressure_field = (TextView)view.findViewById(R.id.pressure_field);
        weatherIcon = (TextView) view.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);


        WeatherClient.placeIdTask asyncTask =new WeatherClient.placeIdTask(new WeatherClient.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                //humidity_field.setText("Humidity: "+weather_humidity);
                //pressure_field.setText("Pressure: "+weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        latitude = prefs.getString("LATITUDE", "19.4372531");
        longitude = prefs.getString("LONGITUDE", "-99.1457678");
        asyncTask.execute(latitude,longitude); //  asyncTask.execute("Latitude", "Longitude")

        return view;
    }



}
