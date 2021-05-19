//package edu.uw.team6tcss450.ui.chat;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import edu.uw.team6tcss450.R;
//import edu.uw.team6tcss450.databinding.FragmentChatListBinding;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class ChatListFragment extends Fragment {
//    private ChatListViewModel mModel;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_chat_list, container, false);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
//        mModel.connectGet();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());
//
//        mModel.addBlogListObserver(getViewLifecycleOwner(), blogList -> {
//            if(!blogList.isEmpty()) {
//                binding.listRoot.setAdapter(
//                        new ChatRecyclerViewAdapter(blogList)
//                );
//                binding.layoutWait.setVisibility(View.GONE);
//            }
//        });
//    }
//}