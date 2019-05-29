package com.example.falling;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class Detector extends Service implements SensorEventListener {

    private static final String TAG = "Service_Detector";
    private float last_accelerometre_values[];
    private final static int INTERVAL = 100;
    private long lastUpdate = -1;

    public Detector() {

    }

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
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);

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
                //double calcul = Math.sqrt(accelerometre_values[0]*accelerometre_values[0] + accelerometre_values[1]*accelerometre_values[1] + accelerometre_values[2]*accelerometre_values[2]);

                if(accelerometre_values[2]>14){
                    Toast.makeText(this, Double.toString(accelerometre_values[2]), Toast.LENGTH_SHORT).show();
                    Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:0648630421"));
                    sms.putExtra("sms_body", "Wooooohhhh si t'as ce sms...ça marche bro' !");
                    sms.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(sms);

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("0648630421", null, "test", null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
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
}
