package pg.eti.ksg.ProjektInzynierski.ui.routes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithRoutes;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.FriendRoutesRVAdapter;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.MyRoutesRVAdapter;

public class RoutesFragment extends Fragment {

    private RoutesViewModel mViewModel;
    private String user;

    public static RoutesFragment newInstance() {
        return new RoutesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.routes_fragment, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RoutesViewModel.class);

        SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(getContext());
        user = manager.logged();
        if(user == null || user.isEmpty())
            return;

        RecyclerView friendRV =getActivity().findViewById(R.id.friendRoutesRV);
        friendRV.setLayoutManager(new LinearLayoutManager(getContext()));


        FriendRoutesRVAdapter friendsAdapter = new FriendRoutesRVAdapter();
        friendRV.setAdapter(friendsAdapter);
        // TODO: Use the ViewModel
        mViewModel.getFriendRoutes(user).observe(getViewLifecycleOwner(), new Observer<UserWithRoutes>() {
            @Override
            public void onChanged(UserWithRoutes userWithRoutes) {
                friendsAdapter.setRoutes(userWithRoutes.getRoutes());
            }
        });

        RecyclerView myRV =getActivity().findViewById(R.id.myRoutesRV);
        myRV.setLayoutManager(new LinearLayoutManager(getContext()));

        MyRoutesRVAdapter myAdapter = new MyRoutesRVAdapter();
        myRV.setAdapter(myAdapter);

        mViewModel.getMyRoutes(user).observe(getViewLifecycleOwner(), new Observer<UserWithRoutes>() {
            @Override
            public void onChanged(UserWithRoutes userWithRoutes) {
                myAdapter.setRoutes(userWithRoutes.getRoutes());
            }
        });
    }

}