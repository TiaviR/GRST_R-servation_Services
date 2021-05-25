package fr.tiavn.grst_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;


public class Inscription extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btn_retour;
    private EditText email;
    private EditText nom;
    private EditText prenom;
    private EditText pseudo;
    private Spinner spinner;
    private EditText mdp;
    private EditText repeatMdp;
    private Button btn_connexion;
    private String selectedSpinner;
    private Button btn_inscription;
    private String imageProfil = "";

    private String uid;
    private int num_id = 0;

    DatabaseReference databaseReference;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    uid = firebaseAuth.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("pseudo_fournisseur")){
                                String getPseudo = snapshot.child(uid).child("pseudo_fournisseur").getValue(String.class);
                                Toast.makeText(Inscription.this, getPseudo+" connecté", Toast.LENGTH_SHORT).show();
                            }else if (snapshot.hasChild("utilisateur_pseudo")){
                                String getPseudo = snapshot.child(uid).child("utilisateur_pseudo").getValue(String.class);
                                Toast.makeText(Inscription.this, getPseudo+" connecté", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    Toast.makeText(Inscription.this, "Quelqu'un vient de se deconnecter", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //***Utilisateur ou Fournisseur****
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choix_inscription, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // **EditText**
        email = findViewById(R.id.text_mail);
        nom = findViewById(R.id.text_nom);
        prenom = findViewById(R.id.text_prenom);
        pseudo = findViewById(R.id.text_pseudo);
        mdp = findViewById(R.id.editMdp);
        repeatMdp = findViewById(R.id.editRepetMdp);

        //**Renvoie à la page de connexion**
        btn_connexion = (Button) findViewById(R.id.buttonConnexion_page_inscription);
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inscription.this, Connexion.class);
                startActivity(intent);
            }
        });

        //**Renvoie à la page d'acceuil**
        btn_retour = (Button) findViewById(R.id.button_retour);
        btn_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inscription.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //**Inscription**
        btn_inscription = (Button) findViewById(R.id.buttonInscription_page_inscription);
        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_nom = nom.getText().toString();
                String txt_prenom = prenom.getText().toString();
                String txt_pseudo = pseudo.getText().toString();
                String txt_mdp = mdp.getText().toString();
                String txt_repeatMdp = repeatMdp.getText().toString();

                if (TextUtils.isEmpty(txt_email)) {
                    email.setError("Veuillez entrer une adresse mail");
                }
                if (TextUtils.isEmpty(txt_nom)) {
                    email.setError("Veuillez entrer votre nom");
                }
                if (TextUtils.isEmpty(txt_prenom)) {
                    email.setError("Veuillez entrer votre prénom");
                }
                if (TextUtils.isEmpty(txt_pseudo)) {
                    email.setError("Veuillez entrer votre pseudo");
                }
                if (TextUtils.isEmpty(txt_mdp)) {
                    email.setError("Veuillez entrer un mot de passe");
                }
                if (TextUtils.isEmpty(txt_repeatMdp)) {
                    email.setError("Veuillez répeter votre mot de passe");
                }

                if (TextUtils.isEmpty(txt_email)
                        || TextUtils.isEmpty(txt_nom)
                        || TextUtils.isEmpty(txt_prenom)
                        || TextUtils.isEmpty(txt_pseudo)
                        || TextUtils.isEmpty(txt_mdp)
                        || TextUtils.isEmpty(txt_repeatMdp)){
                    Toast.makeText(Inscription.this, "Certains champs manquants", Toast.LENGTH_SHORT).show();
                }else if (!(txt_mdp.equals(txt_repeatMdp))){
                    repeatMdp.setError("Veuillez saisir le même mot de passe");
                    Toast.makeText(Inscription.this, "Veuillez saisir les mêmes Mot de passe", Toast.LENGTH_SHORT).show();
                }else if (txt_mdp.length() < 7 || txt_repeatMdp.length() < 7){
                    Toast.makeText(Inscription.this, "Mot de passe trop cours", Toast.LENGTH_SHORT).show();
                }else {
                    //progressBar.setVisibility(View.VISIBLE);
                    inscription(txt_email, txt_nom, txt_prenom, txt_pseudo, txt_mdp);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            auth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (auth.getCurrentUser() != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }

    public void inscription(String email, String nom, String prenom, String pseudo, String mdp){
        auth.createUserWithEmailAndPassword(email, mdp)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //on ajoute les autre données dans la BDD
                            if (selectedSpinner.equals("Utilisateur de services")){
                                //num_id++;
                                UtilisateurClass utilisateur = new UtilisateurClass(
                                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        nom,
                                        prenom,
                                        email,
                                        mdp,
                                        pseudo,
                                        imageProfil
                                );

                                FirebaseDatabase.getInstance().getReference("utilisateurs")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(utilisateur).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Inscription.this, pseudo+", vous êtes maintenant incrit !", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Inscription.this, Utilisateur.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(Inscription.this, "Connexion échouée", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                //Intent intent = new Intent(Inscription.this, Utilisateur.class);
                                //startActivity(intent);
                            }else if (selectedSpinner.equals("Fournisseur de services")){
                                //num_id++;
                                FournisseurClass fournisseur = new FournisseurClass(
                                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        nom,
                                        prenom,
                                        email,
                                        pseudo,
                                        mdp,
                                        imageProfil
                                );

                                FirebaseDatabase.getInstance().getReference("fournisseurs")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(fournisseur).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Inscription.this, pseudo+", vous êtes maintenant incrit !", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Inscription.this, Fournisseur.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(Inscription.this, "Connexion échouée", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                //Intent intent = new Intent(Inscription.this, Fournisseur.class);
                                //startActivity(intent);
                            }

                        }else {
                            Toast.makeText(Inscription.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSpinner = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), selectedSpinner, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}