package com.example.securemessaging;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SmsReciver extends BroadcastReceiver  {
    public SharedPreferences sharedP =null;
    MessageEncryptionLogicLayerFinalVersion2 ecrypterAndDecrypter;
    SharedPreferences MainInformationHolder = null;
    @Override
    public void onReceive(Context context, Intent intent) {

        ecrypterAndDecrypter = new MessageEncryptionLogicLayerFinalVersion2();

        sharedP =context.getSharedPreferences("MessagRecived",Context.MODE_PRIVATE);
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                // getting sms  object
                Object [] pdus = (Object[]) bundle.get("pdus");
                if(pdus.length == 0){
                    return;
                }
                // large message might be broken into many
                SmsMessage [] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();

                for(int i=0; i<pdus.length ;i++){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String Format  =bundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i],Format);
                        sb.append(messages[i].getMessageBody());
                    }else{
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            sb.append(messages[i].getMessageBody());
                    }

                }
                String sender = messages[0].getOriginatingAddress();
//                System.out.println(messages[0]..getServiceCenterAddress());
                System.out.println("-------");
                System.out.println(sender);

                String Messagge = sb.toString();// this the last message r
                if(Messagge.startsWith("MH-")){

                        fileWriter(Messagge, context);
                        Intent ss = new Intent();
                        ss.setClass(context,VerificationLandingPage.class);
                        ss.putExtra("Message",Messagge);
                        ss.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        (context).startActivity(ss);
                }else{
                    if(Messagge.length() > 6 ){
                        if(Messagge.contains("--SM--")){

                            Thread thread = new Thread(new For_DoingThe_DecryptionStuff(Messagge, context,sender) );
                            thread.start();


                         // Done   // now decrypt the messages and  the phone number if the phone number is n't mathc with the current saved
                         // Done   // if so no point of saving the message to db just ignore it
                            // if valid authorization made then feed the data to the data ans update the recycler views
                            // do this task in different thread

                        }// if this conditions are successfull its not a message for this app

                    }
                }



            }
        }
    }

    public void fileWriter(String data,Context cont){

        String arrays [] = data.split("-");
        String verfiCode = arrays[1];
        try{
            FileOutputStream fileoutput = cont.openFileOutput("Verify.txt",Context.MODE_PRIVATE);
            fileoutput.write(verfiCode.getBytes());
            fileoutput.flush();
            fileoutput.close();
        }catch(IOException er){
            Log.e("ONReciver===",er+"");
        }


    }

    class For_DoingThe_DecryptionStuff implements  Runnable{

        String Messagge, sender;
        Context context;
        ArrayList<ContactModel> arrayList = new ArrayList<>();


        public For_DoingThe_DecryptionStuff(String messagge, Context context, String sender) {
            this.Messagge = messagge;
            this.context = context;
            this.sender = sender;
        }


        @Override
        public void run() {

            ContextCompat.getMainExecutor(context).execute(()  -> {
                        // This is where your UI code goes.

            String [] forCheckingEncryptedMessage = Messagge.split ("--SM--");
            Messagge = forCheckingEncryptedMessage[0]; // message except the flag chcke
            String [] forHavingPhoneNumberAndMessage  = Messagge.split("-PN-");

            Messagge = forHavingPhoneNumberAndMessage[0]; // ciphered Text

            String PhoneNumber = forHavingPhoneNumberAndMessage[1]; // phone Number

            MainInformationHolder = context.getSharedPreferences("Information", Context.MODE_PRIVATE);
            String  RegisteredPhoneNumber  = MainInformationHolder.getString("PhoneNumber",null);



            String DecrytedMessage = ecrypterAndDecrypter.messageDeciphering(Messagge);
            PhoneNumber = ecrypterAndDecrypter.messageDeciphering(PhoneNumber);

            PhoneNumber = getFeasbileContact(PhoneNumber);
            RegisteredPhoneNumber = getFeasbileContact(RegisteredPhoneNumber);

            System.out.println(PhoneNumber);
            System.out.println(DecrytedMessage);
            System.out.println(RegisteredPhoneNumber);

//             PhoneNumber = "+251924041650"; // this for just testing delete this later on

                if(PhoneNumber.trim().startsWith("+251")){

                        PhoneNumber = PhoneNumber.substring("+251".length());

                }else if(PhoneNumber.trim().startsWith("09")){

                        PhoneNumber = PhoneNumber.substring("0".length());

                }// if anything beyond this expression u need to check the regular expression solution

                if(RegisteredPhoneNumber.trim().startsWith("+251")){

                    RegisteredPhoneNumber = RegisteredPhoneNumber.substring("+251".length());

                }else if(RegisteredPhoneNumber.trim().startsWith("09")){

                    RegisteredPhoneNumber = RegisteredPhoneNumber.substring("0".length());

                }// if anything beyond this expression u need to check the regular expression solution





                System.out.println(PhoneNumber);

                System.out.println(RegisteredPhoneNumber);



                if(PhoneNumber.trim().equals(RegisteredPhoneNumber.trim())) {

                    System.out.println("Authenticated****************");

                // it means its authenticated or message got the right receiver

                // so write the message on the dabatabse
                // in order this to work u need to get bot users id the one who is reciveing and the one who os sending id  so create fucntion

                String ReciverIdDefault = phoneNumberIdfetcher("+251"+RegisteredPhoneNumber);
                String senderIdFromTheMessage = phoneNumberIdfetcher(sender);

                System.out.println("------------>11");
                System.out.println(ReciverIdDefault);
                System.out.println(senderIdFromTheMessage);

                DB_HELPER dbHelper = new DB_HELPER(context);


                if(senderIdFromTheMessage.trim().equals("")){

                    System.out.println("Sender is not found in db so its means new user ");
                    // this means its new user trying to send new message to this user so no record on the database so create for him one



                    // check contacts user name from save contacts
                    getContactList();
                    String SenderName = sender; // initalize with sender phone Number cuz if the name didn't found save with phoneNumber

                    for (ContactModel model : arrayList) {
                        if(model.getNumber().trim().startsWith("+251")){

                            if(model.getNumber().equals(sender)){
                                SenderName = model.getName();
                                break;
                            }

                        }else if(model.getNumber().trim().startsWith("09")){

                            if(model.getNumber().equals(sender)){
                                SenderName = model.getName();
                                break;
                            }

                        }



                    }

                    System.out.println("After getting--> "+sender);

                    if(! dbHelper.addNewUserToMyChatList(SenderName , sender)){
                        System.out.println("----------------->>>>>>>>>>>>>>CAN'T CREATE A NEW COTAACT -<<<<<<<<<<<<------------- TBH IS SHOULD'TBE HERE");
                        return;
                    }else{
                        System.out.println("Successfully created====");
                    }



                }


                // it means that user is already had a chat with this person or registerd new sender with the name and phonenumber of his PhoneNumber

                ReciverIdDefault = phoneNumberIdfetcher("+251"+RegisteredPhoneNumber);
                senderIdFromTheMessage = phoneNumberIdfetcher(sender);

                System.out.println("Getting finally");

                System.out.println(ReciverIdDefault);
                System.out.println(senderIdFromTheMessage);

                // now u have the ids of both users so save it to the database

               if(dbHelper.InsertNewMessage(ReciverIdDefault, senderIdFromTheMessage,DecrytedMessage ,Messagge,new SimpleDateFormat("HH.mm").format(new Timestamp(System.currentTimeMillis())))){
                   System.out.println("Message Created Successfully");
               }else{
                   System.out.println("Not Succssfull");
               }


            }

          });
        }


        public String getFeasbileContact(String number){
            char [] arr = number.toCharArray();
            number = "";
            for(char num: arr){
               if(String.valueOf(num).trim().equals("")){
                   continue;
               }
               number += String.valueOf(num);
            }
            return  number;
        }
        public void getContactList() {
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            // ASCENDING ORDER
            String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" COLLATE LOCALIZED ASC";

            Cursor cursor = context.getContentResolver().query(
                    uri, null, null, null, sort
            );

            if( cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String id = cursor.getString(cursor.getColumnIndexOrThrow ( ContactsContract.Contacts._ID ));
                    String name = cursor.getString( cursor.getColumnIndexOrThrow( ContactsContract.Contacts.DISPLAY_NAME ));

                    Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                    String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?";

                    Cursor phoneCursor =  context.getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);

                    if(phoneCursor.moveToNext()){
                        String number = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        ContactModel model = new ContactModel();
                        model.setName(name);
                        model.setNumber(number);
                        arrayList.add(model);
                        phoneCursor.close();
                    }

                }

                cursor.close();
            }


        }

        public String phoneNumberIdfetcher(String number){

            System.out.println(number);

            DB_HELPER dbHelper = new DB_HELPER(context);
            Cursor cursor = dbHelper.getSpecificUserUsingPhoneNumber(number);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                return  cursor.getString(0);
            }
            System.out.println("----------------->><<<<<>>><<<>>><<<>><<<>><<<>>><<>>>");

            return  "";
        }

    }
}


