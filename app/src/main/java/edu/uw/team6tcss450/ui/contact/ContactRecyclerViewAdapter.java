package edu.uw.team6tcss450.ui.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team6tcss450.R;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder> {

    private List<ContactModel> mUserList;

    public ContactRecyclerViewAdapter(List<ContactModel> mUserList){
        this.mUserList = mUserList;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact_card, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.ViewHolder holder, int position) {
        int image = mUserList.get(position).getmImage();
        String name = mUserList.get(position).getmName();
        String time = mUserList.get(position).getmCurrTime();
        String chat = mUserList.get(position).getmCurrChat();
        String newLine = mUserList.get(position).getmDivider();

        holder.setData(image, name, time, chat, newLine);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImage;
        private TextView mName;
        private TextView mCurrTime;
        private TextView mCurrChat;
        private TextView mDivider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.picture_cat1);
            mName = itemView.findViewById(R.id.edit_cat1name);
            mCurrTime = itemView.findViewById(R.id.edit_current_time);
            mCurrChat = itemView.findViewById(R.id.edit_current_chat);
            mDivider = itemView.findViewById(R.id.edit_breakline);
        }

        public void setData(int image, String name, String time, String chat, String newLine) {
            mImage.setImageResource(image);
            mName.setText(name);
            mCurrTime.setText(time);
            mCurrChat.setText(chat);
            mDivider.setText(newLine);
        }
    }
}
