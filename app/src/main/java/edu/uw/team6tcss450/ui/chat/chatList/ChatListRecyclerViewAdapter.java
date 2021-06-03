package edu.uw.team6tcss450.ui.chat.chatList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChatRoomBinding;

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private final List<ChatRoom> mChatRoom;

    public ChatListRecyclerViewAdapter(List<ChatRoom> List){
        this.mChatRoom = List;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup theParent, int theViewType) {
        return new ChatListViewHolder(LayoutInflater
                .from(theParent.getContext())
                .inflate(R.layout.fragment_chat_room, theParent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListRecyclerViewAdapter.ChatListViewHolder theHolder, int thePosition) {
        theHolder.setmChatRoom(mChatRoom.get(thePosition));
    }


    @Override
    public int getItemCount() {
        return mChatRoom.size();
    }


    public class ChatListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public FragmentChatRoomBinding mBinding;
        private ChatRoom mChatRoom;

        public ChatListViewHolder(View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentChatRoomBinding.bind(theView);
            //theView.setOnClickListener(this);

            mView.setOnClickListener(v -> Navigation.findNavController(mView).navigate(
                    ChatListFragmentDirections.actionChatListFragmentToChatFragment(mChatRoom.getChatID())
            ));
        }

        @Override
        public void onClick(View theView) {

        }

        void setmChatRoom(ChatRoom theChatRoom) {
            mChatRoom = theChatRoom;
            mBinding.editUserName.setText(theChatRoom.getName());
        }
    }
}
