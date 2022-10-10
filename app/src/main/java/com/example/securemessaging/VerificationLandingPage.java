package com.example.securemessaging;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class VerificationLandingPage extends AppCompatActivity {
    TextView verifyButton,SendCodeAgain;
    EditText NumberField;
    String number_Generated,PhoneNumber;
    SharedPreferences MainInformationHolder = null;
    SharedPreferences mainPreferences = null;
    SharedPreferences sharedp;
    String currentValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_landing_page);
        verifyButton = (TextView)findViewById(R.id.verifybutton);
        SendCodeAgain =(TextView)findViewById(R.id.SendCodeAgain);
        NumberField =  (EditText)findViewById(R.id.VerifyNumber);

        sharedp = getApplicationContext().getSharedPreferences("VeficationNumber", Context.MODE_PRIVATE);
        mainPreferences = getApplicationContext().getSharedPreferences("Verification", Context.MODE_PRIVATE);
        MainInformationHolder = getApplication().getSharedPreferences("Information",Context.MODE_PRIVATE);

        String result = sharedp.getString("Verify","-1");
        String PhoneNumber = sharedp.getString("PhoneNumber","-1");



        Intent inete = getIntent();
        String mes= inete.getStringExtra("Message");


        String number_from_message = MessageDivider(mes);

        NumberField.setText(number_from_message);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!result.equals("-1")){
                    if(result.equals(number_from_message)){


                        mainPreferences.edit().putString("status","XXXXXXVerifiedXXXXXX").apply();

                        mainPreferences.edit().putString("PHONEVERFICATION","10").apply();

                        MainInformationHolder.edit().putString("PhoneNumber",PhoneNumber).apply(); // setting up the phonenumber for later user as this app default message user

                        Intent intr =  new Intent(VerificationLandingPage.this, MainActivity.class);
                        startActivity(intr);

                    }
                }

            }
        });


        SendCodeAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String only_verifcation_number_send = chekerIfMessageVerficationIsArrived();
                NumberField.setText(only_verifcation_number_send);
                if(number_Generated.trim().equals(only_verifcation_number_send.trim())){
                    sharedp.edit().putString("Number",PhoneNumber).apply();
                    String phonNum= sharedp.getString("PhoneNumber","-1");
                    if(!phonNum.equals("-1")){
                        Toast.makeText(getApplicationContext(), " Definetley Minus One", Toast.LENGTH_LONG).show();
                        Intent inten =new Intent(VerificationLandingPage.this,MainActivity.class);
                        inten.putExtra("PhoneNumber",phonNum);
                        startActivity(inten);

                    }else{
                        Toast.makeText(getApplicationContext(), "Not Definetley Minus One", Toast.LENGTH_LONG).show();

                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Make Sure You Enter The Right Verification Number",Toast.LENGTH_LONG).show();
                    // it means the code is not the same
                }

            }
        } );


    }

    private String MessageDivider(String message){
        String divided [] = message.split("-");
        return divided[1].trim();
    }

    private String chekerIfMessageVerficationIsArrived(){

        Handler handler = new Handler();
        Thread thread = new Thread(){
            @Override
            public void run() {
                String value = "";
                try{
                    while(true) {
                        value =   readFile();
                        if(value != null) {
                            if (value.trim().equals(currentValue.trim())) {
                                break;
                            }
                        }
                        Thread.sleep(2500);
                        handler.post(this);
                    }
                }catch (InterruptedException er){
                    Log.e("er",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }

            }
        };
        thread.start();
        //   Log.e("fine if its not found","-------"+value_[0]+"--------");

        return "";

    }

    public String readFile(){
        String lines = "";
        StringBuffer holder = null;
        try{
            FileInputStream fileinput = openFileInput("Verify.txt");
            InputStreamReader inputstrea = new InputStreamReader(fileinput);
            BufferedReader strinBuffer = new BufferedReader(inputstrea);
            StringBuffer buffer = new StringBuffer();

            while((lines = strinBuffer.readLine()) != null){
                buffer.append(lines+"\n");
            }

            holder = buffer;

        }catch(FileNotFoundException er ){
            Log.e("filenotfoundeeeeeee",er+"");

        }catch(IOException err){
            Log.e("ioerrrrrrrrr",err+"");
        }
        Log.e("<-->","This me after rading file correctly holding"+holder.toString());
        return  holder.toString();
    }


}




