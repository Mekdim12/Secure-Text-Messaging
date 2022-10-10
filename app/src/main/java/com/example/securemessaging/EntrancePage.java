package com.example.securemessaging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class EntrancePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance_page);


          Runnable re = new Runnable() {
              @Override
              public void run() {
                  try{

                      Thread.sleep(1000);

                  }catch (Exception e){

                  }
                  SharedPreferences mainPreferences = getApplication().getSharedPreferences("Verification",Context.MODE_PRIVATE);


                  if(mainPreferences.getString("status","-1").equals("-1")){
//                    The user is not verified yet so check

                      Intent inte = new Intent(EntrancePage.this, PhoneNumberInsertingVerificationNumber.class);
                      startActivity(inte);
                  }else{
//                    Verified so to the main page

                      Intent inte = new Intent(EntrancePage.this, MainActivity.class);
                      startActivity(inte);
                  }
              }
          };

        Thread th= new Thread(re);
        th.start();



    }


}