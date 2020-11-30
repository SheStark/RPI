package pg.eti.ksg.ProjektInzynierski.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pg.eti.ksg.ProjektInzynierski.ui.navigation.NavigationActivity;

public class BootService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent shake = new Intent(context, ShakeService.class);
       // shake.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(shake);
    }
}
