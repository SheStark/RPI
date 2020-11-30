package pg.eti.ksg.ProjektInzynierski.ui.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;
import pg.eti.ksg.ProjektInzynierski.Repository.FriendsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.UserRepository;

public class AccountViewModel extends AndroidViewModel {

    private LiveData<Users> user;
    private LiveData<UserWithFriends> friend;
    private Application application;
    private UserRepository userRepository;
    private FriendsRepository friendsRepository;


    public AccountViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<Users> getUser(String login) {
        if(userRepository == null)
        {
            userRepository = new UserRepository(application,login);
            user = userRepository.getUser();
        }
        return user;
    }
    public LiveData<UserWithFriends> getFriend(String login) {
        if(friendsRepository == null)
        {
            friendsRepository =new FriendsRepository(application,login);
            friend = friendsRepository.getFriends();
        }
        return friend;
    }
}