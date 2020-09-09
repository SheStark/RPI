package pg.eti.ksg.rejestracja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout loginTxt,passwordTxt;
    private ArrayList<SharedPreferencesLoginData> users;
    private RecyclerView RVaccountsOnPhone;
    private SharedPreferencesLoginManager manager;

    private AccountsPhoneRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginTxt=(TextInputLayout) findViewById(R.id.Login);
        passwordTxt=(TextInputLayout) findViewById(R.id.Password);
        RVaccountsOnPhone = (RecyclerView) findViewById(R.id.RVaccountsOnPhone);

        manager = new SharedPreferencesLoginManager(this);
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
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            RVaccountsOnPhone.addItemDecoration(itemDecoration);

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
        Intent intent =new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }
}