package edu.uw.team6tcss450.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team6tcss450.databinding.FragmentCreateChatBinding;

import edu.uw.team6tcss450.ui.chat.chatList.ChatRoom;


public class CreateChatFragment extends Fragment {


    public FragmentCreateChatBinding mBinding;



    public CreateChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
//        UserInfoViewModel model = new ViewModelProvider(getActivity())
//                .get(UserInfoViewModel.class);
//
//        mModel = new ViewModelProvider(getActivity()).get(ContactModel.class);
//        mModel.connectGet(model.getmJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCreateChatBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);

        mBinding.buttonCreate.setOnClickListener(button -> {
            ChatRoom chatRoom = new ChatRoom.Builder(
                    mBinding.createChatFragment.toString())
                    .build();

            Navigation.findNavController(getView()).navigate(
                    CreateChatFragmentDirections.actionCreateChatFragmentToChatListFragment()
            );
        });


//        mBinding.buttonSearch.setOnClickListener(button -> {
//
//            String searched = mBinding.contactSearchName.getText().toString();
//            boolean isFound = false;
//
//            List<Contact> list = mModel.getContactList().getValue();
//            System.out.println("I am in the button cliecked !!");
//
//            for(Contact c : list){
//                System.out.println("Iterating !!");
//                if(c.getEmail().equals(searched) || c.getName().equals(searched)){
//
//                    mBinding.searchSendMsg.setVisibility(View.VISIBLE);
//                    mBinding.searchSendRequest.setVisibility(View.VISIBLE);
//                    //Found the matching user.
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("Name : " + c.getName() + "\n"
//                            + "Nickname : " + c.getNickname() + "\n" +
//                            "Email : " + c.getEmail());
//
//                    mBinding.textSearchResult.setText(sb.toString());
//                    isFound = true;
//                    break;
//                }
//            }
//
//            if(!isFound){
//                mBinding.searchSendMsg.setVisibility(View.INVISIBLE);
//                mBinding.searchSendRequest.setVisibility(View.INVISIBLE);
//                //mBinding.searchSendMsg.setVisibility(View.VISIBLE);
//                //mBinding.searchSendRequest.setVisibility(View.VISIBLE);
//                String st = "No results found.";
//                mBinding.textSearchResult.setText(st);
//            }
//        });

    }

}