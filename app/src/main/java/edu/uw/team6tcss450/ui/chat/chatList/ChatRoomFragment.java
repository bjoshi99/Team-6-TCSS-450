package edu.uw.team6tcss450.ui.chat.chatList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uw.team6tcss450.databinding.FragmentChatRoomBinding;
import edu.uw.team6tcss450.databinding.FragmentContactCardBinding;

public class ChatRoomFragment extends Fragment {

    public FragmentChatRoomBinding mBinding;

    public ChatRoomFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatRoomBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);
    }
}
