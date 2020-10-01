package pg.eti.ksg.ProjektInzynierski.ui.navigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;
import pg.eti.ksg.ProjektInzynierski.Repository.UserRepository;

public class NavigationViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<Users> user;
    private Application application;

    public NavigationViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<Users> getUser(String login) {
        if(repository == null)
        {
            repository =new UserRepository(application,login);
            user = repository.getUser();
        }
        return user;
    }
}
