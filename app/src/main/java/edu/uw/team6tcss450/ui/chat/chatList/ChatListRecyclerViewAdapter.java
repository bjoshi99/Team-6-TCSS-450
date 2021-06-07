package edu.uw.team6tcss450.ui.chat.chatList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team6tcss450.MainActivity;
import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChatRoomBinding;
import edu.uw.team6tcss450.model.NewMessageCountViewModel;

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private final List<ChatRoom> mChatRoom;
    private NewMessageCountViewModel messageModel;
    public ChatListRecyclerViewAdapter(List<ChatRoom> List){
        this.mChatRoom = List;
    }
    private Context mContext;

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup theParent, int theViewType) {
        messageModel = new ViewModelProvider((MainActivity) (theParent.getContext())).get(NewMessageCountViewModel.class);
        mContext = theParent.getContext();

        return new ChatListViewHolder(LayoutInflater
                .from(theParent.getContext())
                .inflate(R.layout.fragment_chat_room, theParent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListRecyclerViewAdapter.ChatListViewHolder theHolder, int thePosition) {
        theHolder.setmChatRoom(mChatRoom.get(thePosition));

        if(messageModel.doesContainMessage(mChatRoom.get(thePosition).getChatID())){
            int icon = R.drawable.ic_baseline_mark_chat_unread_24;
            theHolder.mBinding.editUserName.setTypeface(null, Typeface.BOLD_ITALIC);

//            theHolder.mBinding.editUserName.setCompoundDrawables(null, null, mContext.getDrawable(icon), null);
        }
    }


    @Override
    public int getItemCount() {
        return mChatRoom.size();
    }


    public class ChatListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public FragmentChatRoomBinding mBinding;
        private ChatRoom mChatRoom;
        private NewMessageCountViewModel mNewMessageModel;

        public ChatListViewHolder(View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentChatRoomBinding.bind(theView);

            mNewMessageModel = new ViewModelProvider((MainActivity)(mView.getContext())).get(NewMessageCountViewModel.class);
            //theView.setOnClickListener(this);

//            mView.setOnClickListener(v -> Navigation.findNavController(mView).navigate(
//                    ChatListFragmentDirections.actionChatListFragmentToChatFragment(mChatRoom.getChatID())
//
//            ));

            //new update
            mView.setOnClickListener(this::onClick);

            mBinding.buttonChatSettings.setOnClickListener(v -> Navigation.findNavController(mView).navigate(
                    ChatListFragmentDirections.actionChatListFragmentToChatSettingsFragment(mChatRoom.getChatID(), mChatRoom.getName())
            ));
        }

        @Override
        public void onClick(View theView) {

            System.out.println("on click to navigate to another fragment");

            //adjust the badge numbers
            mNewMessageModel.decrease(mChatRoom.getChatID());

            //remove the backgroud as it is now viewed
            mBinding.editUserName.setCompoundDrawables(null,null,null,null);

            mBinding.editUserName.setTypeface(null, Typeface.NORMAL);

            Navigation.findNavController(mView).navigate(
                    ChatListFragmentDirections.actionChatListFragmentToChatFragment(mChatRoom.getChatID()));
        }

        void setmChatRoom(ChatRoom theChatRoom) {
            mChatRoom = theChatRoom;
            mBinding.editUserName.setText(theChatRoom.getName());
        }
    }

    public void deleteChatRoom(int chatID){

        for(int i=0; i<mChatRoom.size(); i++){

            if(mChatRoom.get(i).getChatID() == chatID){
                mChatRoom.remove(i);
            }
        }

    }
}
