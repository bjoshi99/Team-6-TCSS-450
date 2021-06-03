package edu.uw.team6tcss450.ui.chat.chatSettings;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChatListBinding;
import edu.uw.team6tcss450.databinding.FragmentChatSettingsBinding;
import edu.uw.team6tcss450.ui.chat.ChatFragmentArgs;

public class ChatSettingsFragment extends Fragment {

    public ChatSettingsFragment(){
        //Required empty public constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatSettingsBinding binding = FragmentChatSettingsBinding.bind(getView());
        ChatSettingsFragmentArgs args = ChatSettingsFragmentArgs.fromBundle(getArguments());
        binding.chatRoomName.setText(args.getChatName());

        ArrayList<String> contacts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            contacts.add(new String("Fake User " + i));
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity().getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, contacts);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        binding.spinner.setAdapter(adapter);
    }
}
