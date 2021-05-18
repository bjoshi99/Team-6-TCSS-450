package edu.uw.team6tcss450.ui.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentSignInBinding;
import edu.uw.team6tcss450.databinding.FragmentWeatherBinding;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "3d3d39ca7103cb14be20aa0681bc291d";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonCity.setOnClickListener(this::getWeatherDetails);
    }

    public void getWeatherDetails(View view) {
        String tempurl = "";
        String city = binding.editTextSearchbar.getText().toString();

        if (city.equals("")) {
            binding.textViewOutput.setText("City field cannot be empty!");
        } else {
            tempurl = url + "?q=" + city + "&appid=" + appid + "&units=imperial";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");
                        double feelsLike = jsonObjectMain.getDouble("feels_like");
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        String output = "Current weather of " + cityName + " (" + countryName + ")"
                                + "\nTemp: " + df.format(temp) + " °F"
                                + "\nFeels Like: " + df.format(feelsLike) + " °F"
                                + "\nHumidity: " + humidity + "%"
                                + "\nDescription: " + description
                                + "\nWind Speed: " + wind + "m/s"
                                + "\nCloudiness: " + clouds + "%"
                                + "\nPressure: " + pressure + "hPa";
                        binding.textViewOutput.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    binding.textViewOutput.setText(error.toString());
                }
            });
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(stringRequest);
        }
    }
}
