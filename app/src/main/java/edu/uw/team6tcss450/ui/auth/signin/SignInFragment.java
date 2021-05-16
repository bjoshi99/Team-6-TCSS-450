package edu.uw.team6tcss450.ui.auth.signin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.team6tcss450.databinding.FragmentSignInBinding;
import edu.uw.team6tcss450.ui.auth.signin.SignInFragmentArgs;
import edu.uw.team6tcss450.ui.auth.signin.SignInFragmentDirections;
import edu.uw.team6tcss450.ui.auth.signin.SignInViewModel;
import edu.uw.team6tcss450.utils.PasswordValidator;

import static edu.uw.team6tcss450.utils.PasswordValidator.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel mSignInModel;
    private String mUserName;

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToRegisterFragment()
                ));

        binding.buttonSignin.setOnClickListener(this::attemptSignIn);

        binding.textForgotLogin.setOnClickListener(this::forgotnavigate);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.textEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());
        mUserName = args.getUsername();
    }

    private void forgotnavigate(View view) {

        Navigation.findNavController(getView()).navigate(
                SignInFragmentDirections.actionSignInFragmentToGetEmailFragment()
        );
    }

    private void attemptSignIn(final View button) {
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.textEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.textEmail.setError("Please enter a valid Email address."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {

        mSignInModel.connect(
                binding.textEmail.getText().toString(),
                binding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt, String username) {
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(email, jwt, username));
        getActivity().finish();
    }

    private void popUpHelper(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Setting message manually and performing action on button click
        builder.setMessage("We've sent you an email that contains a link to verify your identity. \n\n"
                            + "Please check your spam folder, if you can't find it. If you still can't find it, we can resend it.")
                .setCancelable(false)
                .setPositiveButton("Resend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                        mSignInModel.sendVerification(binding.textEmail.getText().toString());
                    }
                })
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Done' Button
                        dialog.cancel();
                        Toast.makeText(getActivity(), "You selected Ok!", Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Please verify your email address!");
        alert.show();
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
//                        System.out.println("_--------------------------------------------------------_");
//                        System.out.println("Json: "+response.toString());
                        String errorMessage = response.getJSONObject("data").getString("message");

                        if (errorMessage.equals("Please verify your email before signing in.")) {
                            //Please verify your email before signing in.
                            //do if email is not verified - pop up window for re-send email notification
                            popUpHelper();
                        }

                        else {
                            binding.textEmail.setError(
                                    "Error Authenticating: " +
                                            response.getJSONObject("data").getString("message"));
                        }

                    } catch (JSONException e) {
                        Log.e("2: JSON Parse Error", e.getMessage());
                    }
                }
                else {
                    try {
                        System.out.println("Json: "+response.toString());

                        if((response.getString("message").equals("Email resent Successfully."))){
                            Toast.makeText(getActivity(), "Email resent successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            navigateToSuccess(
                                    response.getString("token"),
                                    binding.textEmail.getText().toString(),
                                    mUserName
                            );
                        }

                    } catch (JSONException e) {
                        Log.e("3: JSON Parse Error", e.getMessage());
                    }
                }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
