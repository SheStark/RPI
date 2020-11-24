package pg.eti.ksg.ProjektInzynierski.ui.home;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.jetbrains.annotations.NotNull;

import pg.eti.ksg.ProjektInzynierski.AlertDialogs;
import pg.eti.ksg.ProjektInzynierski.Permissions;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.Services.DangerForegroundService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private boolean permission;
    private Button helpBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Permissions.LocationPermission(getContext());
            if (!permission)
                requestPermissions(Permissions.LOCATION_PERMISSIONS, Permissions.LOCATION_REQUEST_CODE);
        }
        else
            permission =true;

        helpBtn = root.findViewById(R.id.helpBtn);


        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help();
            }
        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResult){
        permission = Permissions.checkRequestCode(requestCode,permissions,grantResult);

    }

    public void help() {
        Intent service=new Intent(getActivity(), DangerForegroundService.class);
        if(isServiceRunning()){
            getActivity().stopService(service);
        }
        else if (permission) {
            if(Permissions.isLocationEnabled(getContext()))
                getActivity().startService(service);
            else
            {
                AlertDialogs.locationDisabledAlertDialog(getContext());
            }
            AlertDialogs.startDangerAlertDialog(getContext());
        }else
            requestPermissions(Permissions.LOCATION_PERMISSIONS,Permissions.LOCATION_REQUEST_CODE);
    }

    public boolean isServiceRunning()
    {
        ActivityManager manager =(ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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