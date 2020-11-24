package pg.eti.ksg.ProjektInzynierski.ui.routes;

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

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithRoutes;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.FriendRoutesRVAdapter;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.FriendsRVAdapter;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.MyRoutesRVAdapter;
import pg.eti.ksg.ProjektInzynierski.ui.directions.MapsActivity;

public class RoutesFragment extends Fragment implements FriendRoutesRVAdapter.OnRouteRVListener, MyRoutesRVAdapter.OnMyRouteRVListener {

    private RoutesViewModel mViewModel;
    private String user;
    private List<Routes> froutes;
    private List<Routes> mroutes;

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
        friendsAdapter.setListener(this::onRouteClick);
        // TODO: Use the ViewModel
        mViewModel.getFriendRoutes(user).observe(getViewLifecycleOwner(), new Observer<UserWithRoutes>() {
            @Override
            public void onChanged(UserWithRoutes userWithRoutes) {
                froutes = userWithRoutes.getRoutes();
                friendsAdapter.setRoutes(froutes);
            }
        });

        RecyclerView myRV =getActivity().findViewById(R.id.myRoutesRV);
        myRV.setLayoutManager(new LinearLayoutManager(getContext()));

        MyRoutesRVAdapter myAdapter = new MyRoutesRVAdapter();
        myRV.setAdapter(myAdapter);
        myAdapter.setListener(this::onMyRouteClick);

        mViewModel.getMyRoutes(user).observe(getViewLifecycleOwner(), new Observer<UserWithRoutes>() {
            @Override
            public void onChanged(UserWithRoutes userWithRoutes) {
                mroutes = userWithRoutes.getRoutes();
                myAdapter.setRoutes(mroutes);
            }
        });
    }


    @Override
    public void onRouteClick(int position) {
        Intent intent = new Intent(getContext(), MapsActivity.class);
        intent.putExtra("route",true);
        intent.putExtra("id",froutes.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onMyRouteClick(int position) {
        Intent intent = new Intent(getContext(), MapsActivity.class);
        intent.putExtra("route",false);
        intent.putExtra("id",mroutes.get(position).getId());
        startActivity(intent);
    }
}