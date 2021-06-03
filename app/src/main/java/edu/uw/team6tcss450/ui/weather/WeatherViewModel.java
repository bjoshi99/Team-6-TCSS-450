package edu.uw.team6tcss450.ui.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import edu.uw.team6tcss450.model.LocationViewModel;

public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<String> zip;
    private MutableLiveData<String> latLon;
    private MutableLiveData<Boolean> whateverYouWant;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        zip = new MutableLiveData<>();
        zip.setValue("98105");
        latLon = new MutableLiveData<>();
        latLon.setValue("(47.61,-122.33)");
        whateverYouWant = new MutableLiveData<>();
        whateverYouWant.setValue(false);
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
        zip.observe(owner, observer);
        latLon.observe(owner, observer);
    }

    public String getZip(){
        return zip.getValue();
    }

    public void setZip(String c){
        zip.setValue(c);
    }

    public String getLatLon(){
        return latLon.getValue();
    }

    public void setLatLon(String c){
        latLon.setValue(c);
    }

    public boolean getWhatever(){
        return whateverYouWant.getValue();
    }

    public void setWhatever(boolean w){
        whateverYouWant.setValue(w);
    }
}
