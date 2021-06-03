package edu.uw.team6tcss450.ui.contact;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentContactBinding;
import edu.uw.team6tcss450.databinding.FragmentContactCard2Binding;
import edu.uw.team6tcss450.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private ContactModel mModel;


    public ContactFragment(){
        //Required empty public constructor.
    }

    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mModel = new ViewModelProvider(getActivity()).get(ContactModel.class);
        mModel.connectGet(model.getmJwt());

    }

    @Override
    public View onCreateView(LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedInstanceState) {

        // Inflate the layout for this fragment
        return theInflater.inflate(R.layout.fragment_contact, theContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);
        FragmentContactBinding binding = FragmentContactBinding.bind(getView());

        //Listener for the contacts recycler view adapter.
        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.recyclerviewContact.setAdapter(
                       new ContactRecyclerViewAdapter(contactList)
                );
            }
        });

        binding.buttonSearchContacts.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                   ContactFragmentDirections.actionNavigationContactToContactSearchFragment2()
            );
        });

    }


}