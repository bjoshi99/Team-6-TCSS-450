package edu.uw.team6tcss450.ui.home;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.ui.contact.Contact;
import edu.uw.team6tcss450.ui.contact.ContactRecyclerViewAdapter;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<String>> mNotificationList;
    private HomeRecyclerViewAdapter mViewAdapter;

    public HomeViewModel(@NonNull Application theApplication){
        super(theApplication);
        mNotificationList = new MutableLiveData<>();
        mNotificationList.setValue(new ArrayList<>());

        mViewAdapter = new HomeRecyclerViewAdapter(mNotificationList.getValue(), this);

    }

    /**
     * Method that creates an observer for myContactList.
     *
     * @param theOwner
     * @param theObserver
     *
     */
    public void addNotificationListObserver(@NonNull LifecycleOwner theOwner,
                                       @NonNull Observer<? super List<String>> theObserver) {
        mNotificationList.observe(theOwner, theObserver);
    }

    /**
     * Get the contactList.
     *
     * @return myContactList
     *
     */
    public MutableLiveData<List<String>> getContactList() {
        return mNotificationList;
    }

    /**
     * Get view adapter
     *
     * @return myViewAdapter
     *
     */
    public HomeRecyclerViewAdapter getViewAdapter() {
        return mViewAdapter;
    }

    public void addNotification(String notification){
//        System.out.println("******************************** \n new notification \n  " + notification);
        mNotificationList.getValue().add(notification);

        mViewAdapter.notifyDataSetChanged();
    }

    public void delete(String msg){
        for(String s : mNotificationList.getValue()){
            if(msg.equals(s)){
                mNotificationList.getValue().remove(msg);
            }
        }
    }

}
