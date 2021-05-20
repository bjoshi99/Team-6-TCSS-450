package edu.uw.team6tcss450.ui.chat.chatList;

import android.app.Application;

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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<ChatRoom>> mChatList;
    private ChatListRecyclerViewAdapter mViewAdapter;

    public ChatListViewModel(@NonNull Application theApplication){
        super(theApplication);
        mChatList = new MutableLiveData<>();
        mChatList.setValue(new ArrayList<>());
        mViewAdapter = new ChatListRecyclerViewAdapter(mChatList.getValue());

    }

    /**
     * Method that creates an observer for myContactList.
     *
     * @param theOwner
     * @param theObserver
     *
     */
    public void addChatListObserver(@NonNull LifecycleOwner theOwner,
                                       @NonNull Observer<? super List<ChatRoom>> theObserver) {
        mChatList.observe(theOwner, theObserver);
    }

    /**
     * Method to handle errors in the server.
     *
     * @param theError an error from the server.
     *
     */
    private void handleError(final VolleyError theError) {

        handleResult(null);
    }

    /**
     * Get the contactList.
     *
     * @return myContactList
     *
     */
    public MutableLiveData<List<ChatRoom>> getChatList() {
        return mChatList;
    }

    /**
     * Get view adapter
     *
     * @return myViewAdapter
     *
     */
    public ChatListRecyclerViewAdapter getViewAdapter() {
        return mViewAdapter;
    }


    /**
     * Method that creates dummy data for the recycler view.
     *
     * @param theResult a JSONObject to be used in the future.
     *
     */
    private void handleResult(final JSONObject theResult) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;

        for(int i = 0; i < 6; i++) {
            ChatRoom chatRoom = new ChatRoom.Builder(
                    "Chat Room 1")
                    .build();
            if (!mChatList.getValue().contains(chatRoom)) {
                mChatList.getValue().add(chatRoom);
            }
        }
        mChatList.setValue(mChatList.getValue());
    }

    /**
     * connect to endpoints using heroku app link. Can use get requests from endpoint.
     *
     * @param theJwt the jason web token to connect to
     *
     */
    public void connectGet(String theJwt) {
        String url =
                "https://tcss450-team6.herokuapp.com/auth";
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

}
