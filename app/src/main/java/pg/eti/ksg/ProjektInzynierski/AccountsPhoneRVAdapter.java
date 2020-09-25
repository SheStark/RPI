package pg.eti.ksg.ProjektInzynierski;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountsPhoneRVAdapter extends RecyclerView.Adapter<AccountsPhoneRVAdapter.ViewHolder> {

    private List<SharedPreferencesLoginData> data;
    private ClickListener listener;

    public interface ClickListener{
        void onItemClick(int position);
        void onItemDelete(int position);
    }

    public void setClickListener(ClickListener listener) {
        this.listener=listener;
    }

    public AccountsPhoneRVAdapter(List<SharedPreferencesLoginData> data){
        this.data=data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater =LayoutInflater.from(context);

        View RVview=inflater.inflate(R.layout.account_phone_recyclerview,parent,false);

        ViewHolder viewHolder = new ViewHolder(RVview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferencesLoginData usersData =data.get(position);

        holder.RVlargeText.setText(usersData.getLogin());
        holder.RVsmallText.setText(usersData.getName()+" "+usersData.getSurname());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView RVlargeText;
        public TextView RVsmallText;
        public ImageView RVdeleteImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            RVlargeText = (TextView) itemView.findViewById(R.id.RVlargeText);
            RVsmallText = (TextView) itemView.findViewById(R.id.RVsmallText);
            RVdeleteImage= (ImageView) itemView.findViewById(R.id.RVdeleteAccount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            RVdeleteImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemDelete(position);
                        }
                    }
                }
            });
        }
    }

}
