package edu.uw.team6tcss450.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentUpdatePasswordBinding;
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
public class UpdatePasswordFragment extends Fragment {

    FragmentUpdatePasswordBinding binding;

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editTextPasswordUpdate.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePasswordBinding.inflate(inflater);
        return binding.getRoot();

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_update_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonUpdate.setOnClickListener(this::attemptToupdate);
    }

    private void attemptToupdate(View view) {
        validatePasswordMatch();
    }

    private void validatePasswordMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editTextRetypeUpdate.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editTextPasswordUpdate.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editTextPasswordUpdate.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editTextPasswordUpdate.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editTextPasswordUpdate.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {

        String oldPass = binding.editTextOldPasswordSetting.getText().toString();
        String curNew = binding.editTextPasswordUpdate.getText().toString();

        if(oldPass.length() == 0 ){
            binding.editTextOldPasswordSetting.setError("Missing Current password");
        }
        else if(oldPass.equals(curNew)){
            binding.editTextOldPasswordSetting.setError("Current password and new password are same !");
        }
        else {
            UpdatePasswordFragmentArgs args = UpdatePasswordFragmentArgs.fromBundle(getArguments());
            String email = args.getEmail();

            System.out.println("Email from the updatePassword fragment : " + email);

            connectPUT(email, oldPass, curNew);
        }
    }

    public void connectPUT(String email, String old, String curNew){

        String url = "https://tcss450-team6.herokuapp.com/reset/password";
        JSONObject body = new JSONObject();
//        mEmail = binding.editTextEmailGetEmail.getText().toString();
        System.out.println("in Connect method");
        try {
            body.put("email", email);
            body.put("oldPassword", old);
            body.put("password", curNew);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                this::observeResponse,
                 this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//        //Instantiate the RequestQueue and add the request to the queue
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        queue.add(request);

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
                    System.out.println("Respone handler: "+msg);
                    binding.editTextOldPasswordSetting.setError("Error: " + msg);

                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                System.out.println("Done updating the password.");
                Navigation.findNavController(getView())
                        .navigate(UpdatePasswordFragmentDirections
                                .actionUpdatePasswordFragmentToDoneChangeFragment());
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
//                System.out.println("Error in herer");
                binding.editTextPasswordUpdate.setError("Error: " + error.toString());
            } catch (Exception e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
//                System.out.println("Error in here " + error.toString());
                binding.editTextPasswordUpdate.setError("Error: " + response.toString());
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}