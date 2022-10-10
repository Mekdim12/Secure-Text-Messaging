package com.example.securemessaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainMessage extends AppCompatActivity {


    private ImageView sendButton,backbutton;
    private String defaultUserID,userId ;
    SharedPreferences MainInformationHolder = null;
    TextView userName,userPhoneNumber;
    DB_HELPER dbHelper;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayout;
    MESSAGE_ADAPTER mainAdapter;
    MessageEncryptionLogicLayerFinalVersion2 ecrypterAndDecrypter;
    EditText textArea;
    ArrayList<MessageModelClass> msg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout_resources);

        ecrypterAndDecrypter = new MessageEncryptionLogicLayerFinalVersion2();

        userName = findViewById(R.id.namDisp);
        recyclerView = findViewById(R.id.recyclerViewe);
        textArea = findViewById(R.id.messageArea);
        userPhoneNumber = findViewById(R.id.phoneNumDisp);
        dbHelper = new DB_HELPER(this);
        sendButton = findViewById(R.id.sendButton);
        msg  = new ArrayList<>();
        linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);
        mainAdapter  = new MESSAGE_ADAPTER(msg);
        recyclerView.setAdapter(mainAdapter);
        backbutton = findViewById(R.id.backbutton);

        mainAdapter.notifyDataSetChanged();








        MainInformationHolder = getApplicationContext().getSharedPreferences("Information", Context.MODE_PRIVATE);
        String PhoneNumber  = MainInformationHolder.getString("PhoneNumber",null);



        if(PhoneNumber != null) {

            Cursor DefaultUserCursor = dbHelper.getIdOFUser("User", PhoneNumber);

            if(DefaultUserCursor.getCount() > 0 ) {
                DefaultUserCursor.moveToFirst();
                defaultUserID = DefaultUserCursor.getString(0);




                // This identifies where is this activities is been calling from
                String flag = getIntent().getStringExtra("Flag");

                if (flag.trim().equals("NewContacts")) {
                    // if from the new contacts adding page
                    String name = getIntent().getStringExtra("Name").trim();
                    String phone = getIntent().getStringExtra("Number").trim();


                    userName.setText(name);
                    userPhoneNumber.setText(phone);

                    Cursor cursor = dbHelper.getIdOFUser(name, phone);

                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        userId = cursor.getString(0);





                        // *********************************** now u can listen to button click listener now u hav both sender reciver id

                        sendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String CurrentMessage = textArea.getText().toString();
                                textArea.setText("");
                                sendButtonFunctionality(CurrentMessage.trim());

                            }
                        });


                        messageRetriverFromTheDatanbase();



                    } else {
                                //    if this contact is never been chat list  add it to chat contact list

                                if(dbHelper.addNewUserToMyChatList(name,phone)){
                                    // get the user id after inserting it

                                    Cursor cursor1 = dbHelper.getIdOFUser(name,phone);
                                    if(cursor1.getCount() > 0){

                                        cursor1.moveToFirst();
                                        userId = cursor1.getString(0);



                                        // *********************************** now u can listen to button click listener now u hav both sender reciver id

                                        sendButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String CurrentMessage = textArea.getText().toString();
                                                textArea.setText("");
                                                sendButtonFunctionality(CurrentMessage.trim());



                                            }
                                        });



                                        messageRetriverFromTheDatanbase();

                                    }else{

                                        Toast.makeText(this, "userId NO ID", Toast.LENGTH_SHORT).show();

                                    }
                                }else{
                                    Toast.makeText(this, "There is no Such ID! with this user on database", Toast.LENGTH_SHORT).show();

                                }

                    }


                } else if (flag.trim().equals("FromMainView")) {
                    // if from main chatting people list viewing page
                    userId  = getIntent().getStringExtra("ID");

                    sendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String CurrentMessage = textArea.getText().toString();
                            textArea.setText("");
                            sendButtonFunctionality(CurrentMessage.trim());

                        }
                    });


                    messageRetriverFromTheDatanbase();

                } else {
                    // something must went wrong
                }

            }else{
                Toast.makeText(this, "Something is wrong while setting up the default phone number No Record On Database!", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Something is wrong while setting up the default phone number", Toast.LENGTH_SHORT).show();
        }


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(MainMessage.this, MainActivity.class);
                startActivity(inte);
            }
        });

    }

    public void sendButtonFunctionality(String message){

        // apply the encryption

        forBackGroundActivitiesOf back = new forBackGroundActivitiesOf(message,dbHelper,msg,mainAdapter);
        Thread thread = new Thread(back);
        thread.start();



//        sendMessage(cipheredMessage,"");
    }




    public void messageRetriverFromTheDatanbase(){
        Cursor messageWithtThisUser = dbHelper.getChatWithSpecificUser(defaultUserID, userId);
//        if sender id is equal is to wiht defaultuser id then send it to message adapter with send model class flag or type


        if(messageWithtThisUser.getCount() > 0){

            while(messageWithtThisUser.moveToNext()){

                String reciverID = messageWithtThisUser.getString(0);
                String SenderId = messageWithtThisUser.getString(1);
                String decryptedMessage =  messageWithtThisUser.getString(2);
                String encyptedMessage = messageWithtThisUser.getString(3);
                String timeStamp = messageWithtThisUser.getString(4);

                if( defaultUserID.equals(reciverID)){
                    msg.add(new MessageModelClass("R",decryptedMessage+"-"+timeStamp));
                    mainAdapter.notifyItemInserted(msg.size()-1);
                }else{
                    msg.add(new MessageModelClass("S",decryptedMessage+"-"+timeStamp));
                    mainAdapter.notifyItemInserted(msg.size()-1);
                }


            }
        }else{

                // no chat has been started yet
                // prepare another layout that basically tells there is no chat

            Toast.makeText(this, "There Is No Chat With This Person Yet", Toast.LENGTH_SHORT).show();
        }




    }


    @Override
    protected void onRestart() {
        super.onRestart();

        msg.clear();

        messageRetriverFromTheDatanbase();
        mainAdapter.notifyDataSetChanged();

    }


    // code for sending a message to the user after generating random one
    public void sendMessage(String message, String number) {

        SmsManager smsManager = SmsManager.getDefault();

        try {
            ArrayList<String> subdividemessage = smsManager.divideMessage(message);
//            System.out.println(subdividemessage.size());
           smsManager.sendMultipartTextMessage(number, null, subdividemessage, null, null);
        } catch (Exception er) {
            er.printStackTrace();
        }


    }


    class forBackGroundActivitiesOf implements  Runnable{

        String message;
        DB_HELPER dbHelper;
        ArrayList<MessageModelClass> msg;
        MESSAGE_ADAPTER mainAdapter;
        String phoneNumber = "";

        public forBackGroundActivitiesOf(String message, DB_HELPER dbHelper, ArrayList<MessageModelClass> msg, MESSAGE_ADAPTER mainAdapter) {
            this.message = message;
            this.dbHelper = dbHelper;
            this.msg = msg;
            this.mainAdapter = mainAdapter;
        }

        @Override
        public void run() {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String encrypted_message = ecrypterAndDecrypter.messageCiphereCaller(message);
                    dbHelper.InsertNewMessage(userId, defaultUserID, message, encrypted_message, new SimpleDateFormat("HH.mm").format(new Timestamp(System.currentTimeMillis())));

                    msg.add(new MessageModelClass("S",message));

                    mainAdapter.notifyItemInserted(msg.size()-1);

//                    msg.add(new MessageModelClass("R",encrypted_message));
//
//                    mainAdapter.notifyItemInserted(msg.size()-1);

                    messagePreparer(); // getting user phone number


                    String encyptedPhoneNumber = ecrypterAndDecrypter.messageCiphereCaller(phoneNumber);

                    String finalMessage = encrypted_message + "-PN-"+encyptedPhoneNumber+"--SM--";


//                   sendMessage(finalMessage,"0924041650");
                     sendMessage(finalMessage,phoneNumber);
                }
            });


        }

        private void messagePreparer(){
            // phoneNumber

            Cursor cursor = dbHelper.gettingSpecificUserDetailUsingId(userId);

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                this.phoneNumber = cursor.getString(1);

            }
        }

    }


}
