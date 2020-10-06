package pg.eti.ksg.ProjektInzynierski.ui.routes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithRoutes;
import pg.eti.ksg.ProjektInzynierski.Repository.RoutesRepository;

public class RoutesViewModel extends AndroidViewModel {

    private RoutesRepository routesRepository;
    private LiveData<UserWithRoutes> friendRoutes;
    private LiveData<UserWithRoutes> myRoutes;
    private Application application;

    public RoutesViewModel(@NonNull Application application){
        super(application);
        this.application = application;
    }

    public LiveData<UserWithRoutes> getFriendRoutes(String login) {
        if(routesRepository == null)
            routesRepository =new RoutesRepository(application,login);
        if(friendRoutes == null)
            friendRoutes = routesRepository.getFriendRoutes();
        return friendRoutes;
    }

    public LiveData<UserWithRoutes> getMyRoutes(String login) {
        if(routesRepository == null)
            routesRepository = new RoutesRepository(application,login);
        if(myRoutes == null)
            myRoutes = routesRepository.getMyRoutes();
        return myRoutes;
    }


}