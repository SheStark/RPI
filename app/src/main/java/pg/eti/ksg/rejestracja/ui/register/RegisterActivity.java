package pg.eti.ksg.rejestracja.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import pg.eti.ksg.rejestracja.R;
import pg.eti.ksg.rejestracja.SharedPreferencesLoginData;
import pg.eti.ksg.rejestracja.SharedPreferencesLoginManager;
import pg.eti.ksg.rejestracja.ValidForms;
import pg.eti.ksg.rejestracja.ValidatePatterns;
import pg.eti.ksg.rejestracja.ui.login.LoginActivity;


public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout LoginForm, NameForm, SurnameForm, EmailForm, PasswordForm;
    private String login,name,surname,email,password;
    private SharedPreferencesLoginManager manager;
    private ValidForms validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getInputLayouts();
        validation = new ValidForms();
    }

    public void AlreadyRegisteredBtnClick(View view){
        Intent intent =new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public boolean ValidForm()
    {
        if(!validation.ValidEmail(EmailForm) | !validation.ValidLogin(LoginForm) | ! validation.ValidName(NameForm) |
                ! validation.ValidPassword(PasswordForm) | !validation.ValidSurname(SurnameForm) ){
            Toast.makeText(RegisterActivity.this,"Formularz nie jest wypełniony prawidłowo",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void RegisterBtnClick(View view) {
        if (!ValidForm())
            return;
        getValuesForm();
        // else
        // start  bound serwis

        //shared preferences
        manager = new SharedPreferencesLoginManager(this);

        SharedPreferencesLoginData newUser = new SharedPreferencesLoginData(login,name,surname,password);
        manager.addData(newUser);

        Toast.makeText(RegisterActivity.this,"Rejestracja przebiegła prawidłowo ",Toast.LENGTH_LONG).show();
        Intent intent =new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);

    }

    private void getValuesForm()
    {
        name = NameForm.getEditText().getText().toString().trim();
        email = EmailForm.getEditText().getText().toString().trim();
        login = LoginForm.getEditText().getText().toString().trim();
        surname = SurnameForm.getEditText().getText().toString().trim();
        password = PasswordForm.getEditText().getText().toString().trim();
    }

    private void getInputLayouts()
    {
        LoginForm = (TextInputLayout)findViewById(R.id.LoginForm);
        NameForm = (TextInputLayout)findViewById(R.id.NameForm);
        SurnameForm = (TextInputLayout)findViewById(R.id.SurnameForm);
        EmailForm = (TextInputLayout)findViewById(R.id.EmailForm);
        PasswordForm = (TextInputLayout)findViewById(R.id.PasswordForm);
    }
}