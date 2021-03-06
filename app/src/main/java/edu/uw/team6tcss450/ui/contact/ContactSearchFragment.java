package edu.uw.team6tcss450.ui.contact;

import android.nfc.Tag;
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

import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentContactBinding;
import edu.uw.team6tcss450.databinding.FragmentContactSearchBinding;
import edu.uw.team6tcss450.model.UserInfoViewModel;


public class ContactSearchFragment extends Fragment {


    public FragmentContactSearchBinding mBinding;
    private UserInfoViewModel model;
    private ContactModel mModel;


    public ContactSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mModel = new ViewModelProvider(getActivity()).get(ContactModel.class);
        mModel.connectGet(model.getmJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentContactSearchBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);

        //Listener for the contacts recycler view adapter.
        mModel.addContactSearchListObserver(getViewLifecycleOwner(), contactSearchList -> {
            if (!contactSearchList.isEmpty()) {
                mBinding.recyclerviewContact.setAdapter(
                        new ContactSearchRecyclerViewAdapter(contactSearchList)
                );
            }
        });

        mBinding.buttonCancel.setOnClickListener(button -> {
            mModel.getSearchList().getValue().clear();
            Navigation.findNavController(getView()).navigate(
                ContactSearchFragmentDirections.actionContactSearchFragment2ToNavigationContact()
           );
        });


        mBinding.buttonSearch.setOnClickListener(button -> {

            String searched = mBinding.contactSearchName.getText().toString();
            mModel.searchMember(model.getmJwt(), searched);

            boolean isFound = false;

            List<Contact> list = mModel.getSearchList().getValue();
            System.out.println("I am in the button cliecked !!" + list.isEmpty());


        });

    }




}