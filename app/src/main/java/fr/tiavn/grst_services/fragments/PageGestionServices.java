package fr.tiavn.grst_services.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.adapter.PageGestionServicesAdapter;
import fr.tiavn.grst_services.adapter.ReservationsFragmentAdapter;
import fr.tiavn.grst_services.adapter.TopServicesItemDecoration;

public class PageGestionServices extends Fragment {
    Context context;

    public PageGestionServices(){}

    public PageGestionServices(Context context) {this.context = context; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_mes_services, container, false);

        ArrayList<ServiceClass> listService = new ArrayList<>();
        for (ServiceClass service : ServiceRepository.getInstance().getListService()){
            if(service.getFournisseur_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                listService.add(service);
            }
        }

        //Récuperer le RecycleView vertical
        RecyclerView services = view.findViewById(R.id.recycler_view_page_gestion_services);
        services.setAdapter(new PageGestionServicesAdapter(this.context, listService, R.layout.service_item));
        services.addItemDecoration(new TopServicesItemDecoration());

        return view;
    }
}
