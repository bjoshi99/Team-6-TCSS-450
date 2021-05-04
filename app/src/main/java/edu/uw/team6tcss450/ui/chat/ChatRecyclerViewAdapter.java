package edu.uw.team6tcss450.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChatCardBinding;


public class ChatRecyclerViewAdapter extends Adapter<ChatRecyclerViewAdapter.ChatViewHolder>  {
    private final List<ChatPost> chatPreviews;
    private final Map<ChatPost, Boolean> mExpandedFlags;

    public ChatRecyclerViewAdapter(List<ChatPost> items) {
        chatPreviews = items;
        mExpandedFlags = chatPreviews.stream()
                .collect(Collectors.toMap(Function.identity(), blog -> false));
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setChat(chatPreviews.get(position));
    }

    @Override
    public int getItemCount() {
        return chatPreviews.size();
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;
        private ChatPost mBlog;

        public ChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);
        }

        private void displayPreview() {
            binding.textPreview.setVisibility(View.VISIBLE);
        }

        void setChat(final ChatPost chat) {
            mBlog = chat;

            binding.textTitle.setText(chat.getPubDate());
            binding.textPubdate.setText("Friends Name");

            binding.textPreview.setText("This is the preview of the chat message");
            displayPreview();
        }
    }

}
