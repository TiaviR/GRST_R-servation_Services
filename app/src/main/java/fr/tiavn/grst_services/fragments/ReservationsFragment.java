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

import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.adapter.RecentItemAdapter;
import fr.tiavn.grst_services.adapter.ReservationsFragmentAdapter;
import fr.tiavn.grst_services.adapter.TopServicesItemDecoration;

public class ReservationsFragment extends Fragment {
    private Context context;

    public ReservationsFragment(){
    }

    public ReservationsFragment(Context context) {
        this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reservations, container, false);

        ArrayList<ServiceClass> listService = new ArrayList<>();
        for (ServiceClass service : ServiceRepository.getInstance().getListService()){
            if(service.getFavori()){
                listService.add(service);
            }
        }

        //Récuperer le RecycleView vertical
        RecyclerView reservationsRecyclerView = view.findViewById(R.id.recycler_view_reservations_fragment);
        reservationsRecyclerView.setAdapter(new ReservationsFragmentAdapter(this.context, listService /*ServiceRepository.getInstance().getListService()*//*ItemHomeRepository.getInstance().getListItem()*/, R.layout.item_reservations_fragment));
        reservationsRecyclerView.addItemDecoration(new TopServicesItemDecoration());

        return view;
    }
}
