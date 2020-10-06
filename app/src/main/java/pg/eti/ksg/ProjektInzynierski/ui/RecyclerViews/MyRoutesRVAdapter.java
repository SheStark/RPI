package pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews;

import android.content.res.ColorStateList;
import android.graphics.Color;
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

public class MyRoutesRVAdapter extends RecyclerView.Adapter<MyRoutesRVAdapter.MyRoutesViewHolder>{

    private List<Routes> routes = new ArrayList<>();

    @NonNull
    @Override
    public MyRoutesRVAdapter.MyRoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_routes_recyclerview,parent,false);


        return new MyRoutesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRoutesRVAdapter.MyRoutesViewHolder holder, int position) {
        Routes route =routes.get(position);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.routeData.setText(format.format(route.getStartDate()));
        if(!route.isDangerous()) {
            holder.image.setImageResource(R.drawable.ic_baseline_location_on_black);
            holder.status.setText("Planowe udostępnianie");
        }
        else {
            holder.status.setText("Niebezpieczeństwo");
            holder.status.setTextColor(Color.RED);
        }

    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }


    public class MyRoutesViewHolder extends RecyclerView.ViewHolder {

        private TextView status;
        private TextView routeData;
        private ImageView image;

        public MyRoutesViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.RVMyRoutesStatusTxt);
            routeData =itemView.findViewById(R.id.RVMyRoutesDateTxt);
            image = itemView.findViewById(R.id.RVMyRoutesImg);


        }
    }
}
