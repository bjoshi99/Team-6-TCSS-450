package edu.uw.team6tcss450.ui.chat;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;

import java.util.List;

import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentChatMessageBinding;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MessageViewHolder> {

    private final List<ChatMessage> mMessages;
    private final String mEmail;

    //changes to add more than one type of card in recycler view
    private static final int ITEM_TYPE_ME = 0;
    private static final int ITEM_TYPE_OTHER = 1;

    public ChatRecyclerViewAdapter(List<ChatMessage> messages, String email) {
        this.mMessages = messages;
        mEmail = email;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new MessageViewHolder(LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.fragment_chat_message, parent, false));

        if(viewType == ITEM_TYPE_OTHER){
            return new MessageViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.test, parent, false));
        }
        else{
            return new MessageViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.layout_chat_right, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.setMessage(mMessages.get(position));
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if((mMessages.get(position).getSender()).equals(mEmail)){
            return ITEM_TYPE_ME;
        }
        else{
            return ITEM_TYPE_OTHER;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentChatMessageBinding binding;

        //updated part
        TextView sender;
        TextView time;
        TextView msg;

        public MessageViewHolder(@NonNull View view) {
            super(view);
            mView = view;
//            binding = FragmentChatMessageBinding.bind(view);

            //update
            time = mView.findViewById( R.id.msg_time );
            msg = mView.findViewById(R.id.text_message);
        }

        void setMessage(final ChatMessage message) {
            final Resources res = mView.getContext().getResources();
//            final MaterialCardView card = binding.cardRootHome;

            int standard = (int) res.getDimension(R.dimen.chat_margin);
            int extended = (int) res.getDimension(R.dimen.chat_margin_sided);

            //to get only time from the time stamp of the message
            String timeSlot = message.getTimeStamp();
//            String[] split = ((timeSlot.split(" ")[1]).split(":"));
//
//            String shortTime = split[0] + ":" + split[1];

            if (mEmail.equals(message.getSender())) {

                message.byMe = true;

//                sender.setVisibility(View.GONE);

                msg.setText(message.getMessage());
                time.setHint(timeSlot);
                time.setHintTextColor(res.getColor(R.color.grayHint, null));

                //This message is from the user. Format it as such
//                binding.textMessageTitle.setVisibility(View.GONE);
//                binding.textMessage.setText(message.getMessage());
//                ViewGroup.MarginLayoutParams layoutParams =
//                        (ViewGroup.MarginLayoutParams) card.getLayoutParams();
//                //Set the left margin
//                layoutParams.setMargins(extended, standard, standard, standard);
//                // Set this View to the right (end) side
//                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity =
//                        Gravity.END;
//
//                card.setCardBackgroundColor(
//                        ColorUtils.setAlphaComponent(
//                                res.getColor(R.color.design_default_color_primary, null),
//                                16));
//                binding.textMessage.setTextColor(
//                        res.getColor(R.color.design_default_color_secondary, null));
//
//                card.setStrokeWidth(standard / 5);
//                card.setStrokeColor(ColorUtils.setAlphaComponent(
//                        res.getColor(R.color.design_default_color_secondary, null),
//                        200));
//
//                //Round the corners on the left side
//                card.setShapeAppearanceModel(
//                        card.getShapeAppearanceModel()
//                                .toBuilder()
//                                .setTopLeftCorner(CornerFamily.ROUNDED,standard * 2)
//                                .setBottomLeftCorner(CornerFamily.ROUNDED,standard * 2)
//                                .setBottomRightCornerSize(0)
//                                .setTopRightCornerSize(0)
//                                .build());
//
//                card.requestLayout();
            } else {

                message.byMe = false;

                sender = mView.findViewById( R.id.text_sender );
                sender.setHint(message.getSender());
                msg.setText(message.getMessage());
                time.setHint(timeSlot);
                time.setHintTextColor(res.getColor(R.color.grayHint, null));

                //This message is from another user. Format it as such
//                binding.textMessageTitle.setHint(message.getSender() + ":");
//                binding.textMessage.setText(message.getMessage());
//                ViewGroup.MarginLayoutParams layoutParams =
//                        (ViewGroup.MarginLayoutParams) card.getLayoutParams();
//
//                //Set the right margin
//                layoutParams.setMargins(standard, standard, extended, standard);
//                // Set this View to the left (start) side
//                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity =
//                        Gravity.START;
//
//                card.setCardBackgroundColor(
//                        ColorUtils.setAlphaComponent(
//                                res.getColor(R.color.design_default_color_secondary, null),
//                                16));
//
//                card.setStrokeWidth(standard / 5);
//                card.setStrokeColor(ColorUtils.setAlphaComponent(
//                        res.getColor(R.color.design_default_color_secondary, null),
//                        200));
//
//                binding.textMessage.setTextColor(
//                        res.getColor(R.color.nice, null));
//
//                //Round the corners on the right side
//                card.setShapeAppearanceModel(
//                        card.getShapeAppearanceModel()
//                                .toBuilder()
//                                .setTopRightCorner(CornerFamily.ROUNDED,standard * 2)
//                                .setBottomRightCorner(CornerFamily.ROUNDED,standard * 2)
//                                .setBottomLeftCornerSize(0)
//                                .setTopLeftCornerSize(0)
//                                .build());
//                card.requestLayout();
            }
        }
    }
}
