package edu.uw.team6tcss450.ui.contact;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
        private Contact mTheContact;

        public ContactViewHolder(View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentContactSearchCardBinding.bind(theView);
            mBinding.buttonAdd.setOnClickListener(button -> {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                sendRequest(theView);
                                System.out.println("Tring to add the contact.");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(theView.getContext());
                builder.setMessage("Do you want send a friend request ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            });
            theView.setOnClickListener(this);
        }

        @Override
        public void onClick(View theView) {

        }


        void setContacts(Contact theContact) {
            mTheContact = theContact;
            mBinding.editUserName.setText(theContact.getNickname());
            mBinding.editCurrentChat.setText(theContact.getEmail());
        }

        private void sendRequest(View view) {

            System.out.println("request sent");

            String url =
                    "https://tcss450-team6.herokuapp.com/contacts";

            UserInfoViewModel model = new ViewModelProvider((MainActivity)(mView.getContext()))
                    .get(UserInfoViewModel.class);

            JSONObject body = new JSONObject();
            try {
                body.put("email", mTheContact.getEmail());
                body.put("verified", 0);
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

            mTheContact.req = true;
            ContactSearchRecyclerViewAdapter.this.notifyDataSetChanged();
            mContact.clear();
            Navigation.findNavController(mView).navigate(
                    ContactSearchFragmentDirections.actionContactSearchFragment2ToNavigationContact()
            );
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


    }
}
