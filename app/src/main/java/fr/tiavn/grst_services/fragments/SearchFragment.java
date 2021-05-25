package fr.tiavn.grst_services.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import fr.tiavn.grst_services.DomaineMetier;
import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.ItemHomeModel;
import fr.tiavn.grst_services.ItemHomeRepository;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.adapter.RecentItemAdapter;
import fr.tiavn.grst_services.adapter.SearchServiceAdapter;
import fr.tiavn.grst_services.adapter.TopServicesItemDecoration;

public class SearchFragment extends Fragment /*implements SearchView.OnQueryTextListener*/ {
    //private Fournisseur context;
    Context context;
    private SearchServiceAdapter adapter;


    public SearchFragment(){
    }

    public SearchFragment(Context context) {
        this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = view.findViewById(R.id.search_bar);

        //ArrayList<ServiceClass> listService = ServiceRepository.getInstance().getListService();

        ArrayList<DomaineMetier> domaineMetiers = new ArrayList<>();

        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2017/07/19/08/50/gardening-2518377__340.jpg", "Agriculture"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2020/01/23/09/17/leaf-4787248__340.jpg", "Agroalimentaire -  Alimentation"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2015/10/12/14/57/dogs-984015__340.jpg", "Animaux"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2021/04/07/16/30/painting-6159592__340.jpg","Architecture - Aménagement Intérieur"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2020/06/09/03/08/craft-5276736__340.jpg", "Artisanat - Métiers d'art"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2016/10/09/19/19/coins-1726618__340.jpg", "Banque - Finance - Assurance"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2016/12/09/17/35/worker-1895691__340.jpg", "Bâtiment - Travaux publics"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2018/01/28/10/08/purchase-3113198__340.jpg","Commerce - Immobilier"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2016/03/09/09/22/workplace-1245776__340.jpg","Communication - Information"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2017/04/11/22/56/stage-2223130__340.jpg","Culture - Spectacle"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2015/07/31/15/01/security-869216__340.jpg","Défense - Sécurité - Secours"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2015/04/12/16/33/hammer-719066__340.jpg","Droit"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2017/08/06/22/01/books-2596809__340.jpg","Edition - Imprimerie - Livre"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2020/11/13/08/37/pc-5737958__340.jpg","Informatique - Electronique"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2018/10/22/18/02/teacher-3765909__340.jpg","Enseignement - Formation"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2014/04/17/23/26/environmental-protection-326923__340.jpg","Environnement - Nature - Nettoyage"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2018/07/06/16/57/the-labour-code-3520806__340.jpg","Gestion - Audit - Ressources humaines"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2014/09/17/20/26/restaurant-449952__340.jpg","Hôtellerie - Restauration - Tourisme"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2018/02/04/09/03/migration-3129340__340.jpg","Humanitaire"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2012/11/28/10/32/welding-67640__340.jpg","Industrie - Matériaux"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2016/02/19/10/12/writing-1209121__340.jpg","Lettres - Sciences Humaines"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2014/09/13/21/46/milling-444493__340.jpg","Mécanique - Maintenance"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2015/03/26/09/43/camera-690163__340.jpg","Numérique - Multimédia - Audiovisuel"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2016/02/29/15/01/doctor-1228627__340.jpg","Santé"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2017/10/31/20/57/hands-2906458__340.jpg","Social - Service à la personne"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2020/05/14/12/37/barber-5194406_960_720.jpg","Soin - Esthétique - Coiffure"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2017/04/25/20/18/sport-2260736_960_720.jpg","Sport et Animation"));
        domaineMetiers.add(new DomaineMetier("https://cdn.pixabay.com/photo/2015/11/07/11/08/truck-1030846__340.jpg","Transport - Logistique"));

        RecyclerView searchRecyclerView = view.findViewById(R.id.recycler_view_search_fragment);
        adapter = new SearchServiceAdapter(context, domaineMetiers, R.layout.item_search_services);
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.addItemDecoration(new TopServicesItemDecoration());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null && newText != null){
                    adapter.getFilter().filter(newText);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
/*
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (adapter != null && newText != null){
            adapter.getFilter().filter(newText);
            return true;
        }
        return false;
    }

 */
}
