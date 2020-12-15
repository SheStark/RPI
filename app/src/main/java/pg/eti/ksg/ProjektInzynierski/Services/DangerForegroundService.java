package pg.eti.ksg.ProjektInzynierski.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.Models.IdsModel;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.Repository.PointsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.RoutesRepository;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import pg.eti.ksg.ProjektInzynierski.ui.home.HomeFragment;
import pg.eti.ksg.ProjektInzynierski.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pg.eti.ksg.ProjektInzynierski.App.FOREGROUND_SERVICE_CHANNEL;

public class DangerForegroundService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private Runnable runnable;
    private String login;
    private FusedLocationProviderClient location;
    private LocationCallback locationCallback;
    private ServerApi api;
    private Long id;
    private LatLng latLng;
    private boolean startD;
    private RoutesRepository routesRepository;
    private PointsRepository pointsRepository;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        Notification notification =new NotificationCompat.Builder(this, FOREGROUND_SERVICE_CHANNEL)
                .setContentTitle("Udostępnianie lokalizacji")
                .setContentText("Niebezpieczeństwo! Twoja lokalizacja udostępniana jest znajomym")
                .setSmallIcon(R.drawable.ic_baseline_location_on_black)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .build();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(1,notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
            }
            else
                startForeground(1,notification);

        startD = true;
        SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(this);

        login = manager.logged();
        if(login == null || login.isEmpty())
            stopSelf();

        setCallback();
        api = ServerClient.getClient();


        location = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000*60*2);
        locationRequest.setFastestInterval(1000*60);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        location.requestLocationUpdates(locationRequest, locationCallback,Looper.getMainLooper());

        runnable =new Runnable() {
            @Override
            public void run() {
                doInBackground();
            }
        };

        new Thread(runnable).start();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                new Thread(runnable).start();
            }
        };
        timer.schedule(timerTask,1000*60*4,1000*60*4);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        if(timerTask != null && timer != null) {
            timerTask.cancel();
            timer.cancel();
        }


        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("MissingPermission")
    public void doInBackground()
    {
        if(latLng == null) {
            location.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location l = (Location) task.getResult();
                    if (l != null) {
                        latLng = new LatLng(l.getLatitude(), l.getLongitude());
                        sendLocation();
                    }
                }
            });
        }
        else
            sendLocation();

    }



    public void sendLocation()
    {
       // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
       // String localDateTime = format.format(date);


        Points point = new Points(latLng.latitude,latLng.longitude,date);
        if(startD)
            startDanger(point);
        else
            sendPoint(point);

    }

    public void sendPoint(Points point){
        point.setRouteId(id);
        Call send = api.sendPoint(login,point);
        send.enqueue(new Callback<IdsModel>() {
            @Override
            public void onResponse(Call<IdsModel> call, Response<IdsModel> response) {
                if(!response.isSuccessful()){
                    return;
                }

                long pointId = response.body().getPointId();
                if(pointId != -1) {
                    point.setId(pointId);
                    pointsRepository.insert(point);
                }
            }

            @Override
            public void onFailure(Call<IdsModel> call, Throwable t) {
                Log.d("Foreground service","Save to shared preferences");
            }
        });
    }

    public void startDanger(Points point)
    {
        Call start = api.startDanger(login,point);
        start.enqueue(new Callback<IdsModel>() {
            @Override
            public void onResponse(Call<IdsModel> call, Response<IdsModel> response) {
                if(!response.isSuccessful()){
                    //AlertDialogs.serverError(getApplicationContext());
                    stopSelf();
                    return;
                }
                id = response.body().getRouteId();
                long pointId= response.body().getPointId();
                if(id != -1 && pointId !=-1) {
                    startD = false;
                    routesRepository = new RoutesRepository(getApplication(), login);
                    pointsRepository = new PointsRepository(getApplication());
                    point.setRouteId(id);
                    point.setId(pointId);
                    routesRepository.insert(new Routes(id, login, true, point.getDate()));
                    pointsRepository.insert(point);
                }

            }

            @Override
            public void onFailure(Call<IdsModel> call, Throwable t) {
                //AlertDialogs.networkError(getApplicationContext());
                stopSelf();
                return;
            }
        });
    };

    private void setCallback()
    {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
            }

        };
    }


}
