package pg.eti.ksg.ProjektInzynierski.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.Dao.PointsDao;
import pg.eti.ksg.ProjektInzynierski.Dao.ProjectDatabase;
import pg.eti.ksg.ProjektInzynierski.Dao.UserDao;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;

public class PointsRepository {

    private PointsDao pointsDao;
    private LiveData<List<Points>> listPoints;


    public PointsRepository(Application application) {
        ProjectDatabase database = ProjectDatabase.getDatabase(application);
        pointsDao =database.pointsDao();
    }

    public LiveData<List<Points>> getListPoints(Long routeId) {
        if(listPoints == null)
            listPoints = pointsDao.getRoutePoints(routeId);
        return listPoints;
    }

    public void insert(Points point){
        new InsertAsyncTask(pointsDao).execute(point);
    }


    public void update(Points point){
        new UpdateAsyncTask(pointsDao).execute(point);
    }


    public void delete(Points point){
        new DeleteAsyncTask(pointsDao).execute(point);
    }

    public void insertAll(List<Points> points){
        new InsertAllAsyncTask(pointsDao).execute(points);
    }

    private static class InsertAsyncTask extends AsyncTask<Points,Void,Void> {

        private PointsDao pointsDao;

        public InsertAsyncTask(PointsDao pointsDao) {
            this.pointsDao = pointsDao;
        }

        @Override
        protected Void doInBackground(Points... points) {
            if(pointsDao.getPoint(points[0].getId()) == null)
                pointsDao.insert(points[0]);
            return null;
        }
    }

    private static class InsertAllAsyncTask extends AsyncTask<List<Points>,Void,Void> {

        private PointsDao pointsDao;

        public InsertAllAsyncTask(PointsDao pointsDao) {
            this.pointsDao = pointsDao;
        }

        @Override
        protected Void doInBackground(List<Points>... points) {
            pointsDao.insert(points[0]);
            return null;
        }
    }



    private static class UpdateAsyncTask extends AsyncTask<Points,Void,Void> {

        private PointsDao pointsDao;

        public UpdateAsyncTask(PointsDao pointsDao) {
            this.pointsDao = pointsDao;
        }

        @Override
        protected Void doInBackground(Points... points) {
            pointsDao.update(points[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Points,Void,Void> {

        private PointsDao pointsDao;

        public DeleteAsyncTask(PointsDao pointsDao) {
            this.pointsDao = pointsDao;
        }

        @Override
        protected Void doInBackground(Points... points) {
            pointsDao.delete(points[0]);
            return null;
        }
    }
}
