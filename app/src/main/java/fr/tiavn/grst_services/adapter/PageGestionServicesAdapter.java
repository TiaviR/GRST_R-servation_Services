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
import fr.tiavn.grst_services.ServiceRepository;

public class PageGestionServicesAdapter extends RecyclerView.Adapter<PageGestionServicesAdapter.PageGestionServicesHolder>{
    private Context context;
    private ArrayList<ServiceClass> listService;
    private int layoutId;

    public PageGestionServicesAdapter(Context context, ArrayList<ServiceClass> listService, int layoutId) {
        this.context = context;
        this.listService = listService;
        this.layoutId = layoutId;
    }

    public static class PageGestionServicesHolder extends RecyclerView.ViewHolder {
        private ImageView imageDomaine;
        private TextView nomDomaine;
        private TextView pseudo;
        private TextView description;

        public PageGestionServicesHolder(@NonNull View itemView) {
            super(itemView);

            imageDomaine = (ImageView) itemView.findViewById(R.id.image_item_service);
            nomDomaine = (TextView) itemView.findViewById(R.id.nom_domaine_item_service);
            pseudo = (TextView) itemView.findViewById(R.id.pseudo_fournisseur_item_service);
            description = (TextView) itemView.findViewById(R.id.description_item_service);
        }

        public ImageView getImageDomaine() {
            return imageDomaine;
        }

        public TextView getNomDomaine() {
            return nomDomaine;
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
    public PageGestionServicesAdapter.PageGestionServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new PageGestionServicesAdapter.PageGestionServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageGestionServicesAdapter.PageGestionServicesHolder holder, int position) {
        ServiceRepository serviceRepository = new ServiceRepository();
        ServiceClass currentService = listService.get(position);

        String currentURL = listService.get(position).getImage_type_service();
        Uri uri = Uri.parse(currentURL);

        //Utiliser Glide pour récupérer l'image à partir de son lien
        try {
            Glide.with(context).load(uri).into(holder.imageDomaine);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.nomDomaine.setText(listService.get(position).getType_service());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.pseudo.setText(listService.get(position).getPseudo_user());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.description.setText(listService.get(position).getDescription_service());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    }
}
