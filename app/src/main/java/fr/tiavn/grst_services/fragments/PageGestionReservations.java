package fr.tiavn.grst_services.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ReservationClass;
import fr.tiavn.grst_services.ReservationRepository;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.adapter.PageGestionReservationsAdapter;
import fr.tiavn.grst_services.adapter.ReservationsFragmentAdapter;
import fr.tiavn.grst_services.adapter.TopServicesItemDecoration;

public class PageGestionReservations extends Fragment {
    Context context;
/*
    public PageGestionReservations(){
        super();
    }

 */

    public PageGestionReservations(Context context) {
        this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_gestion_reservations, container, false);

        ArrayList<ReservationClass> listeReservations = new ArrayList<>();
        for (ReservationClass reservation : ReservationRepository.getInstance().getListReservations()){
            if(reservation.getClient_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || reservation.getFournisseur_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                listeReservations.add(reservation);
            }
        }

        //Récuperer le RecycleView vertical
        RecyclerView reservationsRecyclerView = view.findViewById(R.id.recycler_view_page_gestion_reservations);
        reservationsRecyclerView.setAdapter(new PageGestionReservationsAdapter(this.context, listeReservations, R.layout.item_page_gestion_reservations));
        reservationsRecyclerView.addItemDecoration(new TopServicesItemDecoration());

        return view;
    }
}
