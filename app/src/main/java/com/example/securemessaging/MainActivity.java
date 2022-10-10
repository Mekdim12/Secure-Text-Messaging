package com.example.securemessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{


    private static boolean isInterestingActivityVisible;


    RecyclerView mrecyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> userList;
    DemoAdapter adapter;
    FloatingActionButton actionButton;
    private Dialog dialog;
    Button Okay,Cancel;
    DB_HELPER dbHelper;
    SharedPreferences MainInformationHolder = null;
    private String defaultUserID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout_resource_file);
        actionButton = findViewById(R.id.floatingAddButton);
        dialog = new Dialog(this);
        dbHelper = new DB_HELPER(this);
        userList = new ArrayList<>();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent contactsPageIntent = new Intent(MainActivity.this, NewContactChoosingPage.class);
                startActivity(contactsPageIntent);

            }
        });

        MainInformationHolder = getApplicationContext().getSharedPreferences("Information", Context.MODE_PRIVATE);
        String PhoneNumber  = MainInformationHolder.getString("PhoneNumber",null);


        if(PhoneNumber != null) {

            Cursor DefaultUserCursor = dbHelper.getIdOFUser("User", PhoneNumber);

            if (DefaultUserCursor.getCount() > 0) {
                DefaultUserCursor.moveToFirst();
                defaultUserID = DefaultUserCursor.getString(0);



                initRecyclerView();

                ListOfChatsFetcher();

            }else {
                Toast.makeText(this, "There is something wrong with Setting up the default Phone Number", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "There is something wrong with Setting up the default Phone Number", Toast.LENGTH_SHORT).show();
        }


    }

    private void initRecyclerView() {
        mrecyclerView=findViewById(R.id.RecyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(layoutManager);
        adapter=new DemoAdapter(userList, this);
        mrecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                dialog.setContentView(R.layout.cutsome_dialoge);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_for_custome_dialoge));
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false); //Optional
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                Okay = dialog.findViewById(R.id.btn_okay);
                Cancel = dialog.findViewById(R.id.btn_cancel);

                Okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        // also delete it from the database


                        ModelClass prevData = userList.remove(viewHolder.getAdapterPosition());
                        String id = prevData.getId();

                        if(dbHelper.message_Thread_DeleterOnSwipe(id , defaultUserID) > 0){
                            adapter.notifyDataSetChanged();
                        }// otherwise the data has some error while deleting

                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        }).attachToRecyclerView(mrecyclerView);




    }
    private void initData() {


//        userList.add(new ModelClass(R.drawable.pic0,"Anjali","How are you?","10:45 am","_______________________________________"));
//
//        userList.add(new ModelClass(R.drawable.pic1,"Brijesh","I am fine","15:08 pm","_______________________________________"));
//
//        userList.add(new ModelClass(R.drawable.p,"Sam","You Know?","1:02 am","_______________________________________"));
//
//        userList.add(new ModelClass(R.drawable.pic2,"Divya","How are you?","12:55 pm","_______________________________________"));
//
//        userList.add(new ModelClass(R.drawable.pic0,"Anjali","How are you?","10:45 am","_______________________________________"));
//
//        userList.add(new ModelClass(R.drawable.pic1,"Brijesh","I am fine","15:08 pm","_______________________________________"));
//
//        userList.add(new ModelClass(R.drawable.p,"Sam","You Know?","1:02 am","_______________________________________"));
//
//        userList.add(new ModelClass(R.drawable.pic2,"Divya","How are you?","12:55 pm","_______________________________________"));


    }


    public void ListOfChatsFetcher() {

        Cursor allChats = dbHelper.getChatOfAllData();

        ArrayList<String> id_ofChatts = new ArrayList<>();

        if (allChats.getCount() > 0) {

            while (allChats.moveToNext()) {

                String rowID = allChats.getString(0);
                String reciverID = allChats.getString(1);
                String SenderId = allChats.getString(2);
                String decryptedMessage = allChats.getString(3);
                String timeStamp = allChats.getString(4);

              // aa bb bb aa  s=aa r=bb
                if(SenderId.trim().equals(defaultUserID.trim())){
                    if(!(id_ofChatts.contains(reciverID))){
                        id_ofChatts.add(reciverID);
                    }
                }else{
                    if(!(id_ofChatts.contains(SenderId))){
                        id_ofChatts.add(SenderId);
                    }
                }

            }


            contactInformation(id_ofChatts);


        }else{
            // it mean there is no chat started with no body else so show something no message thread or something like hat
        }



    }



    public void contactInformation(ArrayList<String> listofChattes){


        userList.clear();
        for(int i=listofChattes.size()-1; i>=0; i--){
            String ids = listofChattes.get(i);

            Cursor contactDetail = dbHelper.gettingSpecificUserDetailUsingId(ids);

            if(contactDetail.getCount() > 0){
                    contactDetail.moveToFirst();
                    // get basic information of the contact
                    String name = contactDetail.getString(0);
                    String phonenumber =contactDetail.getString(1);
                    String messageAndTime = last_Message_and_timeRetriver(ids);

                    String [] splitted= messageAndTime.split("###");
                    String lastMessage = splitted[0];
                    String time = splitted[1];


                    userList.add(new ModelClass(R.drawable.account,name,lastMessage,time,ids,"__________________________________________"));
                    adapter.notifyItemInserted(userList.size()-1);



            }else{
                Toast.makeText(this, "There is problem with the fetching from database", Toast.LENGTH_SHORT).show();
            }

        }


    }


    public String last_Message_and_timeRetriver(String id){


        Cursor messages = dbHelper.getChatWithSpecificUser(defaultUserID, id);

        if(messages.getCount() > 0){

            if(messages.moveToLast()){
                String lastMessage_ = messages.getString(2);
                String time_= messages.getString(4);
                return lastMessage_ +"###"+time_;

            }
        }

        return null;

    }
    @Override
    public void onItemClick(int postition) {
        Intent intent = new Intent(MainActivity.this,MainMessage.class);
        intent.putExtra("Flag","FromMainView");
        intent.putExtra("ID",userList.get(postition).getId());
        startActivity(intent);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        ListOfChatsFetcher();
        adapter.notifyDataSetChanged();

    }
}