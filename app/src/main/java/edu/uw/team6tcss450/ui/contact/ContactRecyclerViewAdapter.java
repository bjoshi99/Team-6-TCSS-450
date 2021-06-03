package edu.uw.team6tcss450.ui.contact;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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
import edu.uw.team6tcss450.model.UserInfoViewModel;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Contact> mContact;


    //changes to add more than one type of card in recycler view
    private static final int ITEM_TYPE_CONTACT = 0;
    private static final int ITEM_TYPE_REQUEST = 1;

    public ContactRecyclerViewAdapter(List<Contact> List){
        this.mContact = List;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup theParent, int theViewType) {
//        return new ContactViewHolder(LayoutInflater
//                .from(theParent.getContext())
//                .inflate(R.layout.fragment_contact_card2, theParent, false));

        if(theViewType == ITEM_TYPE_CONTACT){
            return new ContactViewHolder(LayoutInflater
                .from(theParent.getContext())
                .inflate(R.layout.fragment_contact_card2, theParent, false));
        }
        //(theViewType == ITEM_TYPE_REQUEST)
        else {
            //return the card view of the another type
            return new ContactRequestViewHolder(LayoutInflater
                .from(theParent.getContext())
                .inflate(R.layout.fragment_contact_request, theParent, false));
        }
    }

    //new bind view holder to handle more than on type of cards
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itype = getItemViewType(position);

        if(itype == ITEM_TYPE_CONTACT){
            ((ContactViewHolder) holder).setContacts(mContact.get(position));

        }
        else if(itype == ITEM_TYPE_REQUEST){
            //set data for request
            ((ContactRequestViewHolder) holder).setContacts(mContact.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if(mContact.get(position).req){
            return ITEM_TYPE_REQUEST;
        }
        else{
            return ITEM_TYPE_CONTACT;
        }
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

    public class ContactRequestViewHolder extends RecyclerView.ViewHolder{

        public final View mView;
        public FragmentContactRequestBinding mBinding;
        private Contact ct;

        public ContactRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            mBinding = FragmentContactRequestBinding.bind(itemView);

            mBinding.imageButtonAccept.setOnClickListener(this::accept);
            mBinding.imageButtonDecline.setOnClickListener(this::decline);
        }

        void setContacts(Contact theContact) {
            ct = theContact;
            mBinding.textNameContRequest.setText(theContact.getName());
            mBinding.textEmailContRequest.setText(theContact.getEmail());
        }

        private void delete(Contact ct){
            mContact.remove(ct);
        }

        private void accept(View view) {

            System.out.println("Clicked accept");

            String url =
                    "https://tcss450-team6.herokuapp.com/contacts";

            UserInfoViewModel model = new ViewModelProvider((MainActivity)(mView.getContext()))
                    .get(UserInfoViewModel.class);

            JSONObject body = new JSONObject();
            try {
                body.put("email", ct.getEmail());
                body.put("verified", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Request request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    body,
                    this::handleResult,
                    this::handleError) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", model.getmJwt());
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(mView.getContext())
                    .add(request);

        }

        private void handleResult(JSONObject jsonObject) {
            //request is accepted
            //count user as a friend
            ct.req = false;
            ContactRecyclerViewAdapter.this.notifyDataSetChanged();
            Toast.makeText(mView.getContext(), "Friend Request Accepted", Toast.LENGTH_SHORT).show();
        }

        private void handleError(final VolleyError error) {
            if (Objects.isNull(error.networkResponse)) {

                Toast.makeText(mView.getContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
            else {
                String data = new String(error.networkResponse.data, Charset.defaultCharset())
                        .replace('\"', '\'');
                Toast.makeText(mView.getContext(), "Error: " + data, Toast.LENGTH_SHORT).show();
            }
        }

        private void decline(View view) {

            //no need to notify the other user
            //just need to delete the card from recyclerview
            System.out.println("Clicked decline");
            ContactRecyclerViewAdapter.this.notifyDataSetChanged();
            this.delete(ct);
        }
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public FragmentContactCardBinding mBinding;
        private Contact mTheContact;

        public ContactViewHolder(View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentContactCardBinding.bind(theView);
            mBinding.buttonRemoveContact.setOnClickListener(button ->{
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteMember(mTheContact.getMemberId());
                                mContact.remove(mTheContact);
                                ContactRecyclerViewAdapter.this.notifyDataSetChanged();
                                System.out.println("after server remove, and recycler view notify.");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(theView.getContext());
                builder.setMessage("Do you want to delete this contact?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                    });
            theView.setOnClickListener(this);
        }

        @Override
        public void onClick(View theView) {

        }

        public void deleteMember(int memberId) {
            String url =
                    "https://tcss450-team6.herokuapp.com/contacts/contact/" + memberId;
            UserInfoViewModel model = new ViewModelProvider((MainActivity)(mView.getContext()))
                    .get(UserInfoViewModel.class);
            Request request = new JsonObjectRequest(
                    Request.Method.DELETE,
                    url,
                    null,
                    this::handleDeleteResult,
                    this::handleError) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", model.getmJwt());
                    System.out.println("********************************************************");
                    System.out.println("Inside of the deleteMember server call.");
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(mView.getContext())
                    .add(request);
        }


        private void handleDeleteResult(final JSONObject theResult){
            System.out.println("the contact removed ! from the handleDeleteResult method.");
        }
        private void handleError(final VolleyError theError) {

//        handleResult(null);
            System.out.println("***********************************************************");
            System.out.println("Error : " + theError.toString());
        }



        void setContacts(Contact theContact) {
            mTheContact = theContact;
            mBinding.editUserName.setText(theContact.getNickname());
            mBinding.editCurrentChat.setText(theContact.getEmail());
        }


    }
}
