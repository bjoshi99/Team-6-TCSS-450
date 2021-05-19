package edu.uw.team6tcss450.ui.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

public class WeatherViewModel extends AndroidViewModel {
    private MutableLiveData<JSONObject> mWeather;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeather = new MutableLiveData<>();
        mWeather.setValue(new JSONObject());
    }
}
