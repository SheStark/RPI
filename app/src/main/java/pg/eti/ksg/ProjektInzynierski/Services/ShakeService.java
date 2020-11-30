package pg.eti.ksg.ProjektInzynierski.Services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import pg.eti.ksg.ProjektInzynierski.ui.home.HomeFragment;
import pg.eti.ksg.ProjektInzynierski.ui.login.LoginActivity;
import pg.eti.ksg.ProjektInzynierski.ui.navigation.NavigationActivity;

import static pg.eti.ksg.ProjektInzynierski.App.FOREGROUND_SERVICE_CHANNEL;

public class ShakeService extends Service {

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private int count;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        Notification notification =new NotificationCompat.Builder(this, FOREGROUND_SERVICE_CHANNEL)
                .setContentTitle("Jesteś bezpieczny")
                .setContentText("Potrząśnij telefonem aby udostępnić lokalizację")
                .setSmallIcon(R.drawable.ic_baseline_location_on_black)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(10,notification);
        SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(this);
        String login = manager.logged();
        if(login == null || login.isEmpty())
            stopSelf();

        count =0;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        return START_STICKY;
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;
                if (mAccel > 20) {
                    count++;
                    if(count>5) {
                        count = 0;
                        if(!isServiceRunning()) {
                            Intent intent = new Intent(ShakeService.this, DangerForegroundService.class);
                            startService(intent);

                            Intent navigation = new Intent(ShakeService.this, NavigationActivity.class);
                            navigation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            navigation.putExtra("danger",true);
                            startActivity(navigation);
                        }
                    }
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    public boolean isServiceRunning()
    {
        ActivityManager manager =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(manager!=null)
        {
            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if(DangerForegroundService.class.getName().equals(service.service.getClassName()))
                    return true;
            }
        }

        return false;
    }


}
