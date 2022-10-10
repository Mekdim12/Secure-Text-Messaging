package com.example.securemessaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.ArrayList;

public class NewContactChoosingPage extends AppCompatActivity   implements  RecyclerViewInterface{

    RecyclerView recyclerView;
    ContactsAdapter adapter;
    ImageView addContactsButton,backbutton2;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    private Dialog dialog;
    Button Okay,Cancel;
    EditText name_, phonenumber;
    DB_HELPER db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_choosing_page);

        recyclerView = findViewById(R.id.recyclerviewforcontacts);
        addContactsButton = findViewById(R.id.addNewNumber);

        db_helper = new DB_HELPER(this);
        backbutton2 = findViewById(R.id.backbutton2);

        dialog = new Dialog(this);



        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setContentView(R.layout.dialoge_add_new_number_not_in_contact);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_for_custome_dialoge));
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false); //Optional
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                Okay = dialog.findViewById(R.id.btn_okay);
                Cancel = dialog.findViewById(R.id.btn_cancel);
                name_ = (EditText) dialog.findViewById(R.id.nameOfUser);
                phonenumber = (EditText)  dialog.findViewById(R.id.PhoneNumebr);

                Okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nameofNewContact = name_.getText().toString().trim();
                        String  phonenumber_ = phonenumber.getText().toString().trim();

                        if(db_helper.addNewUserToMyChatList(nameofNewContact, phonenumber_)){
                            ContactModel model = new ContactModel();
                            model.setName(nameofNewContact);
                            model.setNumber(phonenumber_);
                            arrayList.add(model);

                            adapter.notifyItemInserted(arrayList.size()-1);
                            Toast.makeText(NewContactChoosingPage.this, "Contact Successfully Added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            Intent intent = new Intent(NewContactChoosingPage.this, MainMessage.class);
                            intent.putExtra("Flag","NewContacts");

                            intent.putExtra("Name",nameofNewContact);
                            intent.putExtra("Number",phonenumber_);

                            startActivity(intent);


                        }else{
                            Toast.makeText(NewContactChoosingPage.this, "The user is might be already there!", Toast.LENGTH_LONG).show();

                        }


                    }
                });
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });
        getContactList();

        backbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(NewContactChoosingPage.this, MainActivity.class);
                startActivity(inte);
            }
        });






    }

    public void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        // ASCENDING ORDER
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" COLLATE LOCALIZED ASC";

        Cursor cursor = getContentResolver().query(
                uri, null, null, null, sort
        );

        if( cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndexOrThrow ( ContactsContract.Contacts._ID ));
                String name = cursor.getString( cursor.getColumnIndexOrThrow( ContactsContract.Contacts.DISPLAY_NAME ));

                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?";

                Cursor phoneCursor = getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);

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




        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactsAdapter(this, arrayList, this);

        recyclerView.setAdapter(adapter);




    }

    @Override
    public void onItemClick(int postition) {

        Intent intent = new Intent(NewContactChoosingPage.this, MainMessage.class);
        intent.putExtra("Flag","NewContacts");

        intent.putExtra("Name",arrayList.get(postition).name);
        intent.putExtra("Number",arrayList.get(postition).number);

        startActivity(intent);

        }


    }
