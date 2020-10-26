package pg.eti.ksg.ProjektInzynierski.Services;

import android.app.Notification;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.Models.FirebaseCodes;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.Repository.PointsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.RoutesRepository;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;

import static pg.eti.ksg.ProjektInzynierski.App.FIREBASE_SERVICE_START_DANGER;
import static pg.eti.ksg.ProjektInzynierski.App.FOREGROUND_SERVICE_CHANNEL;

public class FirebaseService extends FirebaseMessagingService {

    private String TAG = "Firebase";
    private SharedPreferencesLoginManager manager;


    public void onCreate()
    {
        super.onCreate();
        manager=new SharedPreferencesLoginManager(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String stringType = remoteMessage.getData().get("type");
            SharedPreferencesLoginManager manager =new SharedPreferencesLoginManager(this);
            if(stringType != null ) {
                int type = Integer.parseInt(stringType);

                if (type == FirebaseCodes.startDangerRoute) {
                    startDanger(remoteMessage);
                }
                else if(type == FirebaseCodes.newPoint){
                    addPoint(remoteMessage);
                }


            }
            //remoteMessage.getData().
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
               // scheduleJob();
            } else {
                // Handle message within 10 seconds
               // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

    public void startDanger(RemoteMessage remoteMessage)
    {
        String login = manager.logged();
        if(login != null && !login.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification notification = new NotificationCompat.Builder(this, FIREBASE_SERVICE_START_DANGER)
                        .setSmallIcon(R.drawable.ic_baseline_location_on_red)
                        .setContentTitle("Przyjaciel w niebezpieczeństwie")
                        .setContentText("Twój przyjaciel " + remoteMessage.getData().get("login") + " potrzebuje szybkiej pomocy")
                        .build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(2, notification);
            }
            Map<String,String> data = remoteMessage.getData();
            Date date = new Date(Timestamp.valueOf(data.get("dateRoute")).getTime());
            Routes route = new Routes(Long.parseLong(data.get("id")),data.get("login"),true, date);
            Points point = new Points(Long.parseLong(data.get("pointId")),Long.parseLong(data.get("routeId")),Double.parseDouble(data.get("lat")),Double.parseDouble(data.get("lng")),date);
            RoutesRepository routesRepository = new RoutesRepository(getApplication(),login);
            routesRepository.insert(route);
            PointsRepository pointsRepository = new PointsRepository(getApplication());
            pointsRepository.insert(point);

        }
    }

    public void addPoint(RemoteMessage remoteMessage)
    {
        Map<String,String> data = remoteMessage.getData();
        Date date = new Date(Timestamp.valueOf(data.get("datePoint")).getTime());
        Points point = new Points(Long.parseLong(data.get("pointId")),Long.parseLong(data.get("routeId")),Double.parseDouble(data.get("lat")),Double.parseDouble(data.get("lng")),date);
        PointsRepository pointsRepository = new PointsRepository(getApplication());
        pointsRepository.insert(point);
    }
}
