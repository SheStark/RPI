package pg.eti.ksg.ProjektInzynierski.server;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;
import pg.eti.ksg.ProjektInzynierski.Models.LoginModel;
import pg.eti.ksg.ProjektInzynierski.Models.PointModel;
import pg.eti.ksg.ProjektInzynierski.Models.RegisterModel;
import pg.eti.ksg.ProjektInzynierski.Models.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServerApi {

    @POST("/user/register")
    Call<ResponseModel> register(@Body RegisterModel registerModel);

    @POST("/user/login")
    Call<Void> login(@Body LoginModel loginModel);

    @POST("/danger/{login}")
    Call<Void> sendPoint(@Path("login") String login, @Body PointModel pointModel);

    @POST("/danger/{login}/start")
    Call<ResponseModel> startDanger(@Path("login") String login, @Body PointModel pointModel);

    @GET("/user/{login}")
    Call<Users> getCurrentUser(@Path("login") String login);
}
