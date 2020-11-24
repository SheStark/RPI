package pg.eti.ksg.ProjektInzynierski.ui.directions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.Repository.PointsRepository;

public class MapsViewModel extends AndroidViewModel {

    private Application application;
    private LiveData<List<Points>> listPoints;
    private PointsRepository pointsRepository;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        pointsRepository = new PointsRepository(application);
    }

    public LiveData<List<Points>> getPoints(Long id)
    {
        listPoints = pointsRepository.getListPoints(id);
        return listPoints;
    }
}
