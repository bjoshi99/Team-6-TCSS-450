package edu.uw.team6tcss450.model;

import androidx.annotation.HalfFloat;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class NewMessageCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewMessageCount;
    private MutableLiveData<Map<Integer, Integer>> mNewMessageChatCount;

    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);

        mNewMessageChatCount = new MutableLiveData<>(new HashMap<>());
    }

    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    public void increment() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
    }

    public void reset() {
        mNewMessageCount.setValue(0);
    }

    public void increment(int chatID) {
//        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
        if(mNewMessageChatCount.getValue().get(new Integer(chatID)) == null){
            mNewMessageChatCount.getValue().put(new Integer(chatID), 1);
        }
        else {
            int last = mNewMessageChatCount.getValue().get(new Integer(chatID));
            mNewMessageChatCount.getValue().put(new Integer(chatID), last + 1);
        }
    }

    public void decrease(int chatID){
        if(mNewMessageChatCount.getValue().get(new Integer(chatID)) == null || mNewMessageChatCount.getValue().get(new Integer(chatID)) == 0){
            //do nothing basically
        }
        else{
            int pre = mNewMessageChatCount.getValue().get(new Integer(chatID));
            if(mNewMessageCount.getValue() - pre <= 0){
                this.reset();
            }
            else{
                mNewMessageCount.setValue(mNewMessageCount.getValue() - pre);
            }
            mNewMessageChatCount.getValue().remove(new Integer(chatID));
        }
    }

    public boolean doesContainMessage(int chatID){
        return mNewMessageChatCount.getValue().get(new Integer(chatID)) != null;
    }
}
