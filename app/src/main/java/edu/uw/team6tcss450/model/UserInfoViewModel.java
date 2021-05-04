package edu.uw.team6tcss450.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserInfoViewModel extends ViewModel {

    private final String mEmail;
    private final String mUserName;
    private final String mJwt;

    private UserInfoViewModel(String email, String jwt, String username) {
        mEmail = email;
        mJwt = jwt;
        mUserName = username;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getmJwt() {
        return mJwt;
    }

    public String getUserName(){
        return mUserName;
    }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;
        private final String username;

        public UserInfoViewModelFactory(String email, String jwt, String userName) {
            this.email = email;
            this.jwt = jwt;
            this.username = userName;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == edu.uw.team6tcss450.model.UserInfoViewModel.class) {
                return (T) new edu.uw.team6tcss450.model.UserInfoViewModel(email, jwt, username);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + edu.uw.team6tcss450.model.UserInfoViewModel.class);
        }
    }


}

