package edu.uw.team6tcss450.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.team6tcss450.MainActivity;
import edu.uw.team6tcss450.R;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{
    private List<String> mNotificationList;
//    NavController mController;

    private HomeViewModel mHomeModel;

    public HomeRecyclerViewAdapter(List<String> notificationList, HomeViewModel model){
        this.mNotificationList = notificationList;


        mHomeModel = model;
//        mController = ctr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notification_card, parent, false);
        return new HomeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.ViewHolder holder, int position) {
//        String msg = mNotificationList.get(position).getmMsg();
//        String time = mNotificationList.get(position).getmMsgTime();
//        String from = mNotificationList.get(position).getmMsgFrom();
//        String chatid = mNotificationList.get(position).getmInChat();

        String msg = mNotificationList.get(position);

        holder.setData(msg);

//        this.notifyDataSetChanged();
//        holder.setData(msg, time, from, chatid);
    }

    public void deleteAll(){
        mNotificationList.clear();
        this.notifyDataSetChanged();
    }

    public void addNotification(String notification){
        System.out.println("******************************** \n new notification \n  ");

        mNotificationList.add(0, notification);
        this.notifyDataSetChanged();
    }


    public void deleteContactNotifications(){
        int size = mNotificationList.size();
        for(int i=0; i<mNotificationList.size(); i++){
            if((mNotificationList.get(i)).contains("contact")){
                mNotificationList.remove(i);
                i--;
            }
        }
        this.notifyDataSetChanged();
    }

    public void deleteChatNotification(){
        System.out.println("Deleting all chat notification");
        int size = mNotificationList.size();
        for(int i=0; i<mNotificationList.size(); i++){
            if((mNotificationList.get(i)).contains("message")){
                mNotificationList.remove(i);
                i--;
            }
        }
        this.notifyDataSetChanged();

        // 1 3 3 3 5 6 //6, 0
        // 3 3 4 5 6 //5, 1
        // 3 4 5 6 //4, 2
        // 2 4 5 6
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mMsg;
        private View mView;
//        private TextView mTime;
//        private TextView mMsgFrom;
//        private TextView mInChatID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mMsg = itemView.findViewById(R.id.text_message);
//            mTime = itemView.findViewById(R.id.text_time);
//            mMsgFrom = itemView.findViewById(R.id.text_from);
//            mInChatID = itemView.findViewById(R.id.text_TempLow);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Been clicked and now navigating to contact fragment");
//                    mController.navigate(R.id.navigation_contact);
                    String msg = mMsg.getText().toString();

                    //delete the clicked card from the recycler view
                    mHomeModel.delete(msg);

                    if(msg.contains("request"))
                        Navigation.findNavController(mView).navigate(R.id.navigation_contact);

                    if(msg.contains("message"))
                        Navigation.findNavController(mView).navigate(R.id.chat_list_fragment);



                }
            });
        }

        public void setData(String msg) {

            if(msg.contains("message"))
                mView.findViewById(R.id.card_root_home).setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.messageNotificationColor));

            if(msg.contains("request"))
                mView.findViewById(R.id.card_root_home).setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.contactReqNotificationColor));

            this.mMsg.setText(msg);

//            HomeRecyclerViewAdapter.this.notifyDataSetChanged();
//                        this.mMsg.setText(msg);
//            this.mTime.setText(time);
//            this.mMsgFrom.setText(from);
//            this.mInChatID.setText(chatID);
        }
    }
}
