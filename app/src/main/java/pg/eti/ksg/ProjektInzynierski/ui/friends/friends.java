package pg.eti.ksg.ProjektInzynierski.ui.friends;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.FriendsRVAdapter;
import pg.eti.ksg.ProjektInzynierski.ui.addFriend.AddFriend;

public class friends extends Fragment {

    private FriendsViewModel mViewModel;
    private FloatingActionButton fab;
    private String user;

    public static friends newInstance() {
        return new friends();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friends_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        // TODO: Use the ViewModel
        fab = getActivity().findViewById(R.id.AddFriendFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriend fragment = new AddFriend();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.friends_layout,fragment,"TAG")
                        .addToBackStack(null)
                        .commit();
            }
        });

        SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(getContext());
        user = manager.logged();
        if(user == null || user.isEmpty())
            return;

        RecyclerView friendRV = getActivity().findViewById(R.id.RVFriends);
        friendRV.setLayoutManager(new LinearLayoutManager(getContext()));

        FriendsRVAdapter adapter =new FriendsRVAdapter();
        friendRV.setAdapter(adapter);

        mViewModel.getFriends(user).observe(getViewLifecycleOwner(), new Observer<UserWithFriends>() {
            @Override
            public void onChanged(UserWithFriends userWithFriends) {
                adapter.setFriends(userWithFriends.getFriends());
            }
        });

    }

}