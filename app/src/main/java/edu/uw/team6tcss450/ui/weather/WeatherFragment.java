package edu.uw.team6tcss450.ui.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.uw.team6tcss450.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    List<WeatherModel> weatherList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fill();

        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView_Weather);

        WeatherRecyclerViewAdapter listAdapter = new WeatherRecyclerViewAdapter(weatherList);
        mRecyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void fill() {
        weatherList = new ArrayList<>();

        weatherList.add(new WeatherModel("Wednesday", R.drawable.ic_cloud_black_24dp, "69", "48"));
        weatherList.add(new WeatherModel("Thursday", R.drawable.ic_cloud_black_24dp, "65", "47"));
        weatherList.add(new WeatherModel("Friday", R.drawable.ic_baseline_wb_sunny_24, "72", "50"));
        weatherList.add(new WeatherModel("Saturday", R.drawable.ic_baseline_wb_sunny_24, "73", "51"));
        weatherList.add(new WeatherModel("Sunday", R.drawable.ic_cloud_black_24dp, "67", "45"));
    }
}