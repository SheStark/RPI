package pg.eti.ksg.ProjektInzynierski.ui.addFriend;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import pg.eti.ksg.ProjektInzynierski.AlertDialogs;
import pg.eti.ksg.ProjektInzynierski.Models.MessageCodes;
import pg.eti.ksg.ProjektInzynierski.Models.ResponseModel;
import pg.eti.ksg.ProjektInzynierski.R;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFriend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriend extends Fragment {

    private TextInputLayout invitationLogin;
    private Button sendInvitation;
    private String user;

    public static AddFriend newInstance() {
        return new AddFriend();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_friend, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferencesLoginManager manager = new SharedPreferencesLoginManager(getContext());
        user = manager.logged();
        if(user == null || user.isEmpty())
            return;

        invitationLogin = getActivity().findViewById(R.id.friendLoginTxt);
        sendInvitation = getActivity().findViewById(R.id.sendInvitationBtn);

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