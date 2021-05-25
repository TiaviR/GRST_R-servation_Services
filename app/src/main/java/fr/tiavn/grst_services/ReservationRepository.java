package fr.tiavn.grst_services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReservationRepository {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("reservations");

    ArrayList<ReservationClass> listReservations = new ArrayList<>();

    private static ReservationRepository instance;

    public ReservationRepository(){
        //Prévient de l'instanciation
    }

    public static ReservationRepository getInstance(){
        if (instance == null){
            instance = new ReservationRepository();
        }
        return instance;
    }

    public ArrayList<ReservationClass> getListReservations() {
        //Singleton singleton = new Singleton();
        return listReservations;
    }

    public interface Callback {
        void callback();
    }

    public void updateData(ReservationRepository.Callback callback) {

        //Singleton singleton = new Singleton();
        //absorber les données depuis la DatabaseRef - liste items
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retirer les anciennes
                listReservations.clear();

                //Récolter la liste
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ReservationClass reservation = ds.getValue(ReservationClass.class);

                    //Vérifier que le service n'est pas nulle
                    if (reservation != null){
                        //ajouter l'item à notre liste
                        listReservations.add(reservation);
                    }
                }
                //Actionner le callback
                callback.callback();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Mettre à jour une réservation en bdd
    public void updateReservation(ReservationClass reservation){
        myRef.child(String.valueOf(reservation.getReservation_id())).setValue(reservation);
    }

    //ajouter une nouvelle réservation
    public void newReservation(ReservationClass reservation){
        myRef.child(String.valueOf(reservation.getReservation_id())).setValue(reservation);
    }

    //Supprimer une réservation de la bdd
    public void deleteReservation(ReservationClass reservation){
        myRef.child(String.valueOf(reservation.getReservation_id())).removeValue();
    }
}
