package edu.uw.team6tcss450.ui.auth.signin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentSignInBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    //Required empty public constructor
    public SignInFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("SignInFrag", "SigninFrag onviewcreated called ");

        //Local ViewBinding.
        FragmentSignInBinding binding = FragmentSignInBinding.bind(getView());

        //Attach the click listener to the button(Sign In).
        binding.button.setOnClickListener(button -> {

            Navigation.findNavController(getView()).navigate(
                    SignInFragmentDirections
                            .actionSignInFragmentToMainActivity());

            //This tells the containing Activity that we are done with it.
            // It will not be added to backstack.
            getActivity().finish();
        });
    }
}