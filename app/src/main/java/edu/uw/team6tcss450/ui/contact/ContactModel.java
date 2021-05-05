package edu.uw.team6tcss450.ui.contact;

public class ContactModel {

    private int mImage;
    private String mName;
    private String mCurrTime;
    private String mCurrChat;
    private String mDivider;

    ContactModel(int mImage, String mName, String mCurrTime,  String mCurrChat, String mDivider){
        this.mImage = mImage;
        this.mName = mName;
        this.mCurrTime = mCurrTime;
        this.mCurrChat = mCurrChat;
        this.mDivider = mDivider;

    }

    public int getmImage() {
        return mImage;
    }

    public String getmName() {
        return mName;
    }

    public String getmCurrTime() {
        return mCurrTime;
    }

    public String getmCurrChat() {
        return mCurrChat;
    }

    public String getmDivider() {
        return mDivider;
    }
}
