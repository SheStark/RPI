package pg.eti.ksg.ProjektInzynierski;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String FOREGROUND_SERVICE_CHANNEL = "ForegroundServiceChannel";
    public static final String FIREBASE_SERVICE_START_DANGER = "FirebaseServiceDanger";

    @Override
    public void onCreate(){
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel =new NotificationChannel(
                    FOREGROUND_SERVICE_CHANNEL,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(notificationChannel);



            NotificationChannel firebaseChannelDanger = new NotificationChannel(
                    FIREBASE_SERVICE_START_DANGER,
                    "Firebase Service Danger",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(firebaseChannelDanger);
        }
    }
}
