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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentSignInBinding;
import edu.uw.team6tcss450.databinding.FragmentWeatherBinding;
import edu.uw.team6tcss450.ui.auth.signin.SignInViewModel;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private WeatherViewModel mWeatherModel;
    private List<WeatherModel> weatherList;

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
        weatherList = new ArrayList<>();
        model = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

        getWeatherDetails(model.getCity());

        binding.buttonCity.setOnClickListener(button -> getWeatherDetails(binding.editTextSearchbar.getText().toString()));
    }

    public void getWeatherDetails(String city) {
        String tempurl = "";
        model.setValue(city);

        if (city.equals("")) {
            binding.textViewMainCity.setText("City field cannot be empty!");
        } else {
            tempurl = url + "?q=" + city + "&appid=" + appid + "&units=imperial";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description").toLowerCase();

                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");

                        int iconNum;

                        if (description.contains("sun") || description.contains("clear")) iconNum = R.drawable.ic_baseline_wb_sunny_24;
                        else if (description.contains("rain")) iconNum = R.drawable.rain;
                        else if (description.contains("wind")) iconNum = R.drawable.wind;
                        else if (description.contains("snow")) iconNum = R.drawable.snow;
                        else iconNum = R.drawable.ic_cloud_black_24dp;

                        binding.textViewMainCity.setText(city);
                        binding.textViewMainTemp.setText(df.format(temp) + "° F");
                        binding.textViewMainDescription.setText(description);
                        binding.imageViewMainIcon.setImageResource(iconNum);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    binding.textViewMainCity.setText("Please enter a valid city.");
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
            binding.textViewMainTemp.setText("");
        } else {
            tempurl = urlForecast + "?q=" + city + "&appid=" + appid + "&units=imperial";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("list");

                        JSONObject jsonObjectList = jsonArray.getJSONObject(0);
                        JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                        binding.textViewHour1.setText(jsonObjectList.getString("dt_txt").substring(11, 13));
                        binding.textViewTemp1.setText(String.valueOf((int)jsonObjectMain.getDouble("temp")));

                        jsonObjectList = jsonArray.getJSONObject(1);
                        jsonObjectMain = jsonObjectList.getJSONObject("main");
                        binding.textViewHour2.setText(jsonObjectList.getString("dt_txt").substring(11, 13));
                        binding.textViewTemp2.setText(String.valueOf((int)jsonObjectMain.getDouble("temp")));

                        jsonObjectList = jsonArray.getJSONObject(2);
                        jsonObjectMain = jsonObjectList.getJSONObject("main");
                        binding.textViewHour3.setText(jsonObjectList.getString("dt_txt").substring(11, 13));
                        binding.textViewTemp3.setText(String.valueOf((int)jsonObjectMain.getDouble("temp")));

                        jsonObjectList = jsonArray.getJSONObject(3);
                        jsonObjectMain = jsonObjectList.getJSONObject("main");
                        binding.textViewHour4.setText(jsonObjectList.getString("dt_txt").substring(11, 13));
                        binding.textViewTemp4.setText(String.valueOf((int)jsonObjectMain.getDouble("temp")));

                        jsonObjectList = jsonArray.getJSONObject(4);
                        jsonObjectMain = jsonObjectList.getJSONObject("main");
                        binding.textViewHour5.setText(jsonObjectList.getString("dt_txt").substring(11, 13));
                        binding.textViewTemp5.setText(String.valueOf((int)jsonObjectMain.getDouble("temp")));

                        jsonObjectList = jsonArray.getJSONObject(5);
                        jsonObjectMain = jsonObjectList.getJSONObject("main");
                        binding.textViewHour6.setText(jsonObjectList.getString("dt_txt").substring(11, 13));
                        binding.textViewTemp6.setText(String.valueOf((int)jsonObjectMain.getDouble("temp")));

                        jsonObjectList = jsonArray.getJSONObject(6);
                        jsonObjectMain = jsonObjectList.getJSONObject("main");
                        binding.textViewHour7.setText(jsonObjectList.getString("dt_txt").substring(11, 13));
                        binding.textViewTemp7.setText(String.valueOf((int)jsonObjectMain.getDouble("temp")));

                        jsonObjectList = jsonArray.getJSONObject(7);
                        jsonObjectMain = jsonObjectList.getJSONObject("main");
                        binding.textViewHour8.setText(jsonObjectList.getString("dt_txt").substring(11, 13));
                        binding.textViewTemp8.setText(String.valueOf((int)jsonObjectMain.getDouble("temp")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    binding.textViewMainTemp.setText("");
                }
            });
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(stringRequest);
        }
    }

    public void getForecastDetails(String city) {
        String tempurl = "";

        if (city.equals("")) {
            binding.textViewMainDescription.setText("");
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

                        if (!weatherList.isEmpty()) weatherList.clear();
                        for (int i = 0; i < 5; i++) {
                            addToWeekForecast(keyElements[i].getString("dt_txt").substring(0, 10),
                                    keyElements[i].getJSONArray("weather").getJSONObject(0).getString("main"),
                                    df.format(keyElements[i].getJSONObject("main").getDouble("temp_max")),
                                    df.format(keyElements[i].getJSONObject("main").getDouble("temp_min")));
                        }

                        binding.textViewDayOfWeek1.setText(weatherList.get(0).getDayOfWeek());
                        binding.imageViewIcon1.setImageResource(weatherList.get(0).getImage());
                        binding.textViewHigh1.setText(weatherList.get(0).getTempHigh());
                        binding.textViewLow1.setText(weatherList.get(0).getTempLow());

                        binding.textViewDayOfWeek2.setText(weatherList.get(1).getDayOfWeek());
                        binding.imageViewIcon2.setImageResource(weatherList.get(1).getImage());
                        binding.textViewHigh2.setText(weatherList.get(1).getTempHigh());
                        binding.textViewLow2.setText(weatherList.get(1).getTempLow());

                        binding.textViewDayOfWeek3.setText(weatherList.get(2).getDayOfWeek());
                        binding.imageViewIcon3.setImageResource(weatherList.get(2).getImage());
                        binding.textViewHigh3.setText(weatherList.get(2).getTempHigh());
                        binding.textViewLow3.setText(weatherList.get(2).getTempLow());

                        binding.textViewDayOfWeek4.setText(weatherList.get(3).getDayOfWeek());
                        binding.imageViewIcon4.setImageResource(weatherList.get(3).getImage());
                        binding.textViewHigh4.setText(weatherList.get(3).getTempHigh());
                        binding.textViewLow4.setText(weatherList.get(3).getTempLow());

                        binding.textViewDayOfWeek5.setText(weatherList.get(4).getDayOfWeek());
                        binding.imageViewIcon5.setImageResource(weatherList.get(4).getImage());
                        binding.textViewHigh5.setText(weatherList.get(4).getTempHigh());
                        binding.textViewLow5.setText(weatherList.get(4).getTempLow());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    binding.textViewMainDescription.setText("");
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

    private void addToWeekForecast(String dayOfWeek, String weatherConditions, String tempHigh, String tempLow) {
        String finalDay = "";
        int iconNum;

        Date date = new Date(2021,
                Integer.parseInt(dayOfWeek.substring(6, 7)),
                Integer.parseInt(dayOfWeek.substring(8, 10)));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if (cal.get(Calendar.DAY_OF_WEEK) == 1) finalDay = "Sunday";
        else if (cal.get(Calendar.DAY_OF_WEEK) == 2) finalDay = "Monday";
        else if (cal.get(Calendar.DAY_OF_WEEK) == 3) finalDay = "Tuesday";
        else if (cal.get(Calendar.DAY_OF_WEEK) == 4) finalDay = "Wednesday";
        else if (cal.get(Calendar.DAY_OF_WEEK) == 5) finalDay = "Thursday";
        else if (cal.get(Calendar.DAY_OF_WEEK) == 6) finalDay = "Friday";
        else if (cal.get(Calendar.DAY_OF_WEEK) == 7) finalDay = "Saturday";

        weatherConditions = weatherConditions.toLowerCase();
        if (weatherConditions.contains("sun") || weatherConditions.contains("clear")) iconNum = R.drawable.ic_baseline_wb_sunny_24;
        else if (weatherConditions.contains("rain")) iconNum = R.drawable.rain;
        else if (weatherConditions.contains("wind")) iconNum = R.drawable.wind;
        else if (weatherConditions.contains("snow")) iconNum = R.drawable.snow;
        else iconNum = R.drawable.ic_cloud_black_24dp;

        weatherList.add(new WeatherModel(finalDay, iconNum, tempHigh.substring(0, 2), tempLow.substring(0, 2)));
    }
}
