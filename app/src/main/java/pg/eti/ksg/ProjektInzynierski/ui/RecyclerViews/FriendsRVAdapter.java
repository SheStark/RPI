package pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.invoke.LambdaConversionException;
import java.util.ArrayList;
import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.R;

public class FriendsRVAdapter extends RecyclerView.Adapter<FriendsRVAdapter.FriendsViewHolder> {

    private List<Friends> friends = new ArrayList<>();
    private FriendClickListener listener;

    public void setListener(FriendClickListener friendClickListener)
    {
        listener =friendClickListener;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_recyclerview,parent,false);

        return new FriendsViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        Friends friend = friends.get(position);
        holder.login.setText(friend.getLogin());
        holder.name.setText(friend.getName() + " " + friend.getSurname());
        holder.email.setText(friend.getEmail());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setFriends(List<Friends> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView login, name, email;
        private FriendClickListener friendListener;


        public FriendsViewHolder(@NonNull View itemView, FriendClickListener friendListener) {
            super(itemView);
            login = itemView.findViewById(R.id.RVFriendLoginTxt);
            name = itemView.findViewById(R.id.RVFriendNameTxt);
            email = itemView.findViewById(R.id.RVFriendEmailTxt);
            this.friendListener = friendListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            friendListener.onFriendClick(getAdapterPosition());
        }
    }
    public interface FriendClickListener{
        void onFriendClick(int position);
    }

}
