package edu.uw.team6tcss450.ui.chat.chatList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONObject;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChatListBinding;
import edu.uw.team6tcss450.databinding.FragmentCreateChatBinding;
import edu.uw.team6tcss450.model.UserInfoViewModel;
import edu.uw.team6tcss450.ui.chat.chatList.ChatListFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {
    private ChatListViewModel mModel;

    public ChatListFragment(){
        //Required empty public constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
        mModel.connectGet(model.getmJwt());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());

        mModel.addChatListObserver(getViewLifecycleOwner(), chatRoomList -> {
            if (!chatRoomList.isEmpty()) {
                binding.recyclerviewChat.setAdapter(
                        mModel.getViewAdapter()
                );

            }
        });



        binding.chatListFragment.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    ChatListFragmentDirections.actionChatListFragmentToCreateChatFragment()
            );
        });

//        mModel.addBlogListObserver(getViewLifecycleOwner(), blogList -> {
//            if(!blogList.isEmpty()) {
//                binding.listRoot.setAdapter(
//                        new ChatRecyclerViewAdapter(blogList)
//                );
//                binding.layoutWait.setVisibility(View.GONE);
//            }
//        });
    }
}