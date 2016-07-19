package com.example.jungmo.WearSensor;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private SensorManager sensorManager;

    SensorEventListener pedoL;
    Sensor pedoSensor;
    Sensor oriSensor;
    Sensor accSensor;
    Sensor heartSensor;
    SensorEventListener accL;
    SensorEventListener oriL;
    SensorEventListener heartL;

    private Button btn_start;
    private Button btn_stop;

    private EditText ori;
    private EditText acc;

    private EditText heart;

    dbHelper helper;
    SQLiteDatabase db;
    int id=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);

        helper = new dbHelper(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = helper.getReadableDatabase();
        }

        heart = (EditText) findViewById(R.id.heartTEXT);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        acc = (EditText) findViewById(R.id.acc);
        ori = (EditText) findViewById(R.id.ori);

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

        id = getIntent().getIntExtra("ID", 0);
        Cursor query = db.rawQuery("SELECT * FROM \"sensor\" WHERE _id=" + id, null);
        query.moveToFirst();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        oriSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);    // 방향 센서
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);    // 가속도 센서
        heartSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        pedoSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        oriL = new oriListener();        // 방향 센서 리스너 인스턴스
        accL = new accListener();       // 가속도 센서 리스너 인스턴스
        heartL = new heartListener();
        pedoL = new pedoListener();

        if(heartSensor == null)
        {
            Log.i("SENSOR", "Heart rate sensor is disable.");
        }
    }

    public void onClick(View v){
        if(v == btn_start) {
            Toast toast = Toast.makeText(this, "Start", Toast.LENGTH_SHORT);
            toast.show();
            onResume();
        }
        else if(v == btn_stop)
        {
            Toast toast = Toast.makeText(this, "Stop", Toast.LENGTH_SHORT);
            toast.show();
            onPause();
        }
    }

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        sensorManager.registerListener(oriL, oriSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accL, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(heartL, heartSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(pedoL, pedoSensor, SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();

    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {

        sensorManager.unregisterListener(oriL);    // unregister acceleration listener
        sensorManager.unregisterListener(accL);    // unregister orientation listener
        sensorManager.unregisterListener(heartL);    // unregister orientation listener
        sensorManager.unregisterListener(pedoL);
        super.onPause();

    }

    private class pedoListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {  // 가속도 센서 값이 바뀔때마다 호출됨

            //Log.i("SENSOR", "Acceleration changed.");
            //Log.i("SENSOR", "  Acceleration X: " + event.values[0]
            //        + ", Acceleration Y: " + event.values[1]
            //        + ", Acceleration Z: " + event.values[2]);
            String val = new String();
            val = "pedo: " + String.valueOf(event.values[0]).substring(0,3);
            Log.i("SENSOR", "Acceleration changed. : " + event.values[0]);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private class accListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {  // 가속도 센서 값이 바뀔때마다 호출됨

            //Log.i("SENSOR", "Acceleration changed.");
            //Log.i("SENSOR", "  Acceleration X: " + event.values[0]
            //        + ", Acceleration Y: " + event.values[1]
            //        + ", Acceleration Z: " + event.values[2]);
            String val = new String();
            val = "Acc X: " + String.valueOf(event.values[0]).substring(0,3)
                    + "/Y: " + String.valueOf(event.values[1]).substring(0,3)
                    + "/Z: " + String.valueOf(event.values[2]).substring(0,3);
            acc.setText(val);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private class oriListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {  // 방향 센서 값이 바뀔때마다 호출됨

            //Log.i("SENSOR", "Orientation changed.");
            //Log.i("SENSOR", "  Orientation X: " + event.values[0]
            //        + ", Orientation Y: " + event.values[1]
            //        + ", Orientation Z: " + event.values[2]);
            String val = new String();
            val = "ori X: " + String.valueOf(event.values[0]).substring(0,3)
                    + "/Y: " + String.valueOf(event.values[1]).substring(0,3)
                    + "/Z: " + String.valueOf(event.values[2]).substring(0,3);
            ori.setText(val);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class heartListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {  // 방향 센서 값이 바뀔때마다 호출됨

            //Log.i("SENSOR", "Orientation changed.");
            //Log.i("SENSOR", "HR: " + event.values[0]);
            heart.setText(String.valueOf(event.values[0]));
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}

