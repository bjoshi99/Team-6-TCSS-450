package edu.uw.team6tcss450.ui.chat.chatList;

import java.io.Serializable;

public class ChatRoom implements Serializable {

    private String mName;
    private int mID;

    public static class Builder {
        private String mName;
        private int mID;

        public Builder(int theID, String theName) {
            this.mID = theID;
            this.mName = theName;
        }

        public Builder addName(final String theVal) {
            mName = mName + " " + theVal;
            return this;
        }

        public ChatRoom build() {
            return new ChatRoom(this);
        }
    }

    public ChatRoom(final Builder builder)  {
        this.mID = builder.mID;
        this.mName = builder.mName;

    }

    public String getName() {
        return mName;
    }
    public int getChatID() {
        return mID;
    }

}
