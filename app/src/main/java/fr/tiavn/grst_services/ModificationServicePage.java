package fr.tiavn.grst_services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class ModificationServicePage extends Dialog implements AdapterView.OnItemSelectedListener{
    private Context context;
    private ServiceClass currentService;

    private Spinner choixDomaine;
    private String selectedSpinner;
    private ImageView newServiceImage;
    private String imageURL;
    private String[] newImages;
    private EditText newDescription;
    private EditText newPrix;
    private Button modifier;

    public ModificationServicePage(@NonNull Context context, ServiceClass currentService) {
        super(context);
        this.currentService = currentService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.page_modification_service);

        newServiceImage = findViewById(R.id.image_page_modifier_service);
        newImages = getContext().getResources().getStringArray(R.array.choix_image_domaine);

        //Récuperer le spinner

        choixDomaine = findViewById(R.id.spinner_page_modifier_service);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.choix_domaine, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        choixDomaine.setAdapter(adapter);
        choixDomaine.setOnItemSelectedListener(this);

        newDescription = findViewById(R.id.editText_page_modifier_service_description);
        newPrix = findViewById(R.id.editText_page_modifier_service_service_prix);

        modifier = findViewById(R.id.button_page_modifier_service);
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descriptionService = newDescription.getText().toString();
                String prixService = newPrix.getText().toString();

                AlertDialog.Builder myPopUp = new AlertDialog.Builder(getContext());
                myPopUp.setTitle("Confirmation");
                myPopUp.setMessage("Voulez-vous vraiment proposer ce service ?");

                myPopUp.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(descriptionService)) {
                            newDescription.setError("Veuillez faire une courte description du service");
                        }else if (TextUtils.isEmpty(prixService)) {
                            newPrix.setError("Veuillez saisir un prix de départ");
                        }else {
                            modification(descriptionService, prixService);
                            Toast.makeText(getContext(), "Service mis à jour !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                myPopUp.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Informations non validées !", Toast.LENGTH_SHORT).show();
                    }
                });
                myPopUp.show();

            }
        });
    }

    public void modification(String desc, String prix){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("services");

        ServiceRepository repo = new ServiceRepository();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentService.setType_service(choixDomaine.getSelectedItem().toString());
                currentService.setImage_profil(imageURL);
                currentService.setDescription_service(desc);
                currentService.setCout_horaire(prix);

                repo.updateService(currentService);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSpinner = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), selectedSpinner, Toast.LENGTH_SHORT).show();

        imageURL = newImages[position];

        Glide.with(getContext()).load(Uri.parse(newImages[position])).into(newServiceImage);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
