package pg.eti.ksg.ProjektInzynierski.ui.friends;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.Repository.FriendsRepository;

public class FriendsViewModel extends AndroidViewModel {

    private Application application;
    private LiveData<UserWithFriends> friends;
    private FriendsRepository repository;

    public FriendsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }


    public LiveData<UserWithFriends> getFriends(String userLogin) {
        if(repository == null)
            repository = new FriendsRepository(application,userLogin);
        if(friends == null)
            friends = repository.getFriends();
        return friends;
    }
}