package edu.uw.team6tcss450.ui.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.team6tcss450.MainActivity;
import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentContactCardBinding;
import edu.uw.team6tcss450.databinding.FragmentContactRequestBinding;
import edu.uw.team6tcss450.databinding.FragmentContactSearchCardBinding;
import edu.uw.team6tcss450.model.UserInfoViewModel;

public class ContactSearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Contact> mContact;


    public ContactSearchRecyclerViewAdapter(List<Contact> List){
        this.mContact = List;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup theParent, int theViewType) {
        return new ContactViewHolder(LayoutInflater
                .from(theParent.getContext())
                .inflate(R.layout.fragment_contact_search_card, theParent, false));
    }

    //new bind view holder to handle more than on type of cards
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ContactSearchRecyclerViewAdapter.ContactViewHolder) holder).setContacts(mContact.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    //    @Override
//    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.ContactViewHolder theHolder, int thePosition) {
//
//        theHolder.setContacts(mContact.get(thePosition));
//    }


    @Override
    public int getItemCount() {
        return mContact.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public FragmentContactSearchCardBinding mBinding;
        private Contact mContact;

        public ContactViewHolder(View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentContactSearchCardBinding.bind(theView);
            theView.setOnClickListener(this);
        }

        @Override
        public void onClick(View theView) {

        }


        void setContacts(Contact theContact) {
            mContact = theContact;
            mBinding.editUserName.setText(theContact.getNickname());
            mBinding.editCurrentChat.setText(theContact.getEmail());
        }


    }
}
