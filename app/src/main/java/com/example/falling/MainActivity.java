package com.example.falling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int MY_PERMISSIONS_REQUEST_CALL_PHONE = 123;
    private final static int MY_PERMISSIONS_REQUEST_SEND_SMS = 124;
    public final static String SMSREQUEST = "request";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        final EditText testSMS= (EditText) findViewById(R.id.editTextSMS);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }


        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    Intent intent = new Intent(getApplicationContext(),Detector.class);
                    intent.putExtra(SMSREQUEST,testSMS.toString());
                    startService(intent);
                } else {
                    finish();
                }
            }
        });


    }

    @Override
    public void onClick(View v){

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //Cas ou on viebnt de modifier l'autorisation des appels
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getApplicationContext(), Detector.class);
                    startService(intent);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    break;
                }
            }

            //Cas ou on viebnt de modifier l'autorisation des SMS
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "SMS !",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Detector.class);
                    startService(intent);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume(){
        super .onResume();
    }

    @Override
    protected void onPause(){
        super .onPause();
    }
}
