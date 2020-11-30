package pg.eti.ksg.ProjektInzynierski.ui.register;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import pg.eti.ksg.ProjektInzynierski.Models.MessageCodes;
import pg.eti.ksg.ProjektInzynierski.Models.RegisterModel;
import pg.eti.ksg.ProjektInzynierski.Models.ResponseModel;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginData;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.ValidForms;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import pg.eti.ksg.ProjektInzynierski.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;


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

        ServerApi api = ServerClient.getClient();

        Call<ResponseModel> call = api.register(new RegisterModel(login,name,surname,email,password));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                if(!response.isSuccessful())
                    Toast.makeText(RegisterActivity.this,"Błąd! Spróbuj ponownie później",Toast.LENGTH_LONG).show();

                if(response.body().getCode() != MessageCodes.OK.getCode()) {
                    getResponse(MessageCodes.fromInt(response.body().getCode()));
                    return;
                }
                Toast.makeText(RegisterActivity.this,"Rejestracja przebiegła prawidłowo ",Toast.LENGTH_LONG).show();
                Intent intent =new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
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

    private void getResponse(MessageCodes code) {
        switch (code) {
            case INVALIDLOGIN:
                LoginForm.setError("Podany login już istnieje");
                break;
            case INVALIDEMAIL:
                EmailForm.setError("Podany adres email już istnieje");
                break;
            case INVALIDVALUES:
                Toast.makeText(this, "Nieprawidłowe dane", Toast.LENGTH_LONG).show();
                break;
            case ERROR:
                Toast.makeText(this, "Wystapił nieoczekiwany błąd, spróbuj ponownie później", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }




}