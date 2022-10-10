package com.example.securemessaging;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PermmissionCheckingPage extends AppCompatActivity {
    Button checkAgain ;
    SharedPreferences mainPreferences = null;
    private static final int PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permmission_checking_page);


        checkAgain = (Button) findViewById(R.id.check_again);
        mainPreferences = getApplication().getSharedPreferences("Verification", Context.MODE_PRIVATE);



        checkAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckingPermissionIsEnabledOrNot()) {
                    if(mainPreferences.getString("PHONEVERFICATION","-1").equals("-1")){
                        startActivity(new Intent(PermmissionCheckingPage.this, PhoneNumberInsertingVerificationNumber.class));
                    }
                }else{
                    RequestMultiplePermission();
                    Toast.makeText(PermmissionCheckingPage.this, "You Need To Check  The Permission First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    // request the permision
    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(PermmissionCheckingPage.this, new String[]
                {
                        RECEIVE_SMS,
                        READ_CONTACTS,
                        SEND_SMS,
                }, PERMISSION_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        switch (requestCode) {

            case PERMISSION_REQUEST:

                if (grantResults.length > 0) {

                    boolean receive_permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_contacts = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean send_sms = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (receive_permission && read_contacts && send_sms) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();

                        mainPreferences.edit().putString("PHONEVERFICATION","-1").apply();
                        startActivity(new Intent(PermmissionCheckingPage.this,PermmissionCheckingPage.class));

//                        if (!CheckingPermissionIsEnabledOrNot()){
//                            RequestMultiplePermission();
//                        }
//                        if(numberOfPermmissionRequest < 2){
//                            Toast.makeText(getApplicationContext(), "In order this App to work Those permission are needs to be granted! Try Again", Toast.LENGTH_SHORT).show();

//                        }else{
                        // say good bye close the app
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                finishAffinity();
//                            } else {
//                                finish();
//                            }

//                          }

                    }
                }

                break;
        }
    }




}