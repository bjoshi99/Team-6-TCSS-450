package edu.uw.team6tcss450.ui.auth.resestPassword;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChangePasswordBinding;
import edu.uw.team6tcss450.databinding.FragmentGetEmailBinding;
import edu.uw.team6tcss450.ui.auth.register.RegisterFragmentDirections;
import edu.uw.team6tcss450.ui.auth.signin.SignInFragmentArgs;
import edu.uw.team6tcss450.utils.PasswordValidator;

import static edu.uw.team6tcss450.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdLength;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdUpperCase;

/**
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    FragmentChangePasswordBinding binding;
    String mEmail;
    String mPassword;
    String mVerificationcode;

    ChangePasswordViewModel changePasswordViewModel;

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editTextPassword.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    public ChangePasswordFragment(){
        //required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordViewModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChangePasswordBinding.inflate(inflater);
        return binding.getRoot();
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonUpdate.setOnClickListener(this::attemptToupdate);

        ChangePasswordFragmentArgs args = ChangePasswordFragmentArgs.fromBundle(getArguments());

        mVerificationcode = args.getVerificationCode();
        mEmail = args.getEmail();

        changePasswordViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptToupdate(View view) {
        validatePasswordMatch();
    }

    private void validatePasswordMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editTextRepassword.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editTextPassword.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editTextPassword.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editTextPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editTextPassword.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {

        mPassword = binding.editTextPassword.getText().toString();
        changePasswordViewModel.sendAndVerify(mPassword, mEmail, mVerificationcode);
    }


    private void navigateToLogin() {

        ChangePasswordFragmentDirections.ActionChangePasswordFragmentToSignInFragment directions =
                ChangePasswordFragmentDirections.actionChangePasswordFragmentToSignInFragment();

        directions.setEmail(mEmail);
        directions.setPassword(mPassword);

        Navigation.findNavController(getView()).navigate(directions);

        Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();

    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {

        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    String msg = response.getJSONObject("data").getString("message");

                    binding.editTextPassword.setError("Error: " +
                            response.getJSONObject("data").getString("message"));

                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}