package edu.uw.team6tcss450.ui.auth.signin;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentSignInBinding;

/**
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    /**
     * Binding object of this class
     */
    private FragmentSignInBinding binding;

    private SignInViewModel mSignInModel;

    /**
     * String variable to store user entered email address
     */
    private String email;

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

        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //sets the onclick event for register button
        //navigates to register fragment
        binding.buttonRegister.setOnClickListener(button -> {

            //Use the navigate method to perform the navigation.
            Navigation.findNavController(getView()).navigate(
                    SignInFragmentDirections.actionSignInFragmentToRegisterFragment()
            );
        });

        //on click listener for sign in button
        binding.buttonSignin.setOnClickListener(this::onClickSignIn);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse
        );

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.textEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    /**
     * Method to validate user inputs from Sign_in xml file
     * If all inputs are valid then navigates to success fragment
     * @param view
     */
    private void onClickSignIn(View view){

        email = binding.textEmail.getText().toString();
        int size = email.length();
        int occurrence = size - (email.replaceAll("@", "")).length();

        if(binding.textEmail.length()==0){
            binding.textEmail.setError("Please enter an email address!");
        }
        else if(occurrence != 1){
            binding.textEmail.setError("Invalid Email Address. Must contain single '@' ");
        }
        else if(binding.editPassword.length()==0) {
            binding.editPassword.setError("Please enter a password!");
        }
        else{

            //lab3 update
            verifyAuthWithServer();

            //if input is valid - generate JWT
//            String jwt = generateJwt(email);

            //lab2 update
            //navigate to mainActivity
//            SignInFragmentDirections.ActionSignInFragmentToMainActivity direction =
//                    SignInFragmentDirections.actionSignInFragmentToMainActivity(jwt);

            //navigates to success fragments
//            SignInFragmentDirections.ActionSignInFragmentToSuccessFragment direction =
//                    SignInFragmentDirections.actionSignInFragmentToSuccessFragment(email, jwt);

            //Use the navigate method to perform the navigation.
//            Navigation.findNavController(getView()).navigate(direction);

//            getActivity().finish();
        }
    }

    private void verifyAuthWithServer() {

        mSignInModel.connect(
                binding.textEmail.getText().toString(),
                binding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
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
                    binding.textEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    Navigation.findNavController(getView())
                            .navigate(SignInFragmentDirections
                                    .actionSignInFragmentToMainActivity(generateJwt( binding.textEmail.getText().toString())));

//
                } catch (Exception e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * This helper method is creating a JSON Web Token (JWT). In future labs, the JWT will
     * be created and sent to us from the Web Service. For now, we will "fake" that and create
     * the JWT client-side. This is ANTI-PATTERN!!! Do not create JWTs client-side.
     *
     * @param email the email used to encode into the JWT
     * @return the resulting JWT
     */
    private String generateJwt(final String email) {
        String token;
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret key don't use a string literal in " +
                    "production code!!!");
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("email", email)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("JWT Failed to Create.");
        }
        return token;
    }
}