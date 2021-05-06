package edu.uw.team6tcss450.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import edu.uw.team6tcss450.R;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder>{
    private List<WeatherModel> mWeatherList;

    public WeatherRecyclerViewAdapter(List<WeatherModel> mWeatherList){
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
        String dayOfWeek = mWeatherList.get(position).getDayOfWeek();
        int image = mWeatherList.get(position).getImage();
        String tempHigh = mWeatherList.get(position).getTempHigh();
        String tempLow = mWeatherList.get(position).getTempLow();

        holder.setData(dayOfWeek, image, tempHigh, tempLow);
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mDayOfWeek;
        private ImageView mImage;
        private TextView mTempHigh;
        private TextView mTempLow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDayOfWeek = itemView.findViewById(R.id.text_DayOfWeek);
            mImage = itemView.findViewById(R.id.image_WeatherIcon);
            mTempHigh = itemView.findViewById(R.id.text_TempHigh);
            mTempLow = itemView.findViewById(R.id.text_TempLow);
        }

        public void setData(String mDayOfWeek, int mImage, String mTempHigh, String mTempLow) {
            this.mDayOfWeek.setText(mDayOfWeek);
            this.mImage.setImageResource(mImage);
            this.mTempHigh.setText(mTempHigh);
            this.mTempLow.setText(mTempLow);
        }
    }
}
