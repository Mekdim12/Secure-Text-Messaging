package com.example.securemessaging;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





import java.util.ArrayList;
import java.util.Random;

public class PhoneNumberInsertingVerificationNumber extends AppCompatActivity {
    TextView Register_the_phone_number;
    EditText number_Field;
    int Number_for_verfying = 0;
    private int numberOfPermmissionRequest = 0;
    SharedPreferences sharedP = null;
    SharedPreferences MainInformationHolder = null;
    private static final int PERMISSION_REQUEST = 1;
    SharedPreferences mainPreferences = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_inserting_verification_number);
        sharedP = getApplicationContext().getSharedPreferences("VeficationNumber", Context.MODE_PRIVATE);

        mainPreferences = getApplicationContext().getSharedPreferences("Verification", Context.MODE_PRIVATE);
        MainInformationHolder = getApplication().getSharedPreferences("Information",Context.MODE_PRIVATE);



        if(mainPreferences.getString("status","-1").equals("-1")){
//                    The user is note verified yet so DISPLAY

            if(CheckingPermissionIsEnabledOrNot()) {
                // u need to comment this one later << toast

                Register_the_phone_number = (TextView)findViewById(R.id.registerNumber);
                number_Field = (EditText)findViewById(R.id.numberField);




                Register_the_phone_number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number = number_Field.getText().toString();

                        if(number.trim().charAt(0)=='9' && number.trim().length() == 9){
                            String final_number_for_send = "+251"+number.trim();

                            String verficationText = Verification_Genertator();

                            sharedP.edit().putString("Verify",Number_for_verfying+"").apply();
                            sharedP.edit().putString("PhoneNumber",final_number_for_send).apply();


                            sendMessage(verficationText,final_number_for_send);

                        }else{
                            Toast.makeText(getApplicationContext(),"Please Make Sure You Enter In The Write Format!",Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }else {
                Toast.makeText(PhoneNumberInsertingVerificationNumber.this, "First", Toast.LENGTH_SHORT).show();

//                RequestMultiplePermission();
                mainPreferences.edit().putString("PHONEVERFICATION","-1").apply();
                startActivity(new Intent(PhoneNumberInsertingVerificationNumber.this,PermmissionCheckingPage.class));

            }

        }else{
//                    Verified so to the main page
            if(CheckingPermissionIsEnabledOrNot()) {

                mainPreferences.edit().putString("status","XXXXXXXXVerifiedXXXXXXX").apply();
                Toast.makeText(PhoneNumberInsertingVerificationNumber.this, "All Permissions Granted Successfully", Toast.LENGTH_SHORT).show();
                Intent inte = new Intent(PhoneNumberInsertingVerificationNumber.this, MainActivity.class);
                startActivity(inte);


            }else {
                Toast.makeText(PhoneNumberInsertingVerificationNumber.this, "Second", Toast.LENGTH_SHORT).show();

//                RequestMultiplePermission();
                mainPreferences.edit().putString("PHONEVERFICATION","-1").apply();
                startActivity(new Intent(PhoneNumberInsertingVerificationNumber.this,PermmissionCheckingPage.class));

            }


        }
    }




// code for sending a message to the user after generating random one
    public void sendMessage(String message, String number) {
    SmsManager smsManager = SmsManager.getDefault();

    try {
        ArrayList<String> subdividemessage = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(number, null, subdividemessage, null, null);
    } catch (Exception er) {
        er.printStackTrace();
    }


}

// code for checkig a permission is granted or not

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    // Hulawerk ==> 0911019209


    // --- fro genreating the random number along with the text that's going to be send to the user number
    public String Verification_Genertator(){
        Random rand = new Random();
        int random_number = -1;
        while(true) {
            int num = rand.nextInt(999999999);
            if ((num+"").length()>5) {
                random_number  = num;
                break;
            }
        }
        String text ="MH-"+ random_number+"- Is Verification Code For Messaging Application Copy And Paste it unless it pasted it as this text arrives!";
        Number_for_verfying = random_number;
        return text;
    }






}