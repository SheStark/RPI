package pg.eti.ksg.ProjektInzynierski;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class AlertDialogs {

    public static void locationDisabledAlertDialog(Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Lokalizacja wyłączona")
                .setMessage("Lokalizacja jest wyłączona. Uruchom ją i spróbuj ponownie")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .create()
                .show();
    }

    public static void serverError(Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Serwer error")
                .setMessage("Wystąpił nieoczekiwany błąd przy komunikacji z serwerem. Sróbuj ponownie później")
                .setCancelable(true)
                .create()
                .show();
    }


    public static void networkError(Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Problem z siecią")
                .setMessage("nie można nawiązać połączenia z serwerem, sprawdź połączenie internetowe i spróbuj ponownie urucomic usługę")
                .setCancelable(true)
                .create()
                .show();
    }
}
