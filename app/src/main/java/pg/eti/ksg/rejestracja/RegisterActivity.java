package pg.eti.ksg.rejestracja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;


public class RegisterActivity extends AppCompatActivity {

    TextInputLayout LoginForm, NameForm, SurnameForm, EmailForm, PasswordForm;
    String login,name,surname,email,password;
    SharedPreferencesLoginManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginForm = (TextInputLayout)findViewById(R.id.LoginForm);
        NameForm = (TextInputLayout)findViewById(R.id.NameForm);
        SurnameForm = (TextInputLayout)findViewById(R.id.SurnameForm);
        EmailForm = (TextInputLayout)findViewById(R.id.EmailForm);
        PasswordForm = (TextInputLayout)findViewById(R.id.PasswordForm);
    }

    public void AlreadyRegisteredBtnClick(View view){
        Intent intent =new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    public boolean ValidForm()
    {
        if(!ValidEmail() | !ValidLogin() | ! ValidName() | ! ValidPassword() | !ValidSurname()) {
            Toast.makeText(RegisterActivity.this,"Formularz nie jest wypełniony prawidłowo",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public void RegisterBtnClick(View view) {
        if (!ValidForm())
            return;
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

    public boolean ValidLogin()
    {
        login=LoginForm.getEditText().getText().toString().trim();
        if(login.isEmpty())
        {
            LoginForm.setError("To pole nie może być puste");
            return false;
        }
        else if(login.length()<3)
        {
            LoginForm.setError("Login powinien zawierać przynajmniej 3 znaki");
            return false;
        }
        else if(!ValidatePatterns.LOGIN_PATTERN.matcher(login).matches()){
            LoginForm.setError("Nieprawidłowe Login");
            return false;
        }
        else {
            LoginForm.setError(null);
            return true;
        }
    }

    public boolean ValidName()
    {
        name=NameForm.getEditText().getText().toString().trim();
        if(name.isEmpty())
        {
            NameForm.setError("To pole nie może być puste");
            return false;
        }
        else if(name.length()<3)
        {
            NameForm.setError("Imię powinno zawierać przynajmniej 3 znaki");
            return false;
        }
        else if(!ValidatePatterns.NAME_PATTERN.matcher(name).matches()){
            NameForm.setError("Nieprawidłowe Imię");
            return false;
        }
        else {
            NameForm.setError(null);
            return true;
        }
    }

    public boolean ValidSurname()
    {
        surname=SurnameForm.getEditText().getText().toString().trim();
        if(surname.isEmpty())
        {
            SurnameForm.setError("To pole nie może być puste");
            return false;
        }
        else if(surname.length()<3)
        {
            SurnameForm.setError("Nazwisko powinno zawierać przynajmniej 3 znaki");
            return false;
        }
        else if(!ValidatePatterns.NAME_PATTERN.matcher(surname).matches()){
            SurnameForm.setError("Nieprawidłowe Nazwisko");
            return false;
        }
        else {
            SurnameForm.setError(null);
            return true;
        }
    }

    public boolean ValidEmail()
    {
        email=EmailForm.getEditText().getText().toString().trim();
        if(email.isEmpty())
        {
            EmailForm.setError("To pole nie może być puste");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailForm.setError("Nieprawidłowy adres email");
            return false;
        }
        else {
            EmailForm.setError(null);
            return true;
        }
    }

    public boolean ValidPassword()
    {
        password=PasswordForm.getEditText().getText().toString().trim();
        if(password.isEmpty())
        {
            PasswordForm.setError("To pole nie może być puste");
            return false;
        }
        else if(password.length()<8)
        {
            PasswordForm.setError("Hasło powinno zawierac przynajmniej 8 znaków");
            return false;
        }
        else if(!ValidatePatterns.PASSWORD_PATTERN.matcher(password).matches()){
            PasswordForm.setError("Hasło powinno zawierać przynajmniej jedną małą literę, dużą literę, cyfre i znak specjalny (@#$%^&+=)");
            return false;
        }
        else {
            PasswordForm.setError(null);
            return true;
        }
    }
}