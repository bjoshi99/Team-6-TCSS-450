package edu.uw.team6tcss450.ui.chat.chatSettings;

import android.database.DataSetObserver;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChatListBinding;
import edu.uw.team6tcss450.databinding.FragmentChatSettingsBinding;
import edu.uw.team6tcss450.model.UserInfoViewModel;
import edu.uw.team6tcss450.ui.chat.ChatFragmentArgs;
import edu.uw.team6tcss450.ui.chat.chatList.ChatListFragmentDirections;
import edu.uw.team6tcss450.ui.chat.chatList.ChatListViewModel;
import edu.uw.team6tcss450.ui.contact.Contact;
import edu.uw.team6tcss450.ui.contact.ContactModel;

public class ChatSettingsFragment extends Fragment {
    private ContactModel mModel;
    private UserInfoViewModel mUserModel;

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
        mModel.connectGet(mUserModel.getmJwt());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatSettingsBinding binding = FragmentChatSettingsBinding.bind(getView());
        ChatSettingsFragmentArgs args = ChatSettingsFragmentArgs.fromBundle(getArguments());
        binding.chatRoomName.setText(args.getChatName());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            System.out.println(contactList);

            ArrayList<String> contacts = new ArrayList<>();

            for (int i = 0; i < contactList.size(); i++) {
                contacts.add(contactList.get(i).getEmail());
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, contacts);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinner.setAdapter(adapter);

        });
    }
}
