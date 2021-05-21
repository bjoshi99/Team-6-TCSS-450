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
import androidx.lifecycle.ViewModelProvider;

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
import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentSignInBinding;
import edu.uw.team6tcss450.databinding.FragmentWeatherBinding;
import edu.uw.team6tcss450.ui.auth.signin.SignInViewModel;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private WeatherViewModel mWeatherModel;

    private String currentCity = "Seattle";
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String urlForecast = "https://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "3d3d39ca7103cb14be20aa0681bc291d";
    DecimalFormat df = new DecimalFormat("#.##");

    WeatherViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
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

        model =
                new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

        getWeatherDetails(model.getCity());

//        getWeatherDetails(currentCity);
        binding.buttonCity.setOnClickListener(button -> getWeatherDetails(binding.editTextSearchbar.getText().toString()));
//        binding.buttonCity.setOnClickListener(this::getWeatherDetails);
    }

    public void getWeatherDetails(String city) {
        String tempurl = "";
//        String city = binding.editTextSearchbar.getText().toString();
        currentCity = city;
        model.setValue(city);

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
                                + "\nTemp: " + df.format(temp) + "° F"
                                + "\nFeels Like: " + df.format(feelsLike) + " °F"
                                + "\nHumidity: " + humidity + "%"
                                + "\nDescription: " + description
                                + "\nWind Speed: " + wind + "mph"
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
                    binding.textViewOutput.setText("Please enter a valid city.");
                }
            });
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(stringRequest);
        }
        get24HourForecastDetails(city);
        getForecastDetails(city);
    }

    public void get24HourForecastDetails(String city) {
        String tempurl = "";
//        String city = binding.editTextSearchbar.getText().toString();

        if (city.equals("")) {
            binding.textView24hour.setText("");
        } else {
            tempurl = urlForecast + "?q=" + city + "&appid=" + appid + "&units=imperial";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("list");

                        StringBuilder str = new StringBuilder();

                        for (int i = 0; i < 8; i++) {
                            JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                            JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp");
                            String time = jsonObjectList.getString("dt_txt").substring(11, 13);
                            str.append("Time: " + time + ", Temp: " + temp + "\n");
                        }

                        binding.textView24hour.setText(str.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    binding.textView24hour.setText("");
                }
            });
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(stringRequest);
        }
    }

    public void getForecastDetails(String city) {
        String tempurl = "";
//        String city = binding.editTextSearchbar.getText().toString();

        if (city.equals("")) {
            binding.textViewForecast.setText("");
        } else {
            tempurl = urlForecast + "?q=" + city + "&appid=" + appid + "&units=imperial";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        int keyElementsIndex = 1;
                        boolean firstElementNotNoon = true;
                        JSONObject[] keyElements = new JSONObject[5];

                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("list");

                        // loop for adding forecast to keyElements
                        for(int i=0; i<jsonArray.length(); i++) {
                            // adds initial value to first element
                            if (keyElements[0] == null) {
                                keyElements[0] = jsonArray.getJSONObject(0);
                            }
                            else {
                                // makes sure first element is 12:00:00
                                if (firstElementNotNoon && checkDates(keyElements[0].getString("dt_txt"), jsonArray.getJSONObject(i).getString("dt_txt"), firstElementNotNoon)) {
                                    keyElements[0] = jsonArray.getJSONObject(i);
                                    firstElementNotNoon = false;
                                }
                                // makes sure it's a different day and at 12:00:00
                                if(checkDates(keyElements[keyElementsIndex-1].getString("dt_txt"), jsonArray.getJSONObject(i).getString("dt_txt"), false)) {
                                    keyElements[keyElementsIndex] = jsonArray.getJSONObject(i);
                                    keyElementsIndex++;
                                }
                                // backup in case the last element is before 12:00:00
                                if(i == jsonArray.length()-1) keyElements[4] = jsonArray.getJSONObject(i);
                            }
                            // to exit
                            if (keyElements[4] != null) i = jsonArray.length()+1;
                        }

                        String output = keyElements[0].getString("dt_txt").substring(6, 10)
                                + " High:" + df.format(keyElements[0].getJSONObject("main").getDouble("temp_max"))
                                + " Low:" + df.format(keyElements[0].getJSONObject("main").getDouble("temp_min"))
                                + " " + keyElements[0].getJSONArray("weather").getJSONObject(0).getString("main")
                                + "\n"
                                + keyElements[1].getString("dt_txt").substring(6, 10) + " High:"
                                + df.format(keyElements[1].getJSONObject("main").getDouble("temp_max"))
                                + " Low:" + df.format(keyElements[1].getJSONObject("main").getDouble("temp_min"))
                                + " " + keyElements[1].getJSONArray("weather").getJSONObject(0).getString("main")
                                + "\n"
                                + keyElements[2].getString("dt_txt").substring(6, 10)
                                + " High:" + df.format(keyElements[2].getJSONObject("main").getDouble("temp_max"))
                                + " Low:" + df.format(keyElements[2].getJSONObject("main").getDouble("temp_min"))
                                + " " + keyElements[2].getJSONArray("weather").getJSONObject(0).getString("main")
                                + "\n"
                                + keyElements[3].getString("dt_txt").substring(6, 10)
                                + " High:" + df.format(keyElements[3].getJSONObject("main").getDouble("temp_max"))
                                + " Low:" + df.format(keyElements[3].getJSONObject("main").getDouble("temp_min"))
                                + " " + keyElements[3].getJSONArray("weather").getJSONObject(0).getString("main")
                                + "\n"
                                + keyElements[4].getString("dt_txt").substring(6, 10)
                                + " High:" + df.format(keyElements[4].getJSONObject("main").getDouble("temp_max"))
                                + " Low:" + df.format(keyElements[4].getJSONObject("main").getDouble("temp_min"))
                                + " " + keyElements[4].getJSONArray("weather").getJSONObject(0).getString("main");

                        binding.textViewForecast.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    binding.textViewForecast.setText("");
                }
            });
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(stringRequest);
        }
    }


    // returns true if dates are different and are at 12:00:00
    private boolean checkDates(String dateBefore, String dateToCheck, boolean isFirstElement) {
        if (isFirstElement) {
            if (dateToCheck.contains("12:00:00")) return true;
            else return false;
        } else {
            if (dateBefore.substring(6, 10).equals(dateToCheck.substring(6, 10))) return false;
            else {
                if (dateToCheck.contains("12:00:00")) return true;
                else return false;
            }
        }
    }
}
