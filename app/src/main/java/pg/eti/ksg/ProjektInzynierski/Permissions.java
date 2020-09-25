package pg.eti.ksg.ProjektInzynierski;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//change class

public class Permissions {

    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
   // private static final String ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
    public static final String[] LOCATION_PERMISSIONS ={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    public static final int LOCATION_REQUEST_CODE = 1234;



    public static boolean LocationPermission(Context context)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }
        return true;

    }

    public static boolean checkRequestCode(int requestCode, String[] permissions,int[] grantResults)
    {
        switch(requestCode){
            case LOCATION_REQUEST_CODE:
                if(grantResults.length>0){
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                            return false;
                    }
                    return true;
                }
                return false;
            default:
                return false;
        }
    }


}
