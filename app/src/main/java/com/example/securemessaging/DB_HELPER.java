package com.example.securemessaging;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DB_HELPER extends SQLiteOpenHelper {
    SharedPreferences MainInformationHolder = null;
    Context context;

    public DB_HELPER( Context context) {
        super(context, "Secure_Messaging.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

        DB.execSQL("CREATE TABLE user_Detail_Information (name TEXT , phoneNumber TEXT PRIMARY KEY)");
        DB.execSQL("CREATE TABLE chat_Room(reciverId INTEGER , senderId INTEGER, Decryptedmessage TEXT,EncryptedMessage TEXT, timeStamp TEXT)");

        MainInformationHolder = context.getSharedPreferences("Information",Context.MODE_PRIVATE);
        String PhoneNumber  = MainInformationHolder.getString("PhoneNumber",null);
        String Name = "User";

        if( PhoneNumber != null){
            INTIT(DB,Name, PhoneNumber);
        }else{
            Toast.makeText(context, "I AM not getting the phone Number Right", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user_Detail_Information");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS chat_Room");


        onCreate(sqLiteDatabase);
//        sqLiteDatabase.execSQL("CREATE TABLE user_Detail_Information (name TEXT , phoneNumber TEXT PRIMARY KEY)");
//        sqLiteDatabase.execSQL("CREATE TABLE chat_Room(reciverId INTEGER , senderId INTEGER, Decryptedmessage TEXT,EncryptedMessage TEXT, timeStamp TEXT)");
//

//        MainInformationHolder = context.getSharedPreferences("Information",Context.MODE_PRIVATE);
//        String PhoneNumber  = MainInformationHolder.getString("PhoneNumber",null);
//        String Name = "User";
//        if( PhoneNumber != null){
//            INTIT(sqLiteDatabase,Name, PhoneNumber);
//        }else{
//            Toast.makeText(context, "I AM not getting the phone Number Right", Toast.LENGTH_LONG).show();
//        }


    }

    public  boolean INTIT(SQLiteDatabase DB,String name, String phoneNumber){

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("phoneNumber",phoneNumber);

        long result = DB.insert("user_Detail_Information",null,contentValues);

        return result != -1; // returns true if inserted false if its not

    }


    // ---- New Contact Adding

    public  boolean addNewUserToMyChatList(String name, String phoneNumber){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("name", name);
        contentValues.put("phoneNumber",phoneNumber);

        long result = DB.insert("user_Detail_Information",null,contentValues);

        return result != -1; // returns true if inserted false if its not

    }

    // ---- message while chatting adding page

    public boolean InsertNewMessage(String ReciverId, String SenderId, String DecryptedMessage,String EncyptedMessage, String timestamp ){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("reciverId",ReciverId);
        contentValues.put("senderId", SenderId);
        contentValues.put("Decryptedmessage",DecryptedMessage);
        contentValues.put("EncryptedMessage",EncyptedMessage);
        contentValues.put("timeStamp", timestamp);

        long result = DB.insert("chat_Room",null,contentValues);


        return result != -1; // returns true if inserted false if its not

    }

// for getting all the contacts started having chat
    public Cursor gettingSpecificUserDetailUsingId(String Id){
        SQLiteDatabase DB =this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from user_Detail_Information where rowid='"+Id+"'",null);
        return cursor;
    }
    public Cursor getAllTheUser(){

        SQLiteDatabase DB =this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from user_Detail_Information",null);
        return cursor;

    }
    public Cursor getSpecificUserUsingPhoneNumber(String number){
        SQLiteDatabase DB =this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select rowid from user_Detail_Information where phoneNumber='"+number+"'",null);
        return cursor;
    }


//     for getting only some user from the contact list

    public Cursor getSpecificUser(String Name, String PhoneNumber){
        SQLiteDatabase DB =this.getWritableDatabase();
        return DB.rawQuery("select * from user_Detail_Information where name="+Name+" and phoneNumber="+PhoneNumber,null);
    }

    public Cursor getChatOfAllData(){
        SQLiteDatabase DB =this.getWritableDatabase();
        return DB.rawQuery("select rowid,reciverId,senderId,Decryptedmessage,timeStamp from chat_Room ",null);

    }


//    getting chat with specific Person
    public Cursor getChatWithSpecificUser(String ReciverID, String Sendid){
        SQLiteDatabase DB =this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from chat_Room WHERE (reciverId='"+ReciverID+"' and  senderId='"+Sendid+"') or (reciverId='"+Sendid+"' and senderId='"+ReciverID+"')"  ,null);
        return cursor;
    }




    public Cursor getIdOFUser(String name, String phonenumber){
        SQLiteDatabase DB =this.getWritableDatabase();
        return DB.rawQuery("select rowid from user_Detail_Information where name='"+name+"' and phoneNumber='"+phonenumber+"'",null);

    }


    public int message_Thread_DeleterOnSwipe(String Userid,String defaultUserId){

        // first delete from contact list
        // then from chat room

        SQLiteDatabase db =this.getWritableDatabase();

        int result = db.delete("chat_Room","reciverId = ? or senderId = ?" , new String[]{Userid,Userid});

        if(result > 0)
            result = db.delete("user_Detail_Information","rowid= ?",new String[]{Userid});


        return  result;
    }








}
