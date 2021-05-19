package edu.uw.team6tcss450.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentDoneChangeBinding;
import edu.uw.team6tcss450.ui.auth.signin.SignInFragmentDirections;

/**
 * create an instance of this fragment.
 */
public class DoneChangeFragment extends Fragment {

    FragmentDoneChangeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDoneChangeBinding.inflate(inflater);
        return binding.getRoot();

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_done_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonDoneDone.setOnClickListener(this::onClickNavigate);
    }

    private void onClickNavigate(View view) {

        NavController controller = Navigation.findNavController(view);
        controller.popBackStack(R.id.fragmentSetting, true);

        Navigation.findNavController(getView())
                .navigate(DoneChangeFragmentDirections
                        .actionDoneChangeFragmentToNavigationHome());
    }
}