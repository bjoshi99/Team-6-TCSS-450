package edu.uw.team6tcss450.ui.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentContactCardBinding;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mContact;

    public ContactRecyclerViewAdapter(List<Contact> List){
        this.mContact = List;
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup theParent, int theViewType) {
        return new ContactViewHolder(LayoutInflater
                .from(theParent.getContext())
                .inflate(R.layout.fragment_contact_card2, theParent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.ContactViewHolder theHolder, int thePosition) {
        theHolder.setContacts(mContact.get(thePosition));
    }


    @Override
    public int getItemCount() {
        return mContact.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public FragmentContactCardBinding mBinding;
        private Contact mContact;

        public ContactViewHolder(View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentContactCardBinding.bind(theView);
            theView.setOnClickListener(this);
        }

        @Override
        public void onClick(View theView) {

        }


        void setContacts(Contact theContact) {
            mContact = theContact;
            mBinding.editUserName.setText(theContact.getUserName());
            mBinding.editCurrentChat.setText(theContact.getEmail());
        }


    }
}
