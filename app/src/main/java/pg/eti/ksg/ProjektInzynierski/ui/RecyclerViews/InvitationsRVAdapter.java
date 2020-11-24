package pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.R;

public class InvitationsRVAdapter extends RecyclerView.Adapter<InvitationsRVAdapter.InvitationsViewHolder> {

    private List<Invitations> invitations = new ArrayList<>();
    private ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener{
        void onAcceptClick(Invitations invitation);
        void onDismissClick(Invitations invitation);
    }

    @NonNull
    @Override
    public InvitationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invitations_recyclerview,parent,false);

        return new InvitationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationsViewHolder holder, int position) {

        Invitations invitation = invitations.get(position);
        holder.login.setText(invitation.getLogin());
        holder.name.setText(invitation.getName() + " " + invitation.getSurname());
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public void setInvitations(List<Invitations> invitations) {
        this.invitations = invitations;
        notifyDataSetChanged();
    }

    public class InvitationsViewHolder extends RecyclerView.ViewHolder{

        private TextView login, name;
        private ImageView acceptBtn,dismissBtn;

        public InvitationsViewHolder(@NonNull View itemView) {
            super(itemView);
            login = itemView.findViewById(R.id.RVInvitationLoginTxt);
            name = itemView.findViewById(R.id.RVInvitationNameTxt);
            acceptBtn = itemView.findViewById(R.id.RVAcceptFriend);
            dismissBtn = itemView.findViewById(R.id.RVDismissBtn);

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int position = getAdapterPosition();
                     if (position != RecyclerView.NO_POSITION) {
                         listener.onAcceptClick(invitations.get(position));
                     }
                 }
             });

            dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDismissClick(invitations.get(position));
                    }
                }
            });
        }
    }
}
