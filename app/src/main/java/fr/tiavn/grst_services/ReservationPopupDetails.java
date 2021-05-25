package fr.tiavn.grst_services;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ReservationPopupDetails extends Dialog implements AdapterView.OnItemSelectedListener {

    private ReservationClass currentReservation;

    EditText select_Date;
    Spinner selectPeriode;
    Button confirmer;

    private int annee;
    private int mois;
    private int jour;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("reservations");

    public ReservationPopupDetails(@NonNull Context context, ReservationClass currentReservation) {
        super(context);
        this.currentReservation = currentReservation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_reservations_details);
        setupCompenent();
        setupCloseButton();
        setupModification();
        setupAnnulation();
    }

    private void setupAnnulation() {
        Button annuler = findViewById(R.id.btn_annuler);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationRepository reservation = new ReservationRepository();

                AlertDialog.Builder myPopUp = new AlertDialog.Builder(getContext());
                myPopUp.setTitle("Confirmation");
                myPopUp.setMessage("Voulez-vous vraiment annuler cette réservation ?");
                myPopUp.setPositiveButton("OUI", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (v.equals(annuler)){
                            reservation.deleteReservation(currentReservation);
                            dismiss();
                        }
                    }
                });
                myPopUp.setNegativeButton("NON", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                myPopUp.show();
            }
        });
    }

    private void setupModification() {
        select_Date = findViewById(R.id.editText_popup_reservation_details);
        select_Date.setClickable(true);
        selectPeriode = findViewById(R.id.spinner_popup_reservation_details);
        confirmer = findViewById(R.id.btn_confirmer_popup_reservation_details);
        Button modifier = findViewById(R.id.btn_modifier);
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myPopUp = new AlertDialog.Builder(getContext());
                myPopUp.setTitle("Confirmation");
                myPopUp.setMessage("Voulez-vous vraiment modifier cette réservation ?");
                myPopUp.setPositiveButton("OUI", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select_Date.setVisibility(View.VISIBLE);
                        selectPeriode.setVisibility(View.VISIBLE);
                        confirmer.setVisibility(View.VISIBLE);
                    }
                });
                myPopUp.setNegativeButton("NON", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                myPopUp.show();
            }
        });

        select_Date.setOnClickListener(new View.OnClickListener() {
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

        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.choix_periode_journée, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectPeriode.setAdapter(adapter);
        selectPeriode.setOnItemSelectedListener(this);

        //********Confirmer la réservation*****************
        confirmer = findViewById(R.id.btn_confirmer_popup_reservation_details);
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_reservation = select_Date.getText().toString();
                String periode = selectPeriode.getSelectedItem().toString();

                AlertDialog.Builder myPopUp = new AlertDialog.Builder(getContext());
                myPopUp.setTitle("Confirmation");
                myPopUp.setMessage("Voulez-vous vraiment enregistrer vos modifications ?");
                myPopUp.setPositiveButton("OUI", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(date_reservation)) {
                            select_Date.setError("Veuillez saisir une date");
                        }else {
                            modifierService(date_reservation, periode);
                        }
                    }
                });
                myPopUp.setNegativeButton("NON", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                myPopUp.show();
            }
        });
    }

    private void modifierService(String date_reservation, String periode) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("reservations");

        ReservationRepository repository = new ReservationRepository();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("service_id").hasChild(currentReservation.getService_id())){
                    currentReservation.setDate(date_reservation);
                    currentReservation.setPeriode(periode);
                    repository.updateReservation(currentReservation);
                    Toast.makeText(getContext(), "Réservation prise en compte !", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else {
                    for (DataSnapshot ds : snapshot.getChildren()){
                        ReservationClass reservation = ds.getValue(ReservationClass.class);

                        if (reservation.getDate().equals(date_reservation) && reservation.getPeriode().equals(periode)){
                            ((TextView)selectPeriode.getSelectedView()).setError("Période de la journée déjà occupée pour ce jour");
                            Toast.makeText(getContext(), "Veuillez choisir un autre moment de la journée ou réserver pour un autre jour", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    currentReservation.setDate(date_reservation);
                    currentReservation.setPeriode(periode);
                    repository.updateReservation(currentReservation);
                    Toast.makeText(getContext(), "Réservation prise en compte !", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupCloseButton() {
        ImageView closeButton = findViewById(R.id.close_button_popup_reservations_details);
        closeButton.setClickable(true);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(closeButton)){
                    dismiss();
                }
            }
        });
    }

    private void setupCompenent() {
        //BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationBar);
        //View headerView = bottomNavigationView.inflateMenu(R.layout.popup_reservations_details);

        TextView service_ID = (TextView) findViewById(R.id.popup_reservation_details_type_service);
        service_ID.setText("Service (ID) : "+currentReservation.getService_id());

        TextView pseudo_fournisseur = (TextView) findViewById(R.id.pseudo_fournisseur_popup_reservations_details);
        pseudo_fournisseur.setText(currentReservation.getFournisseur_pseudo());

        TextView description_service = (TextView) findViewById(R.id.text_description_popup_reservations_details);
        description_service.setText(currentReservation.getDescription_service());

        TextView pseudo_client = (TextView) findViewById(R.id.pseudo_client_popup_reservations_details);
        pseudo_client.setText(currentReservation.getClient_pseudo());

        TextView date = (TextView) findViewById(R.id.text_date_popup_reservations_details);
        date.setText(currentReservation.getDate()+" - "+currentReservation.getPeriode());
    }

    private void selectDate() {
        //Date Select Listener
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                select_Date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

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
