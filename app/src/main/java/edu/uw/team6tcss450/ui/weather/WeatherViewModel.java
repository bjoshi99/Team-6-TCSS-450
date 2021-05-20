package edu.uw.team6tcss450.ui.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.json.JSONObject;

public class WeatherViewModel extends AndroidViewModel {
//    private MutableLiveData<JSONObject> mWeather;

    private MutableLiveData<String> city;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        city = new MutableLiveData<>();
        city.setValue("Seattle");
//        mWeather = new MutableLiveData<>();
//        mWeather.setValue(new JSONObject());
    }

    /**
     * Add an observer to this live data. This is a pass through method for
     * this classes MutableLiveData field of count.
     *
     * See LiveData.observe for more implementation details.
     * @param owner the LifecycleOwner which controls the observer
     * @param observer the observer that will receive the events
     */
    public void addCountObserver(@NonNull LifecycleOwner owner,
                                 @NonNull Observer<? super String> observer) {
        city.observe(owner, observer);
    }

    public String getCity(){
        return city.getValue();
    }

    public void setValue(String c){
        city.setValue(c);
    }
}
