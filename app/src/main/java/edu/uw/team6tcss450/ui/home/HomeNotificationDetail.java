package edu.uw.team6tcss450.ui.home;

public class HomeNotificationDetail {

    public String getmMsgFrom() {
        return mMsgFrom;
    }

    public String getmInChat() {
        return mID;
    }

    public String getmMsg() {
        return mMsg;
    }

    public String getmMsgTime() {
        return mMsgTime;
    }

    private String mMsgFrom, mID, mMsg;
    private String mMsgTime;

    HomeNotificationDetail(String msg, String from, String chatId, String time){
        this.mMsg = msg;
        this.mID = chatId;
        this.mMsgFrom = from;
        this.mMsgTime = time;
    }


}
