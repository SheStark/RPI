package pg.eti.ksg.ProjektInzynierski.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.ui.home.HomeFragment;
import pg.eti.ksg.ProjektInzynierski.ui.login.LoginActivity;
import pg.eti.ksg.ProjektInzynierski.ui.navigation.NavigationActivity;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private String login;
    private Friends friend;
    private boolean isUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        isUser = true;
        TextView loginTxt = getActivity().findViewById(R.id.accLogin);
        TextView nameTxt = getActivity().findViewById(R.id.accName);
        TextView surnameTxt = getActivity().findViewById(R.id.accSurname);
        TextView emailTxt = getActivity().findViewById(R.id.accEmail);
        TextView cityTxt = getActivity().findViewById(R.id.accCity);
        TextView birthTxt = getActivity().findViewById(R.id.accBirth);

        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null && bundle.getString("login") != null)
        {
            login = bundle.getString("login");
            isUser =false;
            accountViewModel.getFriend(login).observe(getViewLifecycleOwner(), new Observer<UserWithFriends>() {
                @Override
                public void onChanged(UserWithFriends userWithFriends) {
                    for(Friends friends: userWithFriends.getFriends())
                    {
                        if(friends.getLogin().equals(login)){
                            friend = friends;
                        }
                    }
                    if(friend != null)
                        loginTxt.setText(friend.getLogin());
                }
            });
        }
        else
        {
            SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(getContext());
            login = manager.logged();
            if(login == null || login.isEmpty())
            {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
            accountViewModel.getUser(login).observe(getViewLifecycleOwner(), new Observer<Users>() {
                @Override
                public void onChanged(Users users) {
                    if(users != null) {
                        loginTxt.setText(users.getLogin());
                        nameTxt.setText(users.getName());
                        surnameTxt.setText(users.getSurname());
                        emailTxt.setText(users.getEmail());
                        if(users.getCity()!= null)
                            cityTxt.setText(users.getCity());
                        if(users.getBirth() != null)
                            birthTxt.setText(users.getBirth().toString());
                    }

                }
            });
        }


    }

}