package fr.tiavn.grst_services.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServiceRepository;

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Context context;
    private Spinner spinner;
    private String selectedSpinner;
    private ImageView addServiceImage;
    private String imageURL;
    private String[] images;
    private EditText description;
    private EditText prix;
    private Button confirmer;

    public AddFragment(){
    }

    public AddFragment(Context context) {
        this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);

        addServiceImage = view.findViewById(R.id.image_add_service);
        images = getResources().getStringArray(R.array.choix_image_domaine);

        //Récuperer le spinner

        spinner = view.findViewById(R.id.spinner_add_fragment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.choix_domaine, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        description = view.findViewById(R.id.editText_add_service_description);
        prix = view.findViewById(R.id.editText_add_service_prix);

        confirmer = view.findViewById(R.id.button_confirm_add_service);
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descriptionService = description.getText().toString();
                String prixService = prix.getText().toString();

                AlertDialog.Builder myPopUp = new AlertDialog.Builder(getContext());
                myPopUp.setTitle("Confirmation");
                myPopUp.setMessage("Voulez-vous vraiment proposer ce service ?");

                myPopUp.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(descriptionService)) {
                            description.setError("Veuillez faire une courte description du service");
                        }else if (TextUtils.isEmpty(prixService)) {
                            prix.setError("Veuillez saisir un prix de départ");
                        }else {
                            creationService(descriptionService, prixService);
                            Toast.makeText(getContext(), "Service mis en ligne !", Toast.LENGTH_SHORT).show();
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

        return view;

    }

    private void creationService(String desc, String prix) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("fournisseurs");

        ServiceRepository repo = new ServiceRepository();

        String domaineService = spinner.getSelectedItem().toString();
        //String pseudoFournisseur = String.valueOf(myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pseudo_fournisseur"));
        //String imageProfilUser = String.valueOf(myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil"));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pseudoFournisseur = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pseudo_fournisseur").getValue(String.class);
                String imageProfilUser = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil").getValue(String.class);

                //Création d'un nouveau service
                ServiceClass newService = new ServiceClass(
                        UUID.randomUUID().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        domaineService,
                        imageURL,
                        imageProfilUser,
                        pseudoFournisseur,
                        desc,
                        0.0,
                        0,
                        prix,
                        false
                );

                //Envoyer en BDD
                repo.newService(newService);
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

        imageURL = images[position];

        Glide.with(getContext()).load(Uri.parse(images[position])).into(addServiceImage);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
