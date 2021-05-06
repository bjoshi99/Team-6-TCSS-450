package edu.uw.team6tcss450.ui.home;

public class HomeModel {

    public String getmMsgFrom() {
        return mMsgFrom;
    }

    public String getmInChat() {
        return mInChatID;
    }

    public String getmMsg() {
        return mMsg;
    }

    public String getmMsgTime() {
        return mMsgTime;
    }

    private String mMsgFrom, mInChatID, mMsg;
    private String mMsgTime;

    HomeModel(String msg, String from, String chatId, String time){
        this.mMsg = msg;
        this.mInChatID = chatId;
        this.mMsgFrom = from;
        this.mMsgTime = time;
    }


}
