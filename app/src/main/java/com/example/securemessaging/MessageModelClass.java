package com.example.securemessaging;

public class MessageModelClass {



        private String MessageType;
        private String Message;


        MessageModelClass(String MessageType, String Message){

            this.MessageType = MessageType;
            this.Message = Message;

        }

    public String getMessageType() {
        return MessageType;
    }

    public String getMessage() {
        return Message;
    }






    }

