package edu.uw.team6tcss450.ui.chat.chatSettings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChatListBinding;
import edu.uw.team6tcss450.databinding.FragmentChatSettingsBinding;
import edu.uw.team6tcss450.io.RequestQueueSingleton;

import edu.uw.team6tcss450.model.UserInfoViewModel;
import edu.uw.team6tcss450.ui.auth.signin.SignInFragmentArgs;
import edu.uw.team6tcss450.ui.chat.ChatFragmentArgs;
import edu.uw.team6tcss450.ui.chat.chatList.ChatListFragmentDirections;
import edu.uw.team6tcss450.ui.chat.chatList.ChatListRecyclerViewAdapter;
import edu.uw.team6tcss450.ui.chat.chatList.ChatListViewModel;
import edu.uw.team6tcss450.ui.chat.chatList.ChatRoom;
import edu.uw.team6tcss450.ui.contact.Contact;
import edu.uw.team6tcss450.ui.contact.ContactModel;

public class ChatSettingsFragment extends Fragment {
    private ContactModel mModel;
    private UserInfoViewModel mUserModel;
    private View mView;
    private ChatListViewModel mChatListMode;

    private int currentChatID;

    public ChatSettingsFragment(){
        //Required empty public constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_settings, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());

        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ContactModel.class);
        mChatListMode = provider.get(ChatListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;

        FragmentChatSettingsBinding binding = FragmentChatSettingsBinding.bind(getView());
        ChatSettingsFragmentArgs args = ChatSettingsFragmentArgs.fromBundle(getArguments());
        binding.chatRoomName.setText(args.getChatName());

        currentChatID = args.getChatID();
        binding.leaveButton.setOnClickListener(this::onClickLeave);

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            System.out.println(contactList);

            ArrayList<String> contacts = new ArrayList<>();

            for (int i = 0; i < contactList.size(); i++) {
                contacts.add(contactList.get(i).getEmail());
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, contacts);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinner.setAdapter(adapter);

            binding.addButton.setOnClickListener(button -> {
                int memberId = contactList.get(binding.spinner.getSelectedItemPosition()).memberId;

                System.out.println(memberId);

                String url = getActivity().getApplication().getResources().getString(R.string.base_url) +
                        "chats/user/" + args.getChatID() + "/" +memberId;



                Request request = new JsonObjectRequest(
                        Request.Method.PUT,
                        url,
                        null, //push token found in the JSONObject body
                        null, // we get a response but do nothing with it
                        this::handleError) {

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        // add headers <key,value>
                        headers.put("Authorization", mUserModel.getmJwt());
                        return headers;
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(
                        10_000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Instantiate the RequestQueue and add the request to the queue
                RequestQueueSingleton.getInstance(getActivity().getApplication().getApplicationContext())
                        .addToRequestQueue(request);
            });

            binding.removeButton.setOnClickListener(button -> {
                String email = contactList.get(binding.spinner.getSelectedItemPosition()).getEmail();

                System.out.println(email);

                String url = getActivity().getApplication().getResources().getString(R.string.base_url) +
                        "chats/" + args.getChatID() + "/" +email;



                Request request = new JsonObjectRequest(
                        Request.Method.DELETE,
                        url,
                        null, //push token found in the JSONObject body
                        null, // we get a response but do nothing with it
                        this::handleError) {

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        // add headers <key,value>
                        headers.put("Authorization", mUserModel.getmJwt());
                        return headers;
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(
                        10_000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Instantiate the RequestQueue and add the request to the queue
                RequestQueueSingleton.getInstance(getActivity().getApplication().getApplicationContext())
                        .addToRequestQueue(request);
            });

        });

    }

    private void onClickLeave(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Setting message manually and performing action on button click
        builder.setMessage("You are about to leave this Chat Room!!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String email = mUserModel.getEmail();
                        deleteUser(email);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Cancel' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Are You Sure ?");
        alert.show();
    }

    private void deleteUser(String theEmail){
        System.out.println("Inside the delete end point");
        String url = mView.getContext().getResources().getString(R.string.base_url) +
                "chats/" + currentChatID + "/" + theEmail;

        String jwt = mUserModel.getmJwt();

        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                this::handleSuccess,
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(mView.getContext().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleSuccess(JSONObject jsonObject) {

        System.out.println("Room got deleted");

        mChatListMode.getViewAdapter().deleteChatRoom(currentChatID);
        mChatListMode.getViewAdapter().notifyDataSetChanged();

        //navigate to list of chats
        Navigation.findNavController(mView).navigate(R.id.chat_list_fragment);
    }

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

}
