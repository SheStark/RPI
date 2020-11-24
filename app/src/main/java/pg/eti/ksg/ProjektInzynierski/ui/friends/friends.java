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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import pg.eti.ksg.ProjektInzynierski.AlertDialogs;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.Models.MessageCodes;
import pg.eti.ksg.ProjektInzynierski.Models.ResponseModel;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.FriendsRVAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class friends extends Fragment {

    private FriendsViewModel mViewModel;
    private String user;
    private TextInputLayout invitationLogin;
    private Button sendInvitation;


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



        SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(getContext());
        user = manager.logged();
        if(user == null || user.isEmpty())
            return;

        invitationLogin = getActivity().findViewById(R.id.friendLoginTxt);
        sendInvitation = getActivity().findViewById(R.id.sendInvitationBtn);

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

        sendInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = invitationLogin.getEditText().getText().toString().trim();
                if(!login.isEmpty())
                {
                    ServerApi api = ServerClient.getClient();
                    Call<ResponseModel> call =api.sendInvitation(user,login);
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if(!response.isSuccessful())
                                AlertDialogs.serverError(getContext());
                            else if(response.body().getCode() == MessageCodes.OK.getCode())
                                Toast.makeText(getContext(),"Zaproszenie zostało wysłane",Toast.LENGTH_LONG).show();
                            else if(response.body().getCode() == MessageCodes.INVALIDLOGIN.getCode())
                                Toast.makeText(getContext(),"Nieprawidłowy login",Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getContext(),"Zaproszenie zostało już wcześniej wysłane",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            AlertDialogs.networkError(getContext());
                        }
                    });
                }
                else
                    Toast.makeText(getContext(),"Wpisz login w odpowiednim polu",Toast.LENGTH_LONG).show();

            }
        });

    }

}