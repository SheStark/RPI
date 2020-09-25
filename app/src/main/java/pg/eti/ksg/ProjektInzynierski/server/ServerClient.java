package pg.eti.ksg.ProjektInzynierski.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerClient {
    public static String URL = "http://192.168.1.13:8080";

    public static ServerApi getClient()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ServerApi.class);
    }
}
