package fr.tiavn.grst_services.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ReservationClass;
import fr.tiavn.grst_services.ReservationPopupDetails;
import fr.tiavn.grst_services.ReservationRepository;
import fr.tiavn.grst_services.ServiceClass;

public class PageGestionReservationsAdapter extends RecyclerView.Adapter<PageGestionReservationsAdapter.PageGestionReservationsViewHolder>{
    private Context context;
    private ArrayList<ReservationClass> listeReservations;
    private int layoutId;

    public PageGestionReservationsAdapter(Context context, ArrayList<ReservationClass> listeReservations, int layoutId) {
        this.context = context;
        this.listeReservations = listeReservations;
        this.layoutId = layoutId;
    }

    public static class PageGestionReservationsViewHolder extends RecyclerView.ViewHolder {

        private TextView id_reservation;
        private TextView pseudos;
        private TextView date;

        public PageGestionReservationsViewHolder(@NonNull View itemView) {
            super(itemView);

            id_reservation = (TextView) itemView.findViewById(R.id.id_reservation_page_gestion_reservations);
            pseudos = (TextView) itemView.findViewById(R.id.text_fournisseur_client_gestion_reservations);
            date = (TextView) itemView.findViewById(R.id.text_date_periode_gestion_reservations);
        }

        public TextView getId_reservation() {
            return id_reservation;
        }
        public TextView getPseudos() {
            return pseudos;
        }
        public TextView getDate() {
            return date;
        }
    }

    @NonNull
    @Override
    public PageGestionReservationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new PageGestionReservationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageGestionReservationsViewHolder holder, int position) {

        ReservationRepository repository = new ReservationRepository();
        ReservationClass currentReservation = listeReservations.get(position);

        try {
            holder.id_reservation.setText(currentReservation.getReservation_id());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.pseudos.setText(currentReservation.getFournisseur_pseudo()+" - "+currentReservation.getClient_pseudo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.date.setText(currentReservation.getDate()+" - "+currentReservation.getPeriode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ReservationPopupDetails popupDetails = new ReservationPopupDetails(context, currentReservation);
                popupDetails.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listeReservations.size();
    }
}
