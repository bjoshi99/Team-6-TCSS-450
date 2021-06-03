package edu.uw.team6tcss450.ui.chat.chatList;

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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.team6tcss450.R;

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
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
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

    private void handleResult(final JSONObject result) {
        try {
            IntFunction<String> getString = getApplication().getResources()::getString;
            JSONArray jsonArrayChatRooms = result.getJSONArray("chats");
            for (int i = 0; i < jsonArrayChatRooms.length(); i++) {
                JSONObject jsonChatRoom = jsonArrayChatRooms.getJSONObject(i);

                int chatID = jsonChatRoom.getInt("chat");
                String chatName = jsonChatRoom.getString("name");

            ChatRoom chat = new ChatRoom.Builder(
                    chatID,
                    chatName
            ).build();
                if(!isDuplicate(mChatList.getValue(), chat)){
                    Log.i("TAG", "handleResult: does the contact added to the mContactList ? " + i);
                    mChatList.getValue().add(chat);
                }
        }
            mChatList.setValue(mChatList.getValue());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

    }

    private boolean isDuplicate(List<ChatRoom> list, ChatRoom chat){
        for(ChatRoom c : list){
            if(c.getChatID() == chat.getChatID()){
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
                "https://tcss450-team6.herokuapp.com/contacts/listofchat";
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
