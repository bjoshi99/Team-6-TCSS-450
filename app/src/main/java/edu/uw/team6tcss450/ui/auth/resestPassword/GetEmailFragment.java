package edu.uw.team6tcss450.ui.auth.resestPassword;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.team6tcss450.databinding.FragmentGetEmailBinding;
import edu.uw.team6tcss450.ui.auth.register.RegisterViewModel;
import edu.uw.team6tcss450.utils.PasswordValidator;

import static edu.uw.team6tcss450.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdLength;
import static edu.uw.team6tcss450.utils.PasswordValidator.checkPwdSpecialChar;

/**
 * create an instance of this fragment.
 */
public class GetEmailFragment extends Fragment {

    FragmentGetEmailBinding binding;
    String mEmail;
    String mPassCode;
    GetEmailViewModel getEmailViewModel;

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    public GetEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEmailViewModel = new ViewModelProvider(getActivity())
                .get(GetEmailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGetEmailBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_get_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mEmail = binding.editTextEmailGetEmail.getText().toString();

        binding.buttonSend.setOnClickListener(this::onClickSend);
        binding.buttonVerifyReset.setOnClickListener(this::onClickVerify);

        getEmailViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void onClickVerify(View v){
        String userEnteredString = binding.editTextGetpasscodeReset.getText().toString();

        if(userEnteredString.equals(mPassCode)){

            //navigate to change password fragment
            GetEmailFragmentDirections.ActionGetEmailFragmentToChangePasswordFragment directions =
                GetEmailFragmentDirections.actionGetEmailFragmentToChangePasswordFragment(mEmail, mPassCode);;

            Navigation.findNavController(getView()).navigate(directions);
        }
        else{
            popUpHelper();
        }
    }

    private void popUpHelper(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Setting message manually and performing action on button click
        builder.setMessage("We've sent you an email that contains a temporary code to verify your identity. \n\n"
                + "Please check your spam folder, if you can't find it. If you still can't find it, we can resend it.")
                .setCancelable(false)
                .setPositiveButton("Resend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                        mEmail = binding.editTextEmailGetEmail.getText().toString();
                        getEmailViewModel.connect(mEmail);
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
        alert.setTitle("Incorrect Passcode!");
        alert.show();
    }

    private void onClickSend(View v){
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editTextEmailGetEmail.getText().toString().trim()),
                this::send,
                result -> binding.editTextEmailGetEmail.setError("Please enter a valid Email address."));
    }

    private void send(){
        mEmail = binding.editTextEmailGetEmail.getText().toString();
        getEmailViewModel.connect(mEmail);
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {

        System.out.println("In observerResponse");
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    System.out.println("_--------------------------------------------------------_");
                    System.out.println("Json: "+response.toString());
                    String errorMessage = response.getJSONObject("data").getString("message");

                    if (errorMessage.equals("User not found")) {
                        //Please verify your email before signing in.
                        //do if email is not verified - pop up window for re-send email notification
                        binding.editTextEmailGetEmail.setError(
                                "Error: " +
                                        response.getJSONObject("data").getString("message"));
                    }

                } catch (JSONException e) {
                    Log.e("2: JSON Parse Error", e.getMessage());
                }
            }
            else {
                try {
//                    System.out.println("Json: "+response.toString());

                    Toast.makeText(getActivity(), "Email resent successfully!", Toast.LENGTH_SHORT).show();

                    mPassCode = response.getString("Verificationcode");

                    binding.editTextGetpasscodeReset.setVisibility(View.VISIBLE);
                    binding.textPasscodeDirection.setVisibility(View.VISIBLE);
                    binding.buttonVerifyReset.setVisibility(View.VISIBLE);

                    binding.buttonSend.setEnabled(false);

                }
                catch (Exception e) {
                    Log.e("3: JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}