package fr.tiavn.grst_services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotationService extends Dialog {
    private ServiceClass currentService;

    private boolean note1 = false;
    private boolean note2 = false;
    private boolean note3 = false;
    private boolean note4 = false;
    private boolean note5 = false;

    private int note;

    Button confirmer_note;

    public NotationService(@NonNull Context context, ServiceClass currentService) {
        super(context);
        this.currentService = currentService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_notation);

        confirmer_note = (Button) findViewById(R.id.btn_valider_note_popup_notation);

        setupNotation();
        confirmer_new_note();
        setupAnnulation();
    }

    public void updateStar(boolean note, ImageView imageView){
        if (note) {
            imageView.setImageResource(R.drawable.ic_star_plein);
        }else {
            imageView.setImageResource(R.drawable.ic_star_vide);
        }
    }

    protected int noteFinal(){
        if (note1 && note2 && note3 && note4 && note5){
            note = 5;
        }
        if (note1 && note2 && note3 && note4){
            note = 4;
        }
        if (note1 && note2 && note3){
            note = 3;
        }
        if (note1 && note2){
            note = 2;
        }
        if (note1 ){
            note = 1;
        }else {
            note = 0;
        }
        return note;
    }

    private void setupNotation() {
        ImageView note_1 = findViewById(R.id.note_1);
        note_1.setClickable(true);

        ImageView note_2 = findViewById(R.id.note_2);
        note_2.setClickable(true);

        ImageView note_3 = findViewById(R.id.note_3);
        note_3.setClickable(true);

        ImageView note_4 = findViewById(R.id.note_4);
        note_4.setClickable(true);

        ImageView note_5 = findViewById(R.id.note_5);
        note_5.setClickable(true);


        note_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note1 = !note1;
                updateStar(note1, note_1);
                if (note1 == false){
                    note2 = note3 = note4 = note5 = false;
                    updateStar(note2, note_2);
                    updateStar(note3, note_3);
                    updateStar(note4, note_4);
                    updateStar(note5, note_5);
                }
            }
        });

        note_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note2 = !note2;
                updateStar(note2, note_2);
                if (note2 == true){
                    note1 = true;
                    updateStar(note1, note_1);
                }else {
                    note3 = false;
                    updateStar(note3, note_3);
                    note4 = false;
                    updateStar(note4, note_4);
                    note5 = false;
                    updateStar(note5, note_5);
                }
            }
        });
        note_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note3 = !note3;
                updateStar(note3, note_3);
                if (note3 == true){
                    note1 = note2 = true;
                    updateStar(note1, note_1);
                    updateStar(note2, note_2);
                }else {
                    note4 = note5 = false;
                    updateStar(note4, note_4);
                    updateStar(note5, note_5);
                }
            }
        });
        note_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note4 = !note4;
                updateStar(note4, note_4);
                if (note4 == true){
                    note1 = note2 = note3 = true;
                    updateStar(note1, note_1);
                    updateStar(note2, note_2);
                    updateStar(note3, note_3);
                }else {
                    note5 = false;
                    updateStar(note5, note_5);
                }
            }
        });
        note_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note5 = !note5;
                updateStar(note5, note_5);
                if (note5 == true){
                    note1 = note2 = note3 = note4 = true;
                    updateStar(note1, note_1);
                    updateStar(note2, note_2);
                    updateStar(note3, note_3);
                    updateStar(note4, note_4);
                }
            }
        });
        //confirmer_new_note();
    }

    public void confirmer_new_note() {
        //Button confirmer_note = (Button) findViewById(R.id.btn_valider_note_popup_notation);
        confirmer_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myPopup = new AlertDialog.Builder(getContext());
                myPopup.setTitle("Confirmation");
                myPopup.setMessage("Valider la note ?");

                myPopup.setPositiveButton("OUI", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noter();
                        Toast.makeText(getContext(), "Note attribuée !", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
                myPopup.setNegativeButton("NON", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });
    }

    public void noter() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("services");
        ServiceRepository repo = new ServiceRepository();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int nb_notes = currentService.getNb_notes() + 1;
                int note_final_service = noteFinal();
                double note_moyenne = currentService.moyenne();
                currentService.setNb_notes(nb_notes);
                //currentService.getNotes().add(noteFinal());
                currentService.addListNote(note_final_service);
                currentService.setNote_moyenne(note_moyenne);
                repo.updateService(currentService);

                Toast.makeText(getContext(), "Nombre de notes =" + nb_notes+", Note attribuée : "+note_final_service+", Note moyenne : "+note_moyenne, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupAnnulation() {
        Button annuler = (Button) findViewById(R.id.btn_annuler_popup_notation);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myPopup = new AlertDialog.Builder(getContext());
                myPopup.setTitle("Confirmation");
                myPopup.setMessage("Stopper la notation ?");

                myPopup.setPositiveButton("OUI", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
                myPopup.setNegativeButton("NON", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                myPopup.show();
            }
        });
    }
}
