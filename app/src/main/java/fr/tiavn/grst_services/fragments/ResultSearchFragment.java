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

import java.util.ArrayList;

import fr.tiavn.grst_services.DomaineMetier;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.adapter.PageGestionServicesAdapter;
import fr.tiavn.grst_services.adapter.ReservationsFragmentAdapter;
import fr.tiavn.grst_services.adapter.TopServicesItemDecoration;

public class ResultSearchFragment extends Fragment {
    Context context;
    DomaineMetier domaineMetier;

    public ResultSearchFragment(){ }

    public ResultSearchFragment(Context context, DomaineMetier domaineMetier) {
        this.context = context;
        this.domaineMetier = domaineMetier;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_result_search_service, container, false);

        ArrayList<ServiceClass> listService = new ArrayList<>();
        for (ServiceClass service : ServiceRepository.getInstance().getListService()){
            if(service.getType_service().equals(domaineMetier.getDomaine_metier_nom())){
                listService.add(service);
            }
        }

        //Récuperer le RecycleView vertical
        RecyclerView services = view.findViewById(R.id.recycler_view_page_result_search);
        services.setAdapter(new PageGestionServicesAdapter(this.context, listService, R.layout.service_item));
        services.addItemDecoration(new TopServicesItemDecoration());

        return view;
    }
}
