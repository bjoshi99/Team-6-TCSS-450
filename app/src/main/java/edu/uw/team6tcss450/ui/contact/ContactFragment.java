package edu.uw.team6tcss450.ui.contact;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.ui.chat.ChatListViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    List<ContactModel> userList;
    ContactModel mModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init();

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_contact);

        ContactRecyclerViewAdapter listAdapter = new ContactRecyclerViewAdapter(userList);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        return view;
    }

    private void init(){
        userList = new ArrayList<>();

        userList.add(new ContactModel(R.drawable.cat1, "CAT", "10:11 AM", "How you doing ?", "--------" ));
        userList.add(new ContactModel(R.drawable.cat2, "KITTY", "7:11 PM", "TCSS450 is fun.", "--------" ));
        userList.add(new ContactModel(R.drawable.dog, "WOLF", "12:00 AM", "Earth is flat.", "--------" ));
        userList.add(new ContactModel(R.drawable.alpaca, "ANIMAL", "1:11 AM", "I am furry.", "--------" ));
    }


}