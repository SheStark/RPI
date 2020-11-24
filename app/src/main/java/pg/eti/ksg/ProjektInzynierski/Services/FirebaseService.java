package pg.eti.ksg.ProjektInzynierski.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.Models.FirebaseCodes;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.Repository.FriendsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.InvitationsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.PointsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.RoutesRepository;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.ui.login.LoginActivity;

import static pg.eti.ksg.ProjektInzynierski.App.FIREBASE_SERVICE;
import static pg.eti.ksg.ProjektInzynierski.Models.FirebaseCodes.newFriend;
import static pg.eti.ksg.ProjektInzynierski.Models.FirebaseCodes.newInvitation;
import static pg.eti.ksg.ProjektInzynierski.Models.FirebaseCodes.newMessage;
import static pg.eti.ksg.ProjektInzynierski.Models.FirebaseCodes.newPoint;
import static pg.eti.ksg.ProjektInzynierski.Models.FirebaseCodes.startDangerRoute;

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

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String stringType = remoteMessage.getData().get("type");
            SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(this);
            if(stringType != null ) {
                int type = Integer.parseInt(stringType);
                switch(type) {
                    case startDangerRoute:
                        startDanger(remoteMessage);
                        break;
                    case newPoint:
                        addPoint(remoteMessage);
                        break;
                    case newFriend:
                        newFriend(remoteMessage);
                        break;
                    case newInvitation:
                        newInvitation(remoteMessage);
                        break;
                    case newMessage:
                        newMessage(remoteMessage);
                        break;
                    default:
                        break;
                }

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

                Intent resultIntent = new Intent(this, LoginActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);

                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new NotificationCompat.Builder(this, FIREBASE_SERVICE)
                        .setSmallIcon(R.drawable.ic_baseline_location_on_red)
                        .setContentTitle("Przyjaciel w niebezpieczeństwie")
                        .setContentText("Twój przyjaciel " + remoteMessage.getData().get("login") + " potrzebuje szybkiej pomocy")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(resultPendingIntent)
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
        String login = manager.logged();
        if(login != null && !login.isEmpty()) {
            Map<String, String> data = remoteMessage.getData();
            Date date = new Date(Timestamp.valueOf(data.get("datePoint")).getTime());
            Points point = new Points(Long.parseLong(data.get("pointId")), Long.parseLong(data.get("routeId")), Double.parseDouble(data.get("lat")), Double.parseDouble(data.get("lng")), date);
            PointsRepository pointsRepository = new PointsRepository(getApplication());
            pointsRepository.insert(point);
        }
    }

    public void newFriend(RemoteMessage remoteMessage){
        String login = manager.logged();
        if(login != null && !login.isEmpty()) {
            Map<String, String> data = remoteMessage.getData();
            Friends friend = new Friends(data.get("login"), data.get("name"), data.get("surname"), data.get("email"));
            if (!data.get("city").equals("null"))
                friend.setCity(data.get("city"));
            if (!data.get("birth").equals("null")) {
                Date birth = new Date(Timestamp.valueOf(data.get("birth")).getDate());
                friend.setBirth(birth);
            }
            FriendsRepository repository = new FriendsRepository(getApplication(),login);
            repository.insert(friend);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                Intent resultIntent = new Intent(this, LoginActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);

                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new NotificationCompat.Builder(this, FIREBASE_SERVICE)
                        .setSmallIcon(R.drawable.ic_baseline_location_on_red)
                        .setContentTitle("Akceptacja zaproszenia")
                        .setContentText(remoteMessage.getData().get("login") + " zaakceptował Twoje zaproszenie")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(resultPendingIntent)
                        .build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(3, notification);
            }
        }
    }

    public void newInvitation(RemoteMessage remoteMessage){
        String login = manager.logged();
        if(login != null && !login.isEmpty()) {
            Map<String, String> data = remoteMessage.getData();
            Invitations invitation = new Invitations(data.get("login"), data.get("name"), data.get("surname"));
            if (!data.get("city").equals("null"))
                invitation.setCity(data.get("city"));

            InvitationsRepository repository = new InvitationsRepository(getApplication(),login);
            repository.insert(invitation);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                Intent resultIntent = new Intent(this, LoginActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);

                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new NotificationCompat.Builder(this, FIREBASE_SERVICE)
                        .setSmallIcon(R.drawable.ic_baseline_location_on_red)
                        .setContentTitle("Nowe zaproszenie")
                        .setContentText(remoteMessage.getData().get("login") + " wysłał Ci zaproszenie do grona znajomych")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(resultPendingIntent)
                        .build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(4, notification);
            }
        }
    }

    public void newMessage(RemoteMessage remoteMessage){
        //TODO
    }
}
