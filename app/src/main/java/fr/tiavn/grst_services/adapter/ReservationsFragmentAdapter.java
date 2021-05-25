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

import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServicePopup;
import fr.tiavn.grst_services.ServiceRepository;

public class ReservationsFragmentAdapter extends RecyclerView.Adapter<ReservationsFragmentAdapter.ReservationsFragmentViewHolder>{
    private Context context;
    //private ArrayList<ItemHomeModel> listItem;
    private ArrayList<ServiceClass> listService;
    private int layoutId;

    public ReservationsFragmentAdapter(){

    }

    public ReservationsFragmentAdapter(Context context, ArrayList<ServiceClass> listService /*ArrayList<ItemHomeModel> listItem*/, int layoutId) {
        this.context = context;
        //this.listItem = listItem;
        this.listService = listService;
        this.layoutId = layoutId;
    }

    //boîte pour ranger tout les composants à controler
    public static class ReservationsFragmentViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView pseudo;
        private TextView description;

        public ReservationsFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            //à ajouter

            image = (ImageView) itemView.findViewById(R.id.img_profile_reservations_frag);
            pseudo = (TextView) itemView.findViewById(R.id.text_pseudo_reservations_frag);
            description = (TextView) itemView.findViewById(R.id.text_description_reservations_frag);
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

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ReservationsFragmentAdapter.ReservationsFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new ReservationsFragmentAdapter.ReservationsFragmentViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ReservationsFragmentAdapter.ReservationsFragmentViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        // ((ViewHolder) holder).bindData(models.get(position));
        ServiceClass currentService = listService.get(position);

        //Image de profil
        String currentURLprofile = listService.get(position).getImage_type_service();
        Uri uriProfile = Uri.parse(currentURLprofile);

        try {
            Glide.with(context).load(uriProfile).into(holder.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mettre à jour le pseudo
        //ItemHomeModel currentItem = listItem.get(position);
        try {
            holder.pseudo.setText(listService.get(position).getPseudo_user());
            //holder.pseudo.setText(listItem.get(position).getPseudo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mettre à jour la description
        try {
            holder.description.setText(listService.get(position).getDescription_service());
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
        return listService.size();
        //return listItem.size();
    }
}
