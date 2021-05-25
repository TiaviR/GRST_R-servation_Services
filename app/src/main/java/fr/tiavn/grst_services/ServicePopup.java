package fr.tiavn.grst_services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.tiavn.grst_services.adapter.RecentItemAdapter;

public class ServicePopup extends Dialog {
    //public RecentItemAdapter adapter;
    private ServiceClass currentService;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("services");

    public ServicePopup(@NonNull Context context, ServiceClass service) {
        super(context);
        //adapter.context = context;
        this.currentService = service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_services_details);
        setupCompenent();
        setupCloseButton();
        setupFavori();
        setupModification();
        setupNotation();
        startReservation();
        //supprimer service
        //setupDeleteService();
    }

    private void setupNotation() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reservations");

        Button noter = (Button) findViewById(R.id.btn_noter);
        if (currentService.getFavori()){
            noter.setVisibility(View.VISIBLE);
        }else{
            noter.setVisibility(View.GONE);
        }
        /*
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists() && ds.child(currentService.getService_id()).exists()){
                        noter.setVisibility(View.VISIBLE);
                    }else {
                        noter.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
         */
        noter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NotationService notationService = new NotationService(getContext(), currentService);
                notationService.show();
            }
        });
    }

    private void setupModification() {
        Button modifier = (Button) findViewById(R.id.btn_modifier);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(currentService.getFournisseur_id())){
            modifier.setVisibility(View.VISIBLE);
        }else {
            modifier.setVisibility(View.GONE);
        }
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myPopup = new AlertDialog.Builder(getContext());
                myPopup.setTitle("Confirmation");
                myPopup.setMessage("Voulez-vous vraiment modifier votre service ?");
                myPopup.setPositiveButton("OUI", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ModificationServicePage modificationServicePage = new ModificationServicePage(getContext(), currentService);
                        modificationServicePage.show();
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

    private void startReservation() {
        Button buttonReserver = findViewById(R.id.btn_reserver);
        buttonReserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myPopup = new AlertDialog.Builder(getContext());
                myPopup.setTitle("Confirmation");
                myPopup.setMessage("Voulez-vous vraiment faire une réservation ?");
                myPopup.setPositiveButton("OUI", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ReservationPage reservationPage = new ReservationPage(getContext(), currentService);
                        reservationPage.show();
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

    public void updateFavori(ImageView imageView){
        /*
        if (myRef.child(String.valueOf(currentService.getService_id())).child("favori").equals(true)){
            imageView.setImageResource(R.drawable.ic_favs_oui);
        }else if (myRef.child(String.valueOf(currentService.getService_id())).child("favori").equals(false)){
            imageView.setImageResource(R.drawable.ic_favs);
        }
        
         */

        if (currentService.getFavori()){
            imageView.setImageResource(R.drawable.ic_favs_oui);
        }else {
            imageView.setImageResource(R.drawable.ic_favs);
        }

    }

    private void setupFavori() {
        ImageView favori = findViewById(R.id.btn_fav_popup);
        favori.setClickable(true);

        updateFavori(favori);

        //Intéraction
        favori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentService.setFavori(!currentService.getFavori());

                ServiceRepository repo = new ServiceRepository();
                repo.updateService(currentService);

                //currentService.setFavori(!currentService.getFavori());

                //myRef.child(String.valueOf(currentService.getService_id())).child("favori").setValue(currentService.getFavori());

                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference myRef = database.getReference("services");
                //myRef.child(String.valueOf(currentService.getService_id())).setValue(!(currentService.getFavori()));

                updateFavori(favori);
            }
        });
    }

    /*
    private void setupDelete(){
        ImageView deleteButton = findViewById(R.id.close_button_popup_details);
        deleteButton.setClickable(true);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceRepository repo = new ServiceRepository();
                if (v.equals(deleteButton)){
                    repo.deleteService(currentService);
                    dismiss();
                }
            }
        });
    }
     */
    private void setupCloseButton() {
        ImageView closeButton = findViewById(R.id.close_button_popup_details);
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
        //actualiser image service popup
        ImageView serviceImage = (ImageView) findViewById(R.id.imageServicePopup);
        Glide.with(getContext()).load(Uri.parse(currentService.getImage_type_service())).into(serviceImage);

        //Actualiser domaine service
        TextView domaineService = (TextView) findViewById(R.id.popup_details_type_service);
        domaineService.setText(currentService.getType_service());

        //Image Fournisseur service
        CircleImageView imageFournisseur = (CircleImageView) findViewById(R.id.img_profil_pseudo_popup);
        Glide.with(getContext()).load(Uri.parse(currentService.getImage_profil())).into(imageFournisseur);

        //Pseudo
        TextView pseudoFournisseur = (TextView) findViewById(R.id.pseudo_fournisseur_popup);
        pseudoFournisseur.setText((currentService.getPseudo_user()));

        //Description
        TextView description = (TextView) findViewById(R.id.text_description_popup_details);
        description.setText((currentService.getDescription_service()));

        //Note
        TextView note = (TextView) findViewById(R.id.moyenne_notes_popup);
        note.setText((currentService.getNote_moyenne()+""));

        //Nb notes
        TextView nb_notes = (TextView) findViewById(R.id.nb_notes_popup);
        nb_notes.setText(("("+currentService.getNb_notes()+")"));

        //Prix départ
        TextView prix = (TextView) findViewById(R.id.prix_popup);
        prix.setText((currentService.getCout_horaire()));
    }
}
