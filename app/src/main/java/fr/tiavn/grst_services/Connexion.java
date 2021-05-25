package fr.tiavn.grst_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Connexion extends AppCompatActivity {

    private Button btn_retour;
    private Button btn_inscription;
    private Button btn_connexion;
    private EditText text_id_connexion;
    private EditText insert_mdp;

    FirebaseAuth auth;

    DatabaseReference databaseReferenceFournisseur;
    DatabaseReference databaseReferenceUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        auth = FirebaseAuth.getInstance();

        text_id_connexion = findViewById(R.id.text_mail2);
        insert_mdp = findViewById(R.id.editMdp2);

        btn_connexion = findViewById(R.id.buttonConnexion_page_connexion);
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_mail = text_id_connexion.getText().toString().trim();
                String mdp = insert_mdp.getText().toString().trim();

                if (TextUtils.isEmpty(id_mail)){
                    text_id_connexion.setError("Veuillez saisir votre identifiant de connexion");
                    return;
                }
                if (TextUtils.isEmpty(mdp)){
                    insert_mdp.setError("Veuillez entrer votre mot de passe");
                    return;
                }
                //progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(id_mail, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            databaseReferenceUtilisateur = FirebaseDatabase.getInstance().getReference("utilisateurs");
                            databaseReferenceFournisseur = FirebaseDatabase.getInstance().getReference("fournisseurs");
                            databaseReferenceUtilisateur.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        Toast.makeText(Connexion.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Connexion.this, Utilisateur.class);
                                        startActivity(intent);
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
                                        Toast.makeText(Connexion.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Connexion.this, Fournisseur.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else {
                            Toast.makeText(Connexion.this, "Connexion échouée : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btn_inscription = (Button) findViewById(R.id.buttonInscription_page_connexion);
        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Connexion.this, Inscription.class);
                startActivity(intent);
            }
        });

        btn_retour = (Button) findViewById(R.id.button_retour_connexion);
        btn_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Connexion.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}