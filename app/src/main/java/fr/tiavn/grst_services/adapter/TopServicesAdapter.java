package fr.tiavn.grst_services.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServicePopup;

public class TopServicesAdapter extends RecyclerView.Adapter<TopServicesAdapter.TopServicesHolder>{
    private Context context;
    private ArrayList<ServiceClass> listService;
    private int layoutId;

    public TopServicesAdapter(){

    }

    public TopServicesAdapter(Context context, ArrayList<ServiceClass> listService /*ArrayList<ItemHomeModel> listItem*/, int layoutId) {
        this.context = context;
        //this.listItem = listItem;
        this.listService = listService;
        this.layoutId = layoutId;
    }

    public static class TopServicesHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView pseudo;
        private TextView description;
        private TextView note_moyenne;

        public TopServicesHolder(@NonNull View itemView) {
            super(itemView);
            //à ajouter

            image = (ImageView) itemView.findViewById(R.id.img_top_service);
            pseudo = (TextView) itemView.findViewById(R.id.text_pseudo_top_services);
            description = (TextView) itemView.findViewById(R.id.text_description_top_services);
            note_moyenne = (TextView) itemView.findViewById(R.id.note_moyenne_top_service);
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getPseudo() {
            return pseudo;
        }

        public TextView getDescription() {
            return description;
        }
    }

    @NonNull
    @Override
    public TopServicesAdapter.TopServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new TopServicesAdapter.TopServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopServicesAdapter.TopServicesHolder holder, int position) {
        //Image de profil
        String currentURLprofile = listService.get(position).getImage_type_service();
        Uri uriProfile = Uri.parse(currentURLprofile);

        ServiceClass currentService = listService.get(position);

        try {
            Glide.with(context).load(uriProfile).into(holder.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mettre à jour le pseudo
        //ItemHomeModel currentItem = listItem.get(position);
        try {
            holder.pseudo.setText(currentService.getPseudo_user());
            //holder.pseudo.setText(listItem.get(position).getPseudo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mettre à jour la description
        try {
            holder.description.setText(currentService.getDescription_service());
            //holder.description.setText(listItem.get(position).getDescriptionService());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.note_moyenne.setText(currentService.getNote_moyenne()+"");
            //holder.description.setText(listItem.get(position).getDescriptionService());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Intéraction lors du clic sur un service
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ServicePopup servicePopup = new ServicePopup(context, currentService);
                servicePopup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
