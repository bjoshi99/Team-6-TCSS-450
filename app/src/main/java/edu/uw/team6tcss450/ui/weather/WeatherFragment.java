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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentSignInBinding;
import edu.uw.team6tcss450.databinding.FragmentWeatherBinding;
import edu.uw.team6tcss450.model.LocationViewModel;
import edu.uw.team6tcss450.ui.auth.signin.SignInViewModel;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private List<WeatherModel> weatherList;
    private List<WeatherRecyclerModel> weatherRecyclerList;
    private LocationViewModel locationModel;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String urlForecast = "https://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "3d3d39ca7103cb14be20aa0681bc291d";
    DecimalFormat df = new DecimalFormat("#.##");

    WeatherViewModel model;

    private boolean firstTime = true;

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
        weatherList = new ArrayList<>();
        weatherRecyclerList = new ArrayList<>();
        model = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

        locationModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);

        if (firstTime) {
            model.setWhatever(false);
            model.setLatLon("(" + locationModel.getCurrentLocation().getLatitude() + "," + locationModel.getCurrentLocation().getLongitude() + ")");
            getWeatherDetails(model.getLatLon());
            weatherRecyclerList.clear();
            firstTime = false;
        } else {
            getWeatherDetails(model.getLatLon());
            weatherRecyclerList.clear();
        }


        binding.buttonCity.setOnClickListener(button -> {
            model.setWhatever(true);
            getWeatherDetails(binding.editTextSearchbar.getText().toString());
            weatherRecyclerList.clear();
        });
        binding.buttonCurrent.setOnClickListener(button -> {
            model.setWhatever(false);
            model.setLatLon("(" + locationModel.getCurrentLocation().getLatitude() + "," + locationModel.getCurrentLocation().getLongitude() + ")");
            getWeatherDetails(model.getLatLon());
            weatherRecyclerList.clear();
        });
        binding.buttonMap.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(WeatherFragmentDirections.actionNavigationWeatherToLocationFragment());
        });
    }

    public void getWeatherDetails(String input) {
        String tempurl = "";
        model.setZip(input);

        if (input.equals("")) {
            binding.textViewMainCity.setText("City field cannot be empty!");
        } else {
            if (model.getWhatever()) {
                tempurl = url + "?zip=" + input + "&appid=" + appid + "&units=imperial";
            } else {
                String lat = model.getLatLon().substring(model.getLatLon().indexOf('(') + 1,
                        model.getLatLon().indexOf(','));
                String lon = model.getLatLon().substring(model.getLatLon().indexOf(',') + 1,
                        model.getLatLon().indexOf(')'));
                tempurl = url + "?lat=" + lat + "&lon=" + lon + "&appid=" + appid + "&units=imperial";
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description").toLowerCase();

                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");

                        String cityName = jsonResponse.getString("name");

                        double temp = jsonObjectMain.getDouble("temp");

                        int iconNum;

                        if (description.contains("sun") || description.contains("clear")) iconNum = R.drawable.ic_baseline_wb_sunny_24;
                        else if (description.contains("rain")) iconNum = R.drawable.rain;
                        else if (description.contains("wind")) iconNum = R.drawable.wind;
                        else if (description.contains("snow")) iconNum = R.drawable.snow;
                        else iconNum = R.drawable.ic_cloud_black_24dp;

                        binding.textViewMainCity.setText(cityName);
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
                    binding.imageViewMainIcon.setImageResource(0);
                }
            });
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(stringRequest);
        }
        get24HourForecastDetails(input);
        getForecastDetails(input);
    }

    public void get24HourForecastDetails(String input) {
        String tempurl = "";

        if (input.equals("")) {
            binding.textViewMainTemp.setText("");
        } else {
            if (model.getWhatever()) {
                tempurl = urlForecast + "?zip=" + input + "&appid=" + appid + "&units=imperial";
            } else {
                String lat = model.getLatLon().substring(model.getLatLon().indexOf('(') + 1,
                        model.getLatLon().indexOf(','));
                String lon = model.getLatLon().substring(model.getLatLon().indexOf(',') + 1,
                        model.getLatLon().indexOf(')'));
                tempurl = urlForecast + "?lat=" + lat + "&lon=" + lon + "&appid=" + appid + "&units=imperial";
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("list");

                        if (!weatherRecyclerList.isEmpty()) weatherList.clear();
                        for (int i = 0; i<8; i++) {
                            JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                            JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                            addTo24HourForecast(
                                    Integer.parseInt(jsonObjectList.getString("dt_txt").substring(11, 13)),
                                    String.valueOf((int)jsonObjectMain.getDouble("temp"))
                            );
                            for (int j=1; j<=2; j++) {
                                addTo24HourForecast(Integer.parseInt(jsonObjectList.getString("dt_txt").substring(11, 13)) + j, "999");
                            }
                        }
                        addTemp();
                        binding.recyclerView24Hour.setAdapter(new WeatherRecyclerViewAdapter(weatherRecyclerList));
                        binding.recyclerView24Hour.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

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

    public void getForecastDetails(String input) {
        String tempurl = "";

        if (input.equals("")) {
            binding.textViewMainDescription.setText("");
        } else {
            if (model.getWhatever()) {
                tempurl = urlForecast + "?zip=" + input + "&appid=" + appid + "&units=imperial";
            } else {
                String lat = model.getLatLon().substring(model.getLatLon().indexOf('(') + 1,
                        model.getLatLon().indexOf(','));
                String lon = model.getLatLon().substring(model.getLatLon().indexOf(',') + 1,
                        model.getLatLon().indexOf(')'));
                tempurl = urlForecast + "?lat=" + lat + "&lon=" + lon + "&appid=" + appid + "&units=imperial";
            }

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
                    binding.textViewDayOfWeek1.setText("");
                    binding.imageViewIcon1.setImageResource(0);
                    binding.textViewHigh1.setText("");
                    binding.textViewLow1.setText("");

                    binding.textViewDayOfWeek2.setText("");
                    binding.imageViewIcon2.setImageResource(0);
                    binding.textViewHigh2.setText("");
                    binding.textViewLow2.setText("");

                    binding.textViewDayOfWeek3.setText("");
                    binding.imageViewIcon3.setImageResource(0);
                    binding.textViewHigh3.setText("");
                    binding.textViewLow3.setText("");

                    binding.textViewDayOfWeek4.setText("");
                    binding.imageViewIcon4.setImageResource(0);
                    binding.textViewHigh4.setText("");
                    binding.textViewLow4.setText("");

                    binding.textViewDayOfWeek5.setText("");
                    binding.imageViewIcon5.setImageResource(0);
                    binding.textViewHigh5.setText("");
                    binding.textViewLow5.setText("");
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

    private void addTo24HourForecast(int time, String temp) {
        StringBuilder sb = new StringBuilder();

        if (time < 12) {
            if (time == 0) sb.append("12");
            else sb.append(time);
            sb.append(":00 AM");
        } else if (time == 12) {
            sb.append(time);
            sb.append(":00 PM");
        }
        else {
            sb.append(time - 12);
            sb.append(":00 PM");
        }

        weatherRecyclerList.add(new WeatherRecyclerModel(sb.toString(),temp));
    }

    private void addTemp() {
        for (int i = 0; i<weatherRecyclerList.size(); i++) {
            if (i == 0 || i == 3 || i == 6 || i == 9 || i == 12 || i == 15 || i == 18 || i == 21) {
                weatherRecyclerList.get(i).setTemp(weatherRecyclerList.get(i).getTemp() + "°F");
            } else if (i == 23 || i == 22) {
                weatherRecyclerList.get(i).setTemp(weatherRecyclerList.get(21).getTemp());
            } else if (i % 3 == 1) {
                int below = Integer.parseInt(weatherRecyclerList.get(i-1).getTemp().replaceAll("[^\\d.]", ""));
                int above = Integer.parseInt(weatherRecyclerList.get(i+2).getTemp().replaceAll("[^\\d.]", ""));
                int difference = above - below;
                double range = difference*.3333;
                int finalTemp = (int) (below + range);
                weatherRecyclerList.get(i).setTemp(finalTemp + "°F");
            } else if (i % 3 == 2) {
                int below = Integer.parseInt(weatherRecyclerList.get(i-2).getTemp().replaceAll("[^\\d.]", ""));
                int above = Integer.parseInt(weatherRecyclerList.get(i+1).getTemp().replaceAll("[^\\d.]", ""));
                int difference = above - below;
                double range = difference*.6666;
                int finalTemp = (int) (below + range);
                weatherRecyclerList.get(i).setTemp(finalTemp + "°F");
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
