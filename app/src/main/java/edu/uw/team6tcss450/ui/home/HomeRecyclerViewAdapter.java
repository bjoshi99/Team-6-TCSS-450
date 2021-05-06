package edu.uw.team6tcss450.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team6tcss450.R;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{
    private List<HomeModel> mNotificationList;

    public HomeRecyclerViewAdapter(List<HomeModel> notificationList){
        this.mNotificationList = notificationList;
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notification_card, parent, false);
        return new HomeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        String msg = mNotificationList.get(position).getmMsg();
        String time = mNotificationList.get(position).getmMsgTime();
        String from = mNotificationList.get(position).getmMsgFrom();
        String chatid = mNotificationList.get(position).getmInChat();

        holder.setData(msg, time, from, chatid);
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mMsg;
        private TextView mTime;
        private TextView mMsgFrom;
//        private TextView mInChatID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMsg = itemView.findViewById(R.id.text_message);
            mTime = itemView.findViewById(R.id.text_time);
            mMsgFrom = itemView.findViewById(R.id.text_from);
//            mInChatID = itemView.findViewById(R.id.text_TempLow);
        }

        public void setData(String msg, String time, String from, String chatID) {
            this.mMsg.setText(msg);
            this.mTime.setText(time);
            this.mMsgFrom.setText(from);
//            this.mInChatID.setText(chatID);
        }
    }
}
