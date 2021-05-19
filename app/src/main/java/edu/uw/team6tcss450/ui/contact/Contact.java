package edu.uw.team6tcss450.ui.contact;

import java.io.Serializable;

public class Contact implements Serializable {

    private String mName;
    private String mUserName;
    private String mEmail;


    public static class Builder {
        private String mName;
        private String mUserName;
        private String mEmail;


        public Builder(String theName, String theUserName, String theEmail) {
            this.mName = theName;
            this.mUserName = theUserName;
            this.mEmail = theEmail;
        }


        public Builder addName(final String theVal) {
            mName = mName + " " + theVal;
            return this;
        }

        public Builder addUserName(final String val) {
            mUserName = mUserName + " " + val;
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
        this.mUserName = builder.mUserName;
        this.mEmail = builder.mEmail;

    }


    public String getUserName() {
        return mUserName;
    }


    public String getEmail() {
        return mEmail;
    }

    public String getName() {
        return mName;
    }

}
