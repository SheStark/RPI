package pg.eti.ksg.ProjektInzynierski.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pg.eti.ksg.ProjektInzynierski.Dao.PointsDao;
import pg.eti.ksg.ProjektInzynierski.Dao.ProjectDatabase;
import pg.eti.ksg.ProjektInzynierski.Dao.RoutesDao;
import pg.eti.ksg.ProjektInzynierski.Dao.UserDao;
import pg.eti.ksg.ProjektInzynierski.Dao.UserRoutesDao;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserRoutes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithRoutes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;

public class RoutesRepository {

    private RoutesDao routesDao;
    private UserRoutesDao userRoutesDao;
    private PointsDao pointsDao;
    private LiveData<UserWithRoutes> userWithRoutes;
    private String login;


    public RoutesRepository(Application application,String login){
        ProjectDatabase database =ProjectDatabase.getDatabase(application);
        this.login = login;
        routesDao = database.routesDao();
        userRoutesDao = database.userRoutesDao();
        pointsDao = database.pointsDao();
        userWithRoutes = routesDao.getUserRoutes(login);
    }

    public LiveData<UserWithRoutes> getUserWithRoutes() {
        return userWithRoutes;
    }

    public boolean isRouteSync(Long id){
        Routes route = routesDao.getRoute(id);
        if(route==null)
            return false;
        return true;
    }

    public void insert(Routes route)
    {
        new RoutesRepository.InsertAsyncTask(routesDao,userRoutesDao,login).execute(route);
    }
    public void update(Routes route)
    {
        new RoutesRepository.UpdateAsyncTask(routesDao).execute(route);
    }
    public void delete(Routes route)
    {
        new RoutesRepository.DeleteAsyncTask(routesDao,userRoutesDao,pointsDao).execute(route);
    }

    public void delete(UserRoutes userRoutes){
        //TODO
    }

    public LiveData<UserWithRoutes> getFriendRoutes(){
       return Transformations.switchMap(userWithRoutes,this::getFriendsRoutesFromLiveData);
    }

    public LiveData<UserWithRoutes> getMyRoutes(){
        return Transformations.switchMap(userWithRoutes,this::getMyRoutesFromLiveData);
    }

    public LiveData<UserWithRoutes> getFriendsRoutesFromLiveData(UserWithRoutes userWithRoutes)
    {
        MutableLiveData<UserWithRoutes> live = new MutableLiveData<>();
        List<Routes> routes = new LinkedList<>();
        for(Routes route: userWithRoutes.getRoutes() ) {
            if(!route.getFriendLogin().equals(login))
                routes.add(route);
        }
        UserWithRoutes userRoutes = new UserWithRoutes();
        userRoutes.setRoutes(routes);
        userRoutes.setUser(userWithRoutes.getUser());
        live.setValue(userRoutes);
        return live;
    }

    public LiveData<UserWithRoutes> getMyRoutesFromLiveData(UserWithRoutes userWithRoutes)
    {
        MutableLiveData<UserWithRoutes> live = new MutableLiveData<>();
        List<Routes> routes = new LinkedList<>();
        for(Routes route: userWithRoutes.getRoutes() ) {
            if(route.getFriendLogin().equals(login))
                routes.add(route);
        }
        UserWithRoutes userRoutes = new UserWithRoutes();
        userRoutes.setRoutes(routes);
        userRoutes.setUser(userWithRoutes.getUser());
        live.setValue(userRoutes);
        return live;
    }


    private static class InsertAsyncTask extends AsyncTask<Routes,Void,Void> {

        private RoutesDao routesDao;
        private UserRoutesDao userRoutesDao;
        private String login;

        public InsertAsyncTask(RoutesDao routesDao,UserRoutesDao userRoutesDao,String login) {
            this.routesDao=routesDao;
            this.userRoutesDao =userRoutesDao;
            this.login=login;
        }

        @Override
        protected Void doInBackground(Routes... routes) {

            if(routesDao.getRoute(routes[0].getId()) == null)
                routesDao.insert(routes[0]);
            if(userRoutesDao.getUserRoute(login,routes[0].getId()) == null)
                userRoutesDao.insert(new UserRoutes(login,routes[0].getId()));

            return null;
        }
    }


    private static class UpdateAsyncTask extends AsyncTask<Routes,Void,Void> {

        private RoutesDao routesDao;

        public UpdateAsyncTask(RoutesDao routesDao) {
            this.routesDao=routesDao;
        }

        @Override
        protected Void doInBackground(Routes... routes) {
            routesDao.update(routes[0]);

            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Routes,Void,Void> {

        private RoutesDao routesDao;
        private UserRoutesDao userRoutesDao;
        private PointsDao pointsDao;

        public DeleteAsyncTask(RoutesDao routesDao, UserRoutesDao userRoutesDao, PointsDao pointsDao) {
            this.routesDao=routesDao;
            this.userRoutesDao =userRoutesDao;
            this.pointsDao =pointsDao;
        }

        @Override
        protected Void doInBackground(Routes... routes) {

            userRoutesDao.deleteByRouteId(routes[0].getId());
            pointsDao.deleteRoutesPoints(routes[0].getId());
            routesDao.delete(routes[0]);

            return null;
        }
    }


}
