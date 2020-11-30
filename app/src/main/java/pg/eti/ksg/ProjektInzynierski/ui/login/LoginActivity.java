package pg.eti.ksg.ProjektInzynierski.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import pg.eti.ksg.ProjektInzynierski.Services.DangerForegroundService;
import pg.eti.ksg.ProjektInzynierski.Services.ShakeService;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.AccountsPhoneRVAdapter;
import pg.eti.ksg.ProjektInzynierski.Models.LoginModel;
import pg.eti.ksg.ProjektInzynierski.ui.navigation.NavigationActivity;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.Services.SynchronizeDataService;
import pg.eti.ksg.ProjektInzynierski.ValidForms;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import pg.eti.ksg.ProjektInzynierski.ui.register.RegisterActivity;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginData;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout loginTxt,passwordTxt;
    private ArrayList<SharedPreferencesLoginData> users;
    private RecyclerView RVaccountsOnPhone;
    private SharedPreferencesLoginManager manager;
    private ValidForms validation;
    private String login,password;


    private AccountsPhoneRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // Log.d("Token", FirebaseInstanceId.getInstance().getToken());
        manager = new SharedPreferencesLoginManager(this);
        validation = new ValidForms();
        String log = manager.logged();
        if(!log.isEmpty())
        {

            if(!manager.isDataSynchronized())
            {
                Intent serviceIntent = new Intent(this, SynchronizeDataService.class);
                SynchronizeDataService.enqueueWork(this,serviceIntent);

            }
            if(!isServiceRunning()) {
                Intent shake = new Intent(this, ShakeService.class);
                startService(shake);
            }

            Intent intent =new Intent(getApplicationContext(), NavigationActivity.class);
            startActivity(intent);
        }

        loginTxt=(TextInputLayout) findViewById(R.id.Login);
        passwordTxt=(TextInputLayout) findViewById(R.id.Password);
        RVaccountsOnPhone = (RecyclerView) findViewById(R.id.RVaccountsOnPhone);

        users = manager.getPreferences();
        if(users.size() == 0) {
            clearRV();
        }
        else {
            adapter = new AccountsPhoneRVAdapter(users);
            adapter.setClickListener(new AccountsPhoneRVAdapter.ClickListener() {
                @Override
                public void onItemClick(int position) {
                    loginTxt.getEditText().setText(users.get(position).getLogin());
                    loginTxt.clearFocus();
                    passwordTxt.requestFocus();

                    showKeyboard();
                }

                @Override
                public void onItemDelete(int position) {

                    //delete database

                    //delete from shared preferences
                    manager.deleteData(position);
                    adapter.notifyItemRemoved(position);
                    if(users.size() == 0)
                        clearRV();
                }
            });
            RVaccountsOnPhone.setAdapter(adapter);
            RVaccountsOnPhone.setLayoutManager(new LinearLayoutManager(this));
            //RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            //RVaccountsOnPhone.addItemDecoration(itemDecoration);
        }
    }
    public void clearRV()
    {
        TextView loginActivity=(TextView)findViewById(R.id.loginActivityTxt);
        loginActivity.setVisibility(View.GONE);
        RVaccountsOnPhone.setVisibility(View.GONE);
    }

    public void showKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void NoAccountBtnClick(View view){
        Intent intent =new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void LoginBtnClick(View view)
    {

        if(!validation.ValidLogin(loginTxt) | !validation.ValidPassword(passwordTxt))
            return;

        login = loginTxt.getEditText().getText().toString().trim();
        password =passwordTxt.getEditText().getText().toString().trim();

        String token = FirebaseInstanceId.getInstance().getToken();
        ServerApi api = ServerClient.getClient();
        Call<Void> call = api.login(new LoginModel(login,password,token));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Nieprawidłowy login lub hasło", Toast.LENGTH_LONG).show();
                    return;
                }

                if(manager.logIn(login)) {
                    //if not in database create user and load data
                    Intent serviceIntent = new Intent(getApplicationContext(), SynchronizeDataService.class);
                    SynchronizeDataService.enqueueWork(getApplicationContext(),serviceIntent);

                    Intent shake = new Intent(getApplicationContext(), ShakeService.class);
                    startService(shake);

                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

   /* public boolean findUser()
    {
        for (SharedPreferencesLoginData user : users)
            if(user.getLogin().equals(login)){
                if(user.getPassword().equals(password))
                    return true;
            }
        return false;
    }*/

    public Context getContext()
    {
        return this;
    }

    public boolean isServiceRunning()
    {
        ActivityManager manager =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(manager!=null)
        {
            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if(ShakeService.class.getName().equals(service.service.getClassName()))
                    return true;
            }
        }

        return false;
    }
}