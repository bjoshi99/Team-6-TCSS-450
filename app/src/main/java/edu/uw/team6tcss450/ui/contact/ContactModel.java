package edu.uw.team6tcss450.ui.contact;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

public class ContactModel extends AndroidViewModel {

   private MutableLiveData<List<Contact>> mContactList;
   private MutableLiveData<List<Contact>> mSearchContact;
   private ContactRecyclerViewAdapter mViewAdapter;
   private ContactSearchRecyclerViewAdapter mSearchViewAdapter;

   public ContactModel(@NonNull Application theApplication){
        super(theApplication);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<>());
        mSearchContact = new MutableLiveData<>();
        mSearchContact.setValue(new ArrayList<>());
        mViewAdapter = new ContactRecyclerViewAdapter(mContactList.getValue());
        mSearchViewAdapter = new ContactSearchRecyclerViewAdapter(mSearchContact.getValue());

    }

    /**
     * Method that creates an observer for myContactList.
     *
     * @param theOwner
     * @param theObserver
     *
     */
    public void addContactListObserver(@NonNull LifecycleOwner theOwner,
                                       @NonNull Observer<? super List<Contact>> theObserver) {
        mContactList.observe(theOwner, theObserver);
    }

    /**
     * Method that creates an observer for myContactSearchList.
     *
     * @param theOwner
     * @param theObserver
     *
     */
    public void addContactSearchListObserver(@NonNull LifecycleOwner theOwner,
                                       @NonNull Observer<? super List<Contact>> theObserver) {
        mSearchContact.observe(theOwner, theObserver);
    }

    /**
     * Method to handle errors in the server.
     *
     * @param theError an error from the server.
     *
     */
    private void handleError(final VolleyError theError) {

//        handleResult(null);
        mContactList.getValue().clear();
        mContactList.setValue(new ArrayList<>());

        System.out.println("***********************************************************");
        System.out.println("Error : " + theError.toString());
    }

    /**
     * Get the contactList.
     *
     * @return myContactList
     *
     */
    public MutableLiveData<List<Contact>> getContactList() {
        return mContactList;
    }
    /**
     * Get the search list.
     *
     * @return mySearchList
     *
     */
    public MutableLiveData<List<Contact>> getSearchList(){ return mSearchContact; }
    /**
     * Get view adapter
     *
     * @return myViewAdapter
     *
     */
    public ContactRecyclerViewAdapter getViewAdapter() {
        return mViewAdapter;
    }


    public void addRequest(Contact ct){
//        System.out.println("******************************** \n new notification \n  " + ct);
        if(!isDuplicate(mContactList.getValue(), ct)) {
            mContactList.getValue().add(0, ct);
        }
    }

    /**
     * Method that creates dummy data for the recycler view.
     *
     * @param theResult a JSONObject to be used in the future.
     *
     */
    private void handleResult(final JSONObject theResult){

        try{
            //System.out.println(" Before clearing " + mContactList.getValue().get(0).getEmail());

            JSONArray jsonArrayContacts = theResult.getJSONArray("contacts");

            if(jsonArrayContacts.length() == 0){
                System.out.println("The messege from theResult is " + theResult.getString("message"));
            }
            //System.out.println("After clearing " +mContactList.getValue().get(0).getEmail());
            IntFunction<String> getString =
                    getApplication().getResources()::getString;

            System.out.println(jsonArrayContacts.length() + " this is the  length of json array.");

            for(int i = 0; i < jsonArrayContacts.length(); i++) {

                JSONObject jsonObjectContact = jsonArrayContacts.getJSONObject(i);

                String name = jsonObjectContact.getString("firstName")+ " " + jsonObjectContact.getString("lastName");
                String email = jsonObjectContact.getString("email");
                String nickName = jsonObjectContact.getString("userName");
                String memberId = jsonObjectContact.getString("memberId");

                Contact contact = new Contact.Builder(
                        name, nickName, email, memberId
                ).build();

                if(!isDuplicate(mContactList.getValue(), contact)){
                    Log.i("TAG", "handleResult: does the contact added to the mContactList ? " + i);
                    mContactList.getValue().add(contact);
                }

            }

            mContactList.setValue(mContactList.getValue());

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private boolean isDuplicate(List<Contact> list, Contact contact){
        for(Contact c : list){
            if((c.getEmail().equals(contact.getEmail()) && (c.req == contact.req)) ||
            c.getName().equals(contact.getName()) && (c.req == contact.req)){
                return true;
            }
        }
        return false;
    }

    /**
     * connect to endpoints using heroku app link. Can use get requests from endpoint.
     *
     * @param theJwt the jason web token to connect to
     *
     */
    public void connectGet(String theJwt) {
        String url =
                "https://tcss450-team6.herokuapp.com/contacts";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", theJwt);
                System.out.println("********************************************************");
                System.out.println("The jwt: " + theJwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public void searchMember(String theJwt, String theMember) {
        String url =
                "https://tcss450-team6.herokuapp.com/contacts/search/" + theMember;
        System.out.println("Check URL is correct ! " + url);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSearchResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", theJwt);
                System.out.println("********************************************************");
                System.out.println("The jwt: " + theJwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }


    private void handleSearchResult(final JSONObject theResult){

        try{

            IntFunction<String> getString =
                    getApplication().getResources()::getString;

            JSONArray jsonArrayContacts = theResult.getJSONArray("searchResults");

            //If search result contains data, then clear first
            if(!mSearchContact.getValue().isEmpty()){
                mSearchContact.getValue().clear();
            }

            for(int i = 0; i < jsonArrayContacts.length(); i++) {

                JSONObject jsonObjectContact = jsonArrayContacts.getJSONObject(i);

                String name = jsonObjectContact.getString("firstname")+ " " + jsonObjectContact.getString("lastname");
                String email = jsonObjectContact.getString("email");
                String nickName = jsonObjectContact.getString("username");
                //String memberId = jsonObjectContact.getString("memberId");

                Contact contact = new Contact.Builder(
                        name, nickName, email
                ).build();

                if(!isDuplicate(mSearchContact.getValue(), contact)){
                    Log.i("TAG", "handleResult: does the contact added to the mContactList ? " + i);
                    if(!isAlreadyFriend(mContactList.getValue(), contact)){
                        mSearchContact.getValue().add(contact);
                    }
                }


                mSearchContact.setValue(mSearchContact.getValue());

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

private boolean isAlreadyFriend(List<Contact> list, Contact contact){
    for(Contact c : list){
        if((c.getEmail().equals(contact.getEmail()) && (c.req == contact.req)) ||
                c.getName().equals(contact.getName()) && (c.req == contact.req)){
            return true;
        }
    }
    return false;
}





}
