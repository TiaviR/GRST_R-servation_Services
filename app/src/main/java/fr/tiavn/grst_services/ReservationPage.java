package fr.tiavn.grst_services;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class ReservationPage extends Dialog implements AdapterView.OnItemSelectedListener {
    private ServiceClass currentService;

    private Button buttonDate;
    private EditText editTextDate;

    private Spinner spinnerPeriode;

    private Button confirmer;
    private Button annuler;


    private int annee;
    private int mois;
    private int jour;

    public ReservationPage(@NonNull Context context, ServiceClass service) {
        super(context);
        this.currentService = service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.page_reservation);

        //*******Date*********
        editTextDate = findViewById(R.id.editText_page_reservation);
        buttonDate = findViewById(R.id.button_choisir_date_page_reservation);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        //Récuperer la date
        final Calendar c = Calendar.getInstance();
        this.annee = c.get(Calendar.YEAR);
        this.mois = c.get(Calendar.MONTH);
        this.jour = c.get(Calendar.DAY_OF_MONTH);

        //******Période de la journée******
        spinnerPeriode = findViewById(R.id.spinner_page_reservation);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.choix_periode_journée, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPeriode.setAdapter(adapter);
        spinnerPeriode.setOnItemSelectedListener(this);

        //********Confirmer la réservation*****************
        confirmer = findViewById(R.id.button_reserver_page_reservation);
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_reservation = editTextDate.getText().toString();
                String periode = spinnerPeriode.getSelectedItem().toString();

                AlertDialog.Builder myPopUp = new AlertDialog.Builder(getContext());
                myPopUp.setTitle("Confirmation");
                myPopUp.setMessage("Souhaitez-vous réserver ce service ?");

                myPopUp.setPositiveButton("OUI", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(date_reservation)) {
                            editTextDate.setError("Veuillez saisir une date");
                        }else {
                            reserverService(date_reservation, periode);
                        }
                    }
                });
                myPopUp.setNegativeButton("NON", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Non réservé", Toast.LENGTH_SHORT).show();
                    }
                });
                myPopUp.show();
            }
        });

        annuler = findViewById(R.id.button_annuler_page_reservation);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(annuler)){
                    dismiss();
                }
            }
        });
    }

    private void reserverService(String date_reservation, String periode) {
        DatabaseReference databaseReferenceUtilisateur = FirebaseDatabase.getInstance().getReference("utilisateurs");
        DatabaseReference databaseReferenceFournisseur = FirebaseDatabase.getInstance().getReference("fournisseurs");

        final String[] pseudo_client_reseration = {""};
        databaseReferenceUtilisateur.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    pseudo_client_reseration[0] = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("utilisateur_pseudo").getValue(String.class);
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
                    pseudo_client_reseration[0] = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pseudo_fournisseur").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reservations");

        ReservationRepository repository = new ReservationRepository();

        myRef.orderByChild("service_id").equalTo(currentService.getService_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean reservation_possible = true;
                if (!snapshot.exists()){
                    //Réserver le service
                    ReservationClass newReservation = new ReservationClass(
                            UUID.randomUUID().toString(),
                            currentService.getService_id(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            pseudo_client_reseration[0],
                            currentService.getFournisseur_id(),
                            currentService.getPseudo_user(),
                            currentService.getDescription_service(),
                            date_reservation,
                            periode
                    );

                    //Envoyer en BDD
                    repository.newReservation(newReservation);
                    Toast.makeText(getContext(), "Service réservé !", Toast.LENGTH_SHORT).show();
                }else {
                    for (DataSnapshot ds : snapshot.getChildren()){
                        ReservationClass reservation = ds.getValue(ReservationClass.class);

                        if (reservation.getDate().equals(date_reservation) && reservation.getPeriode().equals(periode)){
                            ((TextView)spinnerPeriode.getSelectedView()).setError("Période de la journée déjà occupée pour ce jour");
                            Toast.makeText(getContext(), "Veuillez choisir un autre moment de la journée ou réserver pour un autre jour", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    ReservationClass newReservation = new ReservationClass(
                            UUID.randomUUID().toString(),
                            currentService.getService_id(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            pseudo_client_reseration[0],
                            currentService.getFournisseur_id(),
                            currentService.getPseudo_user(),
                            currentService.getDescription_service(),
                            date_reservation,
                            periode
                    );

                    //Envoyer en BDD
                    repository.newReservation(newReservation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReservationClass newReservation = new ReservationClass(
                        UUID.randomUUID().toString(),
                        currentService.getService_id(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        pseudo_client_reseration[0],
                        currentService.getFournisseur_id(),
                        currentService.getPseudo_user(),
                        currentService.getDescription_service(),
                        date_reservation,
                        periode
                );
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ReservationClass reservation = dataSnapshot.getValue(ReservationClass.class);

                    if (reservation != null){
                        if (reservation.getService_id().equals(currentService.getService_id()) && reservation.getDate().equals(date_reservation) && reservation.getPeriode().equals(periode)){
                            ((TextView)spinnerPeriode.getSelectedView()).setError("Période de la journée déjà occupée pour ce jour");
                            Toast.makeText(getContext(), "Veuillez choisir un autre moment de la journée ou réserver pour un autre jour", Toast.LENGTH_SHORT).show();
                        }else {
                            repository.newReservation(newReservation);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
     */
        /*
        ArrayList<ReservationClass> listeReservations = new ArrayList<>();
        for (ReservationClass reservation : ReservationRepository.getInstance().getListReservations()){
            if (reservation.getService_id().equals(currentService.getService_id()) && reservation.getDate().equals(date_reservation) && reservation.getPeriode().equals(periode)){
                ((TextView)spinnerPeriode.getSelectedView()).setError("Période de la journée déjà occupée pour ce jour");
                Toast.makeText(getContext(), "Veuillez choisir un autre moment de la journée ou réserver pour un autre jour", Toast.LENGTH_SHORT).show();
            }else {
                ReservationClass newReservation = new ReservationClass(
                        UUID.randomUUID().toString(),
                        currentService.getService_id(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        pseudo_client_reseration[0],
                        currentService.getFournisseur_id(),
                        currentService.getPseudo_user(),
                        currentService.getDescription_service(),
                        date_reservation,
                        periode
                );

                repository.newReservation(newReservation);
            }
        }
         */
        /*
        myRef.orderByChild("service_id").equalTo(currentService.getService_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()){
                    //Réserver le service
                    ReservationClass newReservation = new ReservationClass(
                            UUID.randomUUID().toString(),
                            currentService.getService_id(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            pseudo_client_reseration[0],
                            currentService.getFournisseur_id(),
                            currentService.getPseudo_user(),
                            currentService.getDescription_service(),
                            date_reservation,
                            periode
                    );

                    //Envoyer en BDD
                    repository.newReservation(newReservation);
                }else if (snapshot.child("date").getValue(String.class) == date_reservation){
                    if (snapshot.child("periode").getValue(String.class) == periode){
                        ((TextView)spinnerPeriode.getSelectedView()).setError("Période de la journée déjà occupée pour ce jour");
                        Toast.makeText(getContext(), "Veuillez choisir un autre moment de la journée ou réserver pour un autre jour", Toast.LENGTH_SHORT).show();
                    }else {
                        ReservationClass newReservation = new ReservationClass(
                                UUID.randomUUID().toString(),
                                currentService.getService_id(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                pseudo_client_reseration[0],
                                currentService.getFournisseur_id(),
                                currentService.getPseudo_user(),
                                currentService.getDescription_service(),
                                date_reservation,
                                periode
                        );

                        repository.newReservation(newReservation);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    */

    private void selectDate() {
        //Date Select Listener
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                annee = year;
                mois = monthOfYear;
                jour = dayOfMonth;
            }
        };

        // Calendar Mode (Default):
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                dateSetListener, annee, mois, jour);

        // Show
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
