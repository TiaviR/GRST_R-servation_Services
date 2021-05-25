package fr.tiavn.grst_services.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.ItemHomeModel;
import fr.tiavn.grst_services.MainActivity;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServicePopup;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.Utilisateur;

public class RecentItemAdapter extends RecyclerView.Adapter<RecentItemAdapter.ViewHolder>{
    public Context context;
    //private Fournisseur context;
    //private Utilisateur contextUser;
    //private ArrayList<ItemHomeModel> listItem;
    private ArrayList<ServiceClass> listService;
    private int layoutId;

    public RecentItemAdapter(){

    }

    public RecentItemAdapter(Context context, ArrayList<ServiceClass> listService /*ArrayList<ItemHomeModel> listItem*/, int layoutId) {
        this.context = context;
        //this.listItem = listItem;
        this.listService = listService;
        this.layoutId = layoutId;
    }
    /*
    public RecentItemAdapter(Utilisateur contextUser, ArrayList<ServiceClass> listService, int layoutId) {
        this.context = contextUser;
        //this.listItem = listItem;
        this.listService = listService;
        this.layoutId = layoutId;
    }
     */

    //boîte pour ranger tout les composants à controler
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ImageView imageProfil;
        private TextView pseudo;
        private TextView description;
        private ImageView note_image;
        private TextView note;
        private TextView nb_notes;
        private TextView prix;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //à ajouter

            imageView = (ImageView) itemView.findViewById(R.id.img_frag_service_home);
            imageProfil = (ImageView) itemView.findViewById(R.id.img_profile_home_frag);
            pseudo = (TextView) itemView.findViewById(R.id.text_pseudo_frag_home);
            description = (TextView) itemView.findViewById(R.id.text_description_frag_home);
            note_image = (ImageView) itemView.findViewById(R.id.note_image_frag_home);
            note = (TextView) itemView.findViewById(R.id.note_service_frag_home);
            nb_notes = (TextView) itemView.findViewById(R.id.nb_notes);
            prix = (TextView) itemView.findViewById(R.id.prix_service_frag_home);
        }
        public ImageView getImageView() {
            return imageView;
        }

        public ImageView getImageProfil() {
            return imageProfil;
        }

        public TextView getPseudo() {
            return pseudo;
        }

        public TextView getDescription() {
            return description;
        }

        public ImageView getNote_image() {
            return note_image;
        }

        public TextView getNote() {
            return note;
        }

        public TextView getNb_notes() {
            return nb_notes;
        }

        public TextView getPrix() {
            return prix;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
       // ((ViewHolder) holder).bindData(models.get(position));

        ServiceRepository serviceRepository = new ServiceRepository();
        ServiceClass currentService = listService.get(position);

        String currentURL = listService.get(position).getImage_type_service();
        //String currentURL = listItem.get(position).getImageURL();
        Uri uri = Uri.parse(currentURL);

        //Utiliser Glide pour récupérer l'image à partir de son lien
        try {
            Glide.with(context).load(uri).into(holder.imageView);
            //Glide.with(this.contextUser).load(uri).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Image de profil
/*
        String currentURLprofile = listItem.get(position).getImageProfil();
        Uri uriProfile = Uri.parse(currentURLprofile);

        try {
            Glide.with(context).load(uriProfile).into(holder.getImageProfil());
        } catch (Exception e) {
            e.printStackTrace();
        }

 */

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

        //Mettre à jour la note
        try {
            holder.note.setText(listService.get(position).getNote_moyenne()+"");
            //holder.note.setText(listItem.get(position).getNoteMoyenne()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mettre à jour le nombre de votants
        try {
            holder.nb_notes.setText("("+listService.get(position).getNb_notes()+")");
            //holder.nb_notes.setText("("+listItem.get(position).getNbNotes()+")");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mettre à jour le prix de départ
        try {
            holder.prix.setText(listService.get(position).getCout_horaire());
            //holder.prix.setText(listItem.get(position).getPrixMinimum());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Intéraction lors du clic sur un service
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ServicePopup servicePopup = new ServicePopup(context, currentService);
                servicePopup.show();

                //final Dialog dialog = new Dialog(context);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.setContentView(R.layout.popup_services_details);
                //dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listService.size();
        //return listItem.size();
    }

}
