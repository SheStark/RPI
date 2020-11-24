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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.R;

public class FriendRoutesRVAdapter extends RecyclerView.Adapter<FriendRoutesRVAdapter.FriendViewHolder> {

    private List<Routes> routes = new ArrayList<>();
    private OnRouteRVListener listener;

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_routes_recyclerview,parent,false);


        return new FriendViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
            Routes route = routes.get(position);
            holder.friendLogin.setText(route.getFriendLogin());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            holder.routeData.setText(format.format(route.getStartDate()));
            if (!route.isDangerous())
                holder.image.setImageResource(R.drawable.ic_baseline_location_on_black);

    }

    public void setRoutes(List<Routes> routes) {
        Collections.sort(routes, new Comparator<Routes>() {
            @Override
            public int compare(Routes o1, Routes o2) {
                return o2.getStartDate().compareTo(o1.getStartDate());
            }
        });
        this.routes = routes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (routes.size()>10){
            return 10;
        }
        return routes.size();
    }

    public void setListener(OnRouteRVListener onRouteRVListener)
    {
        listener = onRouteRVListener;
    }


    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView friendLogin;
        private TextView routeData;
        private ImageView image;
        private OnRouteRVListener onRouteRVListener;

        public FriendViewHolder(@NonNull View itemView,OnRouteRVListener onRouteRVListener) {
            super(itemView);
            friendLogin = itemView.findViewById(R.id.RVFriendRoutesLogin);
            routeData =itemView.findViewById(R.id.RVFriendRoutesDateTxt);
            image = itemView.findViewById(R.id.RVFriendRoutesImg);
            this.onRouteRVListener = onRouteRVListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onRouteRVListener.onRouteClick(getAdapterPosition());
        }
    }

    public interface OnRouteRVListener{
        void onRouteClick(int position);
    }
}
