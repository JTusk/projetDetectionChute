package com.example.falling;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

import java.net.URISyntaxException;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

public class Detector extends Service implements SensorEventListener, View.OnClickListener {

    private static final String TAG = "Service_Detector";
    private float last_accelerometre_values[];
    private final static int INTERVAL = 100;
    private long lastUpdate = -1;
    private long nbChutes= 0;
    private long nb=0;
    private Handler myHandler;
    public final static String SMSREQUEST = "request";


    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // Code à éxécuter de façon périodique
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("0648630421", null, "Relance suite à la chute", null, null);
            nb++;
            Toast.makeText(getApplicationContext(), "Relance ",
                    Toast.LENGTH_LONG).show();
            myHandler.postDelayed(this,500000);

            if (nb>3){
                myHandler.removeCallbacks(myRunnable);

            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public void onCreate(){

        Toast.makeText(this, "Invoke background service onCreate method.", Toast.LENGTH_LONG).show();

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){

            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_FASTEST);

        }else{
            Toast.makeText(getApplicationContext(), "Aucun accelerométre présent sur ce téléphone", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Aucun accelerométre présent sur ce téléphone");
        }
        super.onCreate();
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "Invoke background service onDestroy method.", Toast.LENGTH_LONG).show();
        /*try {
            stopService(getIntentOld(SMSREQUEST));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if((currentTime - lastUpdate) > INTERVAL){
                lastUpdate = currentTime;
                float[] accelerometre_values = event.values.clone();
                double calcul = Math.sqrt(accelerometre_values[0]*accelerometre_values[0] + accelerometre_values[1]*accelerometre_values[1] + accelerometre_values[2]*accelerometre_values[2]);

                if(calcul>20){
                    Toast.makeText(this, Double.toString(calcul), Toast.LENGTH_SHORT).show();

                    try {


                        myHandler = new Handler();
                        myHandler.postDelayed(myRunnable,500000); // on redemande toute les 500ms

                        if(nbChutes<1){
                            //String name = intent.getStringExtra("SMS",null);
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage("0648630421", null, "Premiere chute", null, null);
                            Toast.makeText(getApplicationContext(), "CHUTE 1!",
                                    Toast.LENGTH_LONG).show();
                            nbChutes++;
                        }


                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                //Log.d(TAG, Double.toString(calcul));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {

    }
}
