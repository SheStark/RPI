package pg.eti.ksg.ProjektInzynierski.ui.navigation;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import pg.eti.ksg.ProjektInzynierski.AlertDialogs;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;
import pg.eti.ksg.ProjektInzynierski.Models.MessageCodes;
import pg.eti.ksg.ProjektInzynierski.Models.ResponseModel;
import pg.eti.ksg.ProjektInzynierski.Permissions;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.Services.DangerForegroundService;
import pg.eti.ksg.ProjektInzynierski.Services.ShakeService;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import pg.eti.ksg.ProjektInzynierski.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferencesLoginManager manager;
    private NavigationViewModel navigationViewModel;
    private TextView navUserName, navUserEmail;
    private View headerView;
    private String login;
    private boolean permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        manager = new SharedPreferencesLoginManager(this);
        login = manager.logged();
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        permission = Permissions.LocationPermission(this);
            if(!permission)
                    requestPermissions(Permissions.LOCATION_PERMISSIONS,Permissions.LOCATION_REQUEST_CODE);
        }
        else {
            permission = true;
        }
        Bundle bundle = getIntent().getExtras();
        boolean danger;
        if(bundle !=null) {
            danger = bundle.getBoolean("danger");
        }
        else
            danger =false;

        if(danger)
        {
            AlertDialogs.startDangerAlertDialog(this);
        }

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
               return logout();
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_account, R.id.nav_map,R.id.nav_routes,R.id.nav_friends,R.id.nav_invitations)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setTextViews();
        Log.d("Token", "Token = "+ FirebaseInstanceId.getInstance().getToken());

    }

    public boolean logout()
    {
        String login = manager.logged();
        if(login != null && !login.isEmpty())
        {
            ServerApi api = ServerClient.getClient();
            Call<ResponseModel> logout  = api.logout(login);
            logout.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                    if(response.isSuccessful()) {
                        if (response.body().getCode() == MessageCodes.OK.getCode()) {
                            if (manager.logout()) {
                                if(isServiceRunning(ShakeService.class.getName()))
                                {
                                    Intent shake = new Intent(getApplicationContext(), ShakeService.class);
                                    stopService(shake);
                                }
                                if(isServiceRunning(DangerForegroundService.class.getName())){
                                    Intent service=new Intent(getApplicationContext(), DangerForegroundService.class);
                                    stopService(service);
                                }
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Nie można sie wylogować",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Nie można sie wylogować",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Brak połączenia z serwerem",Toast.LENGTH_LONG).show();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setTextViews()
    {
        navUserEmail = (TextView) headerView.findViewById(R.id.navUserEmail);
        navUserName = (TextView) headerView.findViewById(R.id.navUserName);
        navigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel.class);
        if(login != null ||!login.isEmpty()) {
            navigationViewModel.getUser(login).observe(this, new Observer<Users>() {
                @Override
                public void onChanged(Users users) {
                    if(users != null) {
                        navUserEmail.setText(users.getEmail());
                        navUserName.setText(users.getName() + " " + users.getSurname());
                    }
                }
            });
        }
    }

    public boolean isServiceRunning(String serviceName)
    {
        ActivityManager manager =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(manager!=null)
        {
            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if(serviceName.equals(service.service.getClassName()))
                    return true;
            }
        }
        return false;
    }

}