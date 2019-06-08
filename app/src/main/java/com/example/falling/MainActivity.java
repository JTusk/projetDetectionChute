package com.example.falling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
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

    public static String numToCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch simpleSwitch = findViewById(R.id.switch1);

        //SharedPreferences sharedPref = getSharedPreferences("Parameter",Context.MODE_PRIVATE);
        //ParameterActivity.numToCall = sharedPref.getString("numToCall","test");
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        this.numToCall = pref.getString("numToCall","numero");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            Intent intent = new Intent(getApplicationContext(),Detector.class);
            startService(intent);
        }
        /*simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    Intent intent = new Intent(getApplicationContext(),Detector.class);
                    startService(intent);
                } else {
                    finish();
                }
            }
        });*/
    }

    @Override
    public void onClick(View v){

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_parametre:
                Intent aboutIntent = new Intent(MainActivity.this, ParameterActivity.class);
                startActivity(aboutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        getPreferences();
        super .onResume();
    }

    @Override
    protected void onPause(){
        super .onPause();
    }

    @Override
    protected void onDestroy() {
        getPreferences();
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("numToCall", numToCall);
        editor.commit();
        super.onDestroy();
    }

    private void getPreferences(){
        SharedPreferences sharedPref = getSharedPreferences("Parameter",Context.MODE_PRIVATE);
        ParameterActivity.numToCall = sharedPref.getString("numToCall","null");
        this.numToCall = sharedPref.getString("numToCall","numero inconnu");
    }
}
