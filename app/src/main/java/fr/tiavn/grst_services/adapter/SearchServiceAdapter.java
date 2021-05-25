package fr.tiavn.grst_services.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.tiavn.grst_services.DomaineMetier;
import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.ItemHomeModel;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.fragments.ReservationsFragment;
import fr.tiavn.grst_services.fragments.ResultSearchFragment;

public class SearchServiceAdapter extends RecyclerView.Adapter<SearchServiceAdapter.SearchServiceViewHolder> implements Filterable {
    //private Fournisseur context;
    Context context;
    private ArrayList<DomaineMetier> domaineMetiers;
    private ArrayList<DomaineMetier> domaineMetierArrayList;
    private int layoutId;

    public SearchServiceAdapter(Context context, ArrayList<DomaineMetier> domaineMetiers, int layoutId){
        this.context = context;
        this.domaineMetiers = domaineMetiers;
        domaineMetierArrayList = new ArrayList<>();
        domaineMetierArrayList.addAll(domaineMetiers);
        this.layoutId = layoutId;
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    @NonNull
    @Override
    public SearchServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new SearchServiceAdapter.SearchServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchServiceViewHolder holder, int position) {
        ServiceRepository serviceRepository = new ServiceRepository();
        DomaineMetier currentDomaine = domaineMetiers.get(position);

        String currentURL = domaineMetiers.get(position).getDomaine_metier_image();
        Uri uri = Uri.parse(currentURL);

        try {
            Glide.with(context).load(uri).into(holder.searchServiceImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.nomSearchService.setText(domaineMetiers.get(position).getDomaine_metier_nom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DatabaseReference databaseReferenceUtilisateur = FirebaseDatabase.getInstance().getReference("utilisateurs");
               DatabaseReference databaseReferenceFournisseur = FirebaseDatabase.getInstance().getReference("fournisseurs");
                /*
                ServiceRepository serviceRepository = new ServiceRepository();
                serviceRepository = ServiceRepository.getInstance();

                serviceRepository.updateData(new ServiceRepository.Callback() {
                    @Override
                    public void callback() {
                        //injecter le fragment
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        ResultSearchFragment services = new ResultSearchFragment(context, currentDomaine);
                        transaction.replace(R.id.fournisseur_fragment, services);
                        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

                 */
                databaseReferenceUtilisateur.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            ResultSearchFragment fragment = new ResultSearchFragment(context, currentDomaine);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.utilisateur_fragment, fragment).addToBackStack(null).commit();
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReferenceFournisseur.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            ResultSearchFragment fragment = new ResultSearchFragment(context, currentDomaine);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fournisseur_fragment, fragment).addToBackStack(null).commit();
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                /*
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ResultSearchFragment fragment = new ResultSearchFragment(context, currentDomaine);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fournisseur_fragment, fragment).addToBackStack(null).commit();
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

                 */
            }
        });

    }

    @Override
    public int getItemCount() {
        return domaineMetiers.size();
    }


    public static class SearchServiceViewHolder extends RecyclerView.ViewHolder {
        private ImageView searchServiceImage;
        private TextView nomSearchService;

        public SearchServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            searchServiceImage = (ImageView) itemView.findViewById(R.id.img_search_services);
            nomSearchService = (TextView) itemView.findViewById(R.id.text_nom_search_service);
        }

        public ImageView getSearchServiceImage() {
            return searchServiceImage;
        }

        public TextView getNomSearchService() {
            return nomSearchService;
        }
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DomaineMetier> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(domaineMetierArrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (DomaineMetier domaine : domaineMetierArrayList) {

                    if (domaine.getDomaine_metier_nom().toLowerCase().contains(filterPattern)) {
                        filteredList.add(domaine);
                    }

                    /*
                    if (domaine.getDomaine_metier_nom().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(domaine);
                    }

                     */
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            domaineMetiers.clear();
            domaineMetiers.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
/*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        //**************ViewHolder Build Pattern Start **************
        final SearchServiceViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, parent, false);
            holder = new SearchServiceViewHolder();

            holder.searchServiceImage = convertView.findViewById(R.id.img_search_services);
            holder.nomSearchService = convertView.findViewById(R.id.text_nom_search_service);

            convertView.setTag(holder);
        }
        else {
            holder = (SearchServiceViewHolder) convertView.getTag();
        }

        //String currentURL = listService.get(position).getImage_type_service();
        //String currentURL = listItem.get(position).getImageURL();
        //Uri uri = Uri.parse(currentURL);
        String currentURL = getItem(position).getDomaine_metier_image();
        Uri uri = Uri.parse(currentURL);

        try {
            Glide.with(context).load(uri).into(holder.searchServiceImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String domaineNom = getItem(position).getDomaine_metier_nom();
        holder.nomSearchService.setText(domaineNom);

        return convertView;
    }

    //filtrer la barre de recherche
    public void filter(String characterText){
        characterText = characterText.toLowerCase(Locale.getDefault());
        domaineMetiers.clear();
        if (characterText.length() == 0){
            domaineMetiers.addAll(domaineMetierArrayList);
        } else {
            domaineMetiers.clear();
            for (DomaineMetier domaine: domaineMetierArrayList) {
                if (domaine.getDomaine_metier_nom().toLowerCase(Locale.getDefault()).contains(characterText)){
                    domaineMetiers.add(domaine);
                }
            }
        }
        notifyDataSetChanged();
    }
}

 */
