package pg.eti.ksg.ProjektInzynierski.ui.invitations;

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

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.ClickListeners.InvitationAdapterClickListener;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithInvitations;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.InvitationsRVAdapter;

public class invitations extends Fragment {

    private InvitationsViewModel mViewModel;
    private String user;
    private List<Invitations> invitationsList;

    public static invitations newInstance() {
        return new invitations();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invitations_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InvitationsViewModel.class);
        // TODO: Use the ViewModel

        SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(getContext());
        user = manager.logged();
        if(user == null || user.isEmpty())
            return;


        RecyclerView invitationsRV = getActivity().findViewById(R.id.RVInvitations);
        invitationsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        InvitationsRVAdapter adapter =new InvitationsRVAdapter();
        invitationsRV.setAdapter(adapter);
        adapter.setListener(new InvitationAdapterClickListener(getActivity().getApplication(),getContext(),user));

        mViewModel.getInvitations(user).observe(getViewLifecycleOwner(), new Observer<UserWithInvitations>() {
            @Override
            public void onChanged(UserWithInvitations userWithInvitations) {
                invitationsList = userWithInvitations.getInvitations();
                adapter.setInvitations(invitationsList);
            }
        });
    }

}