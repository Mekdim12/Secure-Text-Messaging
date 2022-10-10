package com.example.securemessaging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MESSAGE_ADAPTER extends  RecyclerView.Adapter {
    public static  final int LEFTSIDE = 1;
    public static  final int RIGHSIDE =2;
    public static  final int NOMESSAGE = 3;


    private List<MessageModelClass> message;

    MESSAGE_ADAPTER(List<MessageModelClass> message){
        this.message = message;
        notifyDataSetChanged();
    }





    @Override
    public int getItemViewType(int position) {


            switch (message.get(position).getMessageType()){
                case "R":
                    return LEFTSIDE;
                case "S":
                    return  RIGHSIDE;

                default:
                    return  -1;
            }



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case LEFTSIDE:
                View leftSideChat = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_side_chat,parent,false);
                return new LEFT_SIDE_CHAT_MESSAGE_HOLDER(leftSideChat);
            case RIGHSIDE:
                View rightSideChat = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_side_chat,parent,false);
                return new RIGHT_SIDE_CHAT_MESSAGE_HOLDER(rightSideChat);
            case NOMESSAGE:
                View empty = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_chat_yet,parent,false);
                return new EMPTY_CHAT_VIEW(empty);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            switch (message.get(position).getMessageType()) {
                case "R":
                    String message_ = message.get(position).getMessage();
                    LEFT_SIDE_CHAT_MESSAGE_HOLDER holder1 = (LEFT_SIDE_CHAT_MESSAGE_HOLDER) holder;
                    holder1.setTextMessage(message_);
                    break;
                case "S":
                    String message_2 = message.get(position).getMessage();
                    RIGHT_SIDE_CHAT_MESSAGE_HOLDER holder2 = (RIGHT_SIDE_CHAT_MESSAGE_HOLDER) holder;
                    holder2.setTextMessage(message_2);
                    break;

            }


    }

    @Override
    public int getItemCount() {

//        if(message.size() == 0){
//            return 1;
//        }else{
            return message.size();
//        }

    }

     class LEFT_SIDE_CHAT_MESSAGE_HOLDER  extends RecyclerView.ViewHolder{

        private final TextView messageTextHolderLeftSide;

         public LEFT_SIDE_CHAT_MESSAGE_HOLDER(@NonNull View itemView) {
             super(itemView);
             messageTextHolderLeftSide = itemView.findViewById(R.id.left_side_chat_textArea);
         }

         private void setTextMessage(String text){
             messageTextHolderLeftSide.setText(text);
         }
     }

    class RIGHT_SIDE_CHAT_MESSAGE_HOLDER  extends RecyclerView.ViewHolder{

        private final TextView messageTextHolderRightSide;

        public RIGHT_SIDE_CHAT_MESSAGE_HOLDER(@NonNull View itemView) {
            super(itemView);
            messageTextHolderRightSide = itemView.findViewById(R.id.right_side_of_chat);
        }

        private void setTextMessage(String text){
            messageTextHolderRightSide.setText(text);
        }
    }


    class EMPTY_CHAT_VIEW  extends RecyclerView.ViewHolder{


        public EMPTY_CHAT_VIEW( View itemView) {
            super(itemView);

        }


    }


}