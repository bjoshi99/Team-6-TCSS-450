package edu.uw.team6tcss450.ui.auth.register;

import static edu.uw.team6tcss450.utils.PasswordValidator.*;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdLength;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdSpecialChar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.team6tcss450.databinding.FragmentRegisterBinding;
import edu.uw.team6tcss450.utils.PasswordValidator;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPasswordRegister.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegisterInRegisterFrag.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirstName.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirstName.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLastName.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLastName.setError("Please enter a last name."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmailRegister.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmailRegister.setError("Please enter a valid Email address."));
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editRePassword.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPasswordRegister.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPasswordRegister.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPasswordRegister.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPasswordRegister.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {

        mRegisterModel.connect(
                binding.editFirstName.getText().toString(),
                binding.editLastName.getText().toString(),
                binding.editEmailRegister.getText().toString(),
                binding.editPasswordRegister.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    private void navigateToLogin() {
        RegisterFragmentDirections.ActionRegisterFragmentToSignInFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToSignInFragment();

        directions.setEmail(binding.editEmailRegister.getText().toString());
        directions.setPassword(binding.editPasswordRegister.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

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
                    binding.editEmailRegister.setError(
                            "Error Authenticating: " +
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
