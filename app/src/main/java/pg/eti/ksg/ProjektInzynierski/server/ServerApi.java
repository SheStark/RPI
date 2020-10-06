package pg.eti.ksg.ProjektInzynierski.server;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
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

    @GET("invitations/{login}")
    Call<List<Users>>  getUserInvitations(@Path("login") String login);

    @POST("invitations/{userLogin}/invite/{invitationLogin}")
    Call<ResponseModel> sendInvitation(@Path("userLogin") String userLogin, @Path("invitationLogin") String invitationLogin);

    @GET("friends/{login}")
    Call<List<Friends>> getUserFriends(@Path("login") String login);

    @POST("invitations/{userLogin}/accept/{friendLogin}")
    Call<Friends> acceptInvitation(@Path("userLogin") String userLogin, @Path("friendLogin") String friendLogin);

    @POST("invitations/{userLogin}/dismiss/{invitationLogin}")
    Call<ResponseModel> dismissInvitation(@Path("userLogin") String userLogin, @Path("invitationLogin") String invitationLogin);

    @GET("/routes/my/{login}")
    Call<List<Routes>> getMyRoutes(@Path("login") String userLogin);

    @GET("/routes/friends/{login}")
    Call<List<Routes>> getFriendsRoutes(@Path("login") String userLogin);
}
