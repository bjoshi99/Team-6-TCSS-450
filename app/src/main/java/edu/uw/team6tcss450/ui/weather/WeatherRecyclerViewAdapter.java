package edu.uw.team6tcss450.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import edu.uw.team6tcss450.R;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder>{
    private List<WeatherRecyclerModel> mWeatherList;

    public WeatherRecyclerViewAdapter(List<WeatherRecyclerModel> mWeatherList){
        this.mWeatherList = mWeatherList;
    }

    @NonNull
    @Override
    public WeatherRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_weather_card, parent, false);
        return new WeatherRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerViewAdapter.ViewHolder holder, int position) {
        String time = mWeatherList.get(position).getTime();
        String temp = mWeatherList.get(position).getTemp();

        holder.setData(time, temp);
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView time;
        private TextView temp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textView_hour1);
            temp = itemView.findViewById(R.id.textView_temp1);
        }

        public void setData( String time, String temp) {
            this.time.setText(time);
            this.time.setText(temp);
        }
    }
}