package pg.eti.ksg.rejestracja.server;

import pg.eti.ksg.rejestracja.Models.LoginModel;
import pg.eti.ksg.rejestracja.Models.RegisterModel;
import pg.eti.ksg.rejestracja.Models.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServerApi {

    @POST("/user/register")
    Call<Response> register(@Body RegisterModel registerModel);

    @POST("/user/login")
    Call<Void> login(@Body LoginModel loginModel);
}
