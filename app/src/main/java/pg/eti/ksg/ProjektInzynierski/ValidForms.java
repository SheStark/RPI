package pg.eti.ksg.ProjektInzynierski;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class ValidForms {

    public boolean ValidLogin(TextInputLayout textInputLayout)
    {
        String login=textInputLayout.getEditText().getText().toString().trim();
        if(login.isEmpty())
        {
            textInputLayout.setError("To pole nie może być puste");
            return false;
        }
        else if(login.length()<3)
        {
            textInputLayout.setError("Login powinien zawierać przynajmniej 3 znaki");
            return false;
        }
        else if(!ValidatePatterns.LOGIN_PATTERN.matcher(login).matches()){
            textInputLayout.setError("Nieprawidłowe Login");
            return false;
        }
        else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public boolean ValidName(TextInputLayout textInputLayout)
    {
        String name=textInputLayout.getEditText().getText().toString().trim();
        if(name.isEmpty())
        {
            textInputLayout.setError("To pole nie może być puste");
            return false;
        }
        else if(name.length()<3)
        {
            textInputLayout.setError("Imię powinno zawierać przynajmniej 3 znaki");
            return false;
        }
        else if(!ValidatePatterns.NAME_PATTERN.matcher(name).matches()){
            textInputLayout.setError("Nieprawidłowe Imię");
            return false;
        }
        else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public boolean ValidSurname(TextInputLayout textInputLayout)
    {
        String surname=textInputLayout.getEditText().getText().toString().trim();
        if(surname.isEmpty())
        {
            textInputLayout.setError("To pole nie może być puste");
            return false;
        }
        else if(surname.length()<3)
        {
            textInputLayout.setError("Nazwisko powinno zawierać przynajmniej 3 znaki");
            return false;
        }
        else if(!ValidatePatterns.NAME_PATTERN.matcher(surname).matches()){
            textInputLayout.setError("Nieprawidłowe Nazwisko");
            return false;
        }
        else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public boolean ValidEmail(TextInputLayout textInputLayout)
    {
        String email =textInputLayout.getEditText().getText().toString().trim();
        if(email.isEmpty())
        {
            textInputLayout.setError("To pole nie może być puste");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textInputLayout.setError("Nieprawidłowy adres email");
            return false;
        }
        else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public boolean ValidPassword(TextInputLayout textInputLayout)
    {
        String password=textInputLayout.getEditText().getText().toString().trim();
        if(password.isEmpty())
        {
            textInputLayout.setError("To pole nie może być puste");
            return false;
        }
        else if(password.length()<8)
        {
            textInputLayout.setError("Hasło powinno zawierac przynajmniej 8 znaków");
            return false;
        }
        else if(!ValidatePatterns.PASSWORD_PATTERN.matcher(password).matches()){
            textInputLayout.setError("Hasło powinno zawierać przynajmniej jedną małą literę, dużą literę, cyfre i znak specjalny (@#$%^&+=)");
            return false;
        }
        else {
            textInputLayout.setError(null);
            return true;
        }
    }
}
