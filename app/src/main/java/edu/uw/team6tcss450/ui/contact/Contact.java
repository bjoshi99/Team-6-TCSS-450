package edu.uw.team6tcss450.ui.contact;

import java.io.Serializable;

public class Contact implements Serializable {

    private String mName;
    private String mNickname;
    private String mEmail;
    public boolean req = false;
    public int memberId = 0;

    public static class Builder {
        private String mName;
        private String mNickname;
        private String mEmail;


        public Builder(String theName, String theNickname, String theEmail) {
            this.mName = theName;
            this.mNickname = theNickname;
            this.mEmail = theEmail;
        }


        public Builder addName(final String theVal) {
            mName = mName + " " + theVal;
            return this;
        }

        public Builder addNickname(final String val) {
            mNickname = mNickname + " " + val;
            return this;
        }


        public Builder addEmail(final String val) {
            mEmail = mEmail + " " + val;
            return this;
        }


        public Contact build() {

            return new Contact(this);
        }

    }


    public Contact(final Builder builder)  {
        this.mName = builder.mName;
        this.mNickname = builder.mNickname;
        this.mEmail = builder.mEmail;

    }


    public String getNickname() {
        return mNickname;
    }


    public String getEmail() {
        return mEmail;
    }

    public String getName() {
        return mName;
    }

}
