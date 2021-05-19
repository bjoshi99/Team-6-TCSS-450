package edu.uw.team6tcss450.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentHomeBinding;
import edu.uw.team6tcss450.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ArrayList<HomeModel> notifcationList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        makeUp();

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView_Home);

        HomeRecyclerViewAdapter listAdapter = new HomeRecyclerViewAdapter(notifcationList);
        mRecyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        return view;

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        //for now, email is displayed on home but for future, we can change it to username
        FragmentHomeBinding.bind(getView()).texttile.setText("Welcome " + model.getEmail() + " !");
    }

    private void makeUp() {
        notifcationList = new ArrayList<>();

        notifcationList.add(new HomeModel("Updated layout.", "Alexis" , "123", "9:04 pm"));
        notifcationList.add(new HomeModel("Good night!", "Jun" , "123", "11:15 pm"));
        notifcationList.add(new HomeModel("Just found this bug!", "Rj" , "123", "6:48 pm"));
        notifcationList.add(new HomeModel("RJ found a bug, need to fix asap.", "Bhavesh" , "123", "7:55 pm"));
        notifcationList.add(new HomeModel("Upcoming meeting: 9:30 am.", "Charles" , "123", "10:00 pm"));
    }
}