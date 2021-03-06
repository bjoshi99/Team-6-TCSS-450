package edu.uw.team6tcss450.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import edu.uw.team6tcss450.ui.contact.ContactModel;
import edu.uw.team6tcss450.ui.contact.ContactRecyclerViewAdapter;
import edu.uw.team6tcss450.ui.weather.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ArrayList<String> notifcationList = new ArrayList<>();
    private FragmentHomeBinding binding;
    private WeatherViewModel modelWeather;
    private HomeViewModel mHomeModel;
    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        makeUp();

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView_Home);

//        mHomeModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
//
//        HomeRecyclerViewAdapter listAdapter = new HomeRecyclerViewAdapter(notifcationList, mHomeModel);
//        mRecyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        binding = FragmentHomeBinding.inflate(inflater);
//        return binding.getRoot();


        return view;

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mView = view;

        modelWeather = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        grabWeatherDetails(modelWeather.getZip());

        mHomeModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        ImageView img = ((ImageView) ((getView().findViewById(R.id.button_clear))));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeModel.deleteAll();

                //notify changes
                mHomeModel.mViewAdapter.notifyDataSetChanged();
            }
        });

        grabDate();
        binding.datehome.setText(grabDate());
        ((TextView)(getView().findViewById(R.id.datehome))).setText(grabDate());

        //for now, email is displayed on home but for future, we can change it to username
        FragmentHomeBinding.bind(getView()).texttile.setText("Welcome " + model.getEmail() + " !");

        //Listener for the contacts recycler view adapter.
        mHomeModel.addNotificationListObserver(getViewLifecycleOwner(), notificationList -> {
            if (!notificationList.isEmpty()) {
//                binding.RecyclerViewHome.setAdapter(
//                        new HomeRecyclerViewAdapter(notificationList)
//                );
                RecyclerView rv = (RecyclerView)(getView().findViewById(R.id.RecyclerView_Home));

                //to pass navigation controller to view holder
                NavController cntrl = Navigation.findNavController(getActivity(),
                        R.id.nav_host_fragment);

                (rv).setAdapter(
//                        new HomeRecyclerViewAdapter(notificationList, mHomeModel)
                        mHomeModel.getViewAdapter()
                );
                rv.getAdapter().notifyDataSetChanged();
                mHomeModel.mViewAdapter.notifyDataSetChanged();
                rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
            }
        });
    }

    private void makeUp() {
        notifcationList = new ArrayList<>();
        notifcationList.add("New message request from unknown");
//        notifcationList.add(new HomeNotificationDetail("Updated layout.", "Alexis" , "123", "9:04 pm"));
//        notifcationList.add(new HomeNotificationDetail("Good night!", "Jun" , "123", "11:15 pm"));
//        notifcationList.add(new HomeNotificationDetail("Just found this bug!", "Rj" , "123", "6:48 pm"));
//        notifcationList.add(new HomeNotificationDetail("RJ found a bug, need to fix asap.", "Bhavesh" , "123", "7:55 pm"));
//        notifcationList.add(new HomeNotificationDetail("Upcoming meeting: 9:30 am.", "Charles" , "123", "10:00 pm"));
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

                    String outputTemp = df.format(temp) + "?? F";
                    String outputCityName = "Currently in " + jsonResponse.getString("name");
                    String outputMinMax = "Min/Max: " + df.format(min) + "?? / " + df.format(max) + "?? F";
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

//                    binding.textTempHome.setText(outputTemp);
//                    binding.textCityHome.setText(outputCityName);
//                    binding.textMinmaxHome.setText(outputMinMax);
//                    binding.textHumidityHome.setText(outputHumidity);
//                    binding.textWindHome.setText(outputWindSpeed);
//                    binding.textDescriptionHome.setText(outputDescription);

                    //without using binding objects
//                    ((TextView)(getView().findViewById(R.id.text_temp_home))).setText(outputTemp);
//                    ((TextView)(getView().findViewById(R.id.text_city_home))).setText(outputCityName);
//                    ((TextView)(getView().findViewById(R.id.text_minmax_home))).setText(outputMinMax);
//                    ((TextView)(getView().findViewById(R.id.text_humidity_home))).setText(outputHumidity);
//                    ((TextView)(getView().findViewById(R.id.text_wind_home))).setText(outputWindSpeed);
//                    ((TextView)(getView().findViewById(R.id.text_description_home))).setText(outputDescription);

                    //with new view object
                    ((TextView)(mView.findViewById(R.id.text_temp_home))).setText(outputTemp);
                    ((TextView)(mView.findViewById(R.id.text_city_home))).setText(outputCityName);
                    ((TextView)(mView.findViewById(R.id.text_minmax_home))).setText(outputMinMax);
                    ((TextView)(mView.findViewById(R.id.text_humidity_home))).setText(outputHumidity);
                    ((TextView)(mView.findViewById(R.id.text_wind_home))).setText(outputWindSpeed);
                    ((TextView)(mView.findViewById(R.id.text_description_home))).setText(outputDescription);

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