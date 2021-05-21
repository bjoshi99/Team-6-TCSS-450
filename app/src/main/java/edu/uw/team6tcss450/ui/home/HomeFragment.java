package edu.uw.team6tcss450.ui.home;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentHomeBinding;
import edu.uw.team6tcss450.model.UserInfoViewModel;
import edu.uw.team6tcss450.ui.weather.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ArrayList<HomeModel> notifcationList;
    private FragmentHomeBinding binding;
    private WeatherViewModel modelWeather;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        makeUp();

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView_Home);

        HomeRecyclerViewAdapter listAdapter = new HomeRecyclerViewAdapter(notifcationList);
        mRecyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();


//        return view;

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        modelWeather = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        grabWeatherDetails(modelWeather.getCity());

        grabDate();
        binding.datehome.setText(grabDate());

        //for now, email is displayed on home but for future, we can change it to username
        FragmentHomeBinding.bind(getView()).texttile.setText("Welcome " + model.getEmail() + " !");
    }

    private void makeUp() {
        notifcationList = new ArrayList<>();

        notifcationList.add(new HomeModel("Updated layout.", "Alexis" , "123", "9:04 pm"));
        notifcationList.add(new HomeModel("Good night!", "Jun" , "123", "11:15 pm"));
        notifcationList.add(new HomeModel("Just found this bug!", "Rj" , "123", "6:48 pm"));
        notifcationList.add(new HomeModel("RJ found a bug, need to fix asap.", "Bhavesh" , "123", "7:55 pm"));
        notifcationList.add(new HomeModel("Upcoming meeting: 9:30 am.", "Charles" , "123", "10:00 pm"));
    }

    private String grabDate() {
        StringBuilder sb = new StringBuilder();
        System.currentTimeMillis();
        SimpleDateFormat formatter= new SimpleDateFormat("MM dd, yyyy");
        Date date = new Date(System.currentTimeMillis());

        if (formatter.format(date).charAt(1) == '5') sb.append("May");
        else sb.append("June");

        sb.append(formatter.format(date).substring(2, 11));
        return sb.toString();
    }

    private void grabWeatherDetails(String city) {
        String tempurl = "";
        final String appid = "3d3d39ca7103cb14be20aa0681bc291d";
        DecimalFormat df = new DecimalFormat("#.##");

        tempurl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + appid + "&units=imperial";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
//                    String icon = jsonObjectWeather.getString("icon");
//                    String iconURL = "https://openweathermap.org/img/w/" + icon + ".png";

                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp");
                    double feelsLike = jsonObjectMain.getDouble("feels_like");
                    int humidity = jsonObjectMain.getInt("humidity");
                    double min = jsonObjectMain.getDouble("temp_min");
                    double max = jsonObjectMain.getDouble("temp_max");
                    String icon = jsonObjectWeather.getString("icon");
                    String iconURL = "https://openweathermap.org/img/w/" + icon + ".png";

                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String windSpeed = jsonObjectWind.getString("speed");

                    String outputTemp = df.format(temp) + "° F";
                    String outputCityName = "Currently in " + jsonResponse.getString("name");
                    String outputMinMax = "Min/Max: " + df.format(min) + "° / " + df.format(max) + "° F";
                    String outputWindSpeed = "Wind Speed: " + windSpeed + "mph";
                    String outputHumidity = "Humidity: " + humidity + "%";
                    String outputDescription = jsonObjectWeather.getString("description");

//                    try {
//                        URL url = new URL(iconURL);
//
//                        binding.imageIconHome.setImageBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
//                    } catch (Exception e) {
//                        System.out.println("nah");
//                    }

//                    Picasso.with(context).load(imageID).into(imageView);

                    binding.textTempHome.setText(outputTemp);
                    binding.textCityHome.setText(outputCityName);
                    binding.textMinmaxHome.setText(outputMinMax);
                    binding.textHumidityHome.setText(outputHumidity);
                    binding.textWindHome.setText(outputWindSpeed);
                    binding.textDescriptionHome.setText(outputDescription);
                } catch (JSONException e) {
                    e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                binding.textDescriptionHome.setText(error.toString());
                grabWeatherDetails("Seattle");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
    }
}