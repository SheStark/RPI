package pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.R;

public class FriendRoutesRVAdapter extends RecyclerView.Adapter<FriendRoutesRVAdapter.FriendViewHolder> {

    private List<Routes> routes = new ArrayList<>();

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_routes_recyclerview,parent,false);


        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Routes route =routes.get(position);
        holder.friendLogin.setText(route.getFriendLogin());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.routeData.setText(format.format(route.getStartDate()));
        if(!route.isDangerous())
            holder.image.setImageResource(R.drawable.ic_baseline_location_on_black);

    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }


    public class FriendViewHolder extends RecyclerView.ViewHolder {

        private TextView friendLogin;
        private TextView routeData;
        private ImageView image;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendLogin = itemView.findViewById(R.id.RVFriendRoutesLogin);
            routeData =itemView.findViewById(R.id.RVFriendRoutesDateTxt);
            image = itemView.findViewById(R.id.RVFriendRoutesImg);


        }
    }
}
