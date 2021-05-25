package fr.tiavn.grst_services.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import fr.tiavn.grst_services.Connexion;
import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.Inscription;
import fr.tiavn.grst_services.MainActivity;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ReservationRepository;
import fr.tiavn.grst_services.ServicePopup;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.Utilisateur;

public class ProfileFragment extends Fragment {
    Context context;
    //private Fournisseur context;
    //private Utilisateur contextUtilisateur;

    ImageView imageProfil;
    private String BUCKET_URL = "gs://grst-services.appspot.com";
    StorageReference storageReference;

    ListView monEspaceListView;
    ListView gestionListView;
    ListView generalitesListView;
    TextView pseudo_profil;

    DatabaseReference databaseReferenceFournisseur;
    DatabaseReference databaseReferenceUtilisateur;

    String monEspaceSection[] = {"Mes services favoris", "Historique", "Mon compte"};
    int imagesMonEspaceSection[] = {R.drawable.ic_favs, R.drawable.ic_historique, R.drawable.ic_profil};

    String gestionSection[] = {"Gérer les offres", "Gérer les réservations"};
    int imagesGestionSection[] ={R.drawable.ic_offres, R.drawable.ic_reservation};

    String generalitesSection[] = {"Paiements", "Notifications", "Aide", "Déconnexion"};
    int imagesGeneralitesSection[] = {R.drawable.ic_paiement, R.drawable.ic_notification, R.drawable.ic_aide,R.drawable.ic_deconnexion};

    public ProfileFragment(){
    }

    public ProfileFragment(Context context) {
        this.context = context;
    }
   /*
    public ProfileFragment(Utilisateur context) {
        this.contextUtilisateur = context;
    }

    */

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //************************Fournisseur*****************************************
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Image de profil
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL);
        imageProfil = view.findViewById(R.id.img_profile_profile_frag);
        imageProfil.setClickable(true);
        imageProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickupImage();
            }
        });

        pseudo_profil = view.findViewById(R.id.pseudo_profile_frag);
        databaseReferenceUtilisateur = FirebaseDatabase.getInstance().getReference("utilisateurs");
        databaseReferenceFournisseur = FirebaseDatabase.getInstance().getReference("fournisseurs");
        databaseReferenceUtilisateur.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    pseudo_profil.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("utilisateur_pseudo").getValue(String.class));
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
                    pseudo_profil.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pseudo_fournisseur").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        monEspaceListView = view.findViewById(R.id.listview_mon_espace);
        MyAdapter adapter = new MyAdapter(context, monEspaceSection, imagesMonEspaceSection);
        //MyAdapter adapterUtilisateur = new MyAdapter(this.contextUtilisateur, monEspaceSection, imagesMonEspaceSection);
        monEspaceListView.setAdapter(adapter);
        //monEspaceListView.setAdapter(adapterUtilisateur);
        monEspaceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    databaseReferenceUtilisateur.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                ServiceRepository serviceRepository = new ServiceRepository();
                                serviceRepository = ServiceRepository.getInstance();

                                serviceRepository.updateData(new ServiceRepository.Callback() {
                                    @Override
                                    public void callback() {
                                        //injecter le fragment
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        ReservationsFragment services = new ReservationsFragment(getContext());
                                        transaction.replace(R.id.utilisateur_fragment, services);
                                        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                });
                                Toast.makeText(getContext(), "Mes services favoris", Toast.LENGTH_SHORT).show();
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
                                ServiceRepository serviceRepository = new ServiceRepository();
                                serviceRepository = ServiceRepository.getInstance();

                                serviceRepository.updateData(new ServiceRepository.Callback() {
                                    @Override
                                    public void callback() {
                                        //injecter le fragment
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        ReservationsFragment services = new ReservationsFragment(getContext());
                                        transaction.replace(R.id.fournisseur_fragment, services);
                                        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                });
                                Toast.makeText(getContext(), "Mes services favoris", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                if (position == 1) {
                    Toast.makeText(getContext(), "Historique", Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    Toast.makeText(getContext(), "Mon compte", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Helper.getListViewSize(monEspaceListView);

        gestionListView = view.findViewById(R.id.listview_gestion);
        MyAdapter adapter2 = new MyAdapter(context, gestionSection, imagesGestionSection);
        //MyAdapter adapterUtilisateur2 = new MyAdapter(this.contextUtilisateur, monEspaceSection, imagesMonEspaceSection);
        gestionListView.setAdapter(adapter2);
        //gestionListView.setAdapter(adapterUtilisateur2);
        gestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    databaseReferenceFournisseur.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ServiceRepository serviceRepository = new ServiceRepository();
                            serviceRepository = ServiceRepository.getInstance();
                            if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                serviceRepository.updateData(new ServiceRepository.Callback() {
                                    @Override
                                    public void callback() {
                                        //injecter le fragment
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        PageGestionServices services = new PageGestionServices(getContext());
                                        transaction.replace(R.id.fournisseur_fragment, services);
                                        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                });
                            }else {
                                serviceRepository.updateData(new ServiceRepository.Callback() {
                                    @Override
                                    public void callback() {
                                        //injecter le fragment
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        ContenuIndisponible fragment = new ContenuIndisponible(getContext());
                                        transaction.replace(R.id.utilisateur_fragment, fragment);
                                        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(getContext(), "Consultez vos services !", Toast.LENGTH_SHORT).show();
                }

                if (position == 1) {
                    databaseReferenceUtilisateur.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                ReservationRepository repo = new ReservationRepository();
                                repo = ReservationRepository.getInstance();

                                repo.updateData(new ReservationRepository.Callback() {
                                    @Override
                                    public void callback() {

                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        PageGestionReservations reservations = new PageGestionReservations(getContext());
                                        transaction.replace(R.id.utilisateur_fragment, reservations);
                                        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
                                        transaction.addToBackStack(null);
                                        transaction.commit();


                                    }
                                });
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
                                ReservationRepository repo = new ReservationRepository();
                                repo = ReservationRepository.getInstance();

                                repo.updateData(new ReservationRepository.Callback() {
                                    @Override
                                    public void callback() {

                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        PageGestionReservations reservations = new PageGestionReservations(getContext());
                                        transaction.replace(R.id.fournisseur_fragment, reservations);
                                        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
                                        transaction.addToBackStack(null);
                                        transaction.commit();


                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    /*
                    ReservationRepository repo = new ReservationRepository();
                    repo = ReservationRepository.getInstance();

                    repo.updateData(new ReservationRepository.Callback() {
                        @Override
                        public void callback() {

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            PageGestionReservations reservations = new PageGestionReservations(getContext());
                            transaction.replace(R.id.fournisseur_fragment, reservations);
                            transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
                            transaction.addToBackStack(null);
                            transaction.commit();


                        }
                    });
                     */
                    /*
                    ReservationRepository repo = new ReservationRepository();
                    repo = ReservationRepository.getInstance();

                    repo.updateData(new ReservationRepository.Callback() {
                        @Override
                        public void callback() {

                            PageGestionReservations reservations = new PageGestionReservations(context);
                            reservations.show(getFragmentManager(), "Gestion réservations dialog");

                            Toast.makeText(getContext(), "Consultez vos réservations et rendez-vous !", Toast.LENGTH_SHORT).show();

                        }
                    });
                     */
                }
            }
        });
        Helper.getListViewSize(gestionListView);

        generalitesListView = view.findViewById(R.id.listview_generalites);
        MyAdapter adapter3 = new MyAdapter(context, generalitesSection, imagesGeneralitesSection);
        //MyAdapter adapterUtilisateur3 = new MyAdapter(this.contextUtilisateur, monEspaceSection, imagesMonEspaceSection);
        generalitesListView.setAdapter(adapter3);
        //generalitesListView.setAdapter(adapterUtilisateur3);
        generalitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(getContext(), "Consultez vos moyens de paiement !", Toast.LENGTH_SHORT).show();
                }
                if (position == 1) {
                    Toast.makeText(getContext(), "Gérez vos notifications !", Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    Toast.makeText(getContext(), "Besoin d'aide ?", Toast.LENGTH_SHORT).show();
                }

                if (position == 3) {
                    AlertDialog.Builder myPopup = new AlertDialog.Builder(getContext());
                    myPopup.setTitle("Confirmation");
                    myPopup.setMessage("Voulez-vous vraiment vous déconnecter ?");
                    myPopup.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getContext(), "Déconnexion réussie", Toast.LENGTH_SHORT).show();
                        }
                    });
                    myPopup.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    myPopup.show();
                }
            }
        });
        Helper.getListViewSize(generalitesListView);

        return view;

    }

    //Choisir une image sur le téléphone
    private void pickupImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choisissez une image de profil"), 47);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 47 && resultCode == Activity.RESULT_OK){
            //Vérifier si les données sont nulles
            if (data == null || data.getData() == null){
                return;
            }
            //Récuperer l'image
            Uri selectedImage = data.getData();

            //Mettre à jour l'aperçu de l'image
            imageProfil.setImageURI(selectedImage);

            //Changer les données du fournisseur ou utilisateur
            /*
            databaseReferenceUtilisateur.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        databaseReferenceUtilisateur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil").setValue(selectedImage.toString());
                        Glide.with(getContext()).load(databaseReferenceUtilisateur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil").setValue(selectedImage.toString())).into(imageProfil);
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
                        databaseReferenceFournisseur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil").setValue(selectedImage.toString());
                        Glide.with(getContext()).load(databaseReferenceFournisseur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil").setValue(selectedImage.toString())).into(imageProfil);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            */
            //Héberger sur le bucket
            uploadImage(selectedImage);
        }
    }

    //Fonction pour envoyer des fichiers sur le storage
    public void uploadImage(Uri file) {
        //Vérifier que le fichier n'est pas null
        if (file != null) {
            String filename = UUID.randomUUID().toString() + ".jpg";
            StorageReference ref = storageReference.child(filename);
            UploadTask uplaodTask = ref.putFile(file);

            //Démarrer la tâche d'envoi
            Task<Uri> urlTask = uplaodTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        //Récuperer l'image
                        Uri downloadURI = task.getResult();
                        String downloadURL = downloadURI.toString();

                        //Changer les données du fournisseur ou utilisateur

                        databaseReferenceUtilisateur.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    databaseReferenceUtilisateur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil").setValue(downloadURL);
                                    Glide.with(getContext()).load(Uri.parse(String.valueOf(databaseReferenceUtilisateur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil")))).into(imageProfil);
                                    //imageProfil.setImageURI(Uri.parse(String.valueOf(databaseReferenceUtilisateur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil"))));
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
                                    databaseReferenceFournisseur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil").setValue(downloadURL);
                                    Glide.with(getContext()).load(Uri.parse(String.valueOf(databaseReferenceFournisseur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil")))).into(imageProfil);
                                    //imageProfil.setImageURI(Uri.parse(String.valueOf(databaseReferenceFournisseur.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageProfil"))));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });
        }
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String sections[];
        int images[];

        MyAdapter (Context c, String sect[], int img[]) {
            super(c, R.layout.item_menu_profil_page, R.id.text_item_menu_profil_page, sect);
            this.context = c;
            this.sections = sect;
            this.images = img;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sectionItem = layoutInflater.inflate(R.layout.item_menu_profil_page, parent, false);
            ImageView iconSection = sectionItem.findViewById(R.id.image_item_menu_profil_page);
            TextView textSection = sectionItem.findViewById(R.id.text_item_menu_profil_page);

            iconSection.setImageResource(images[position]);
            textSection.setText(sections[position]);

            return sectionItem;
        }
    }

    public static class Helper {
        public static void getListViewSize(ListView myListView) {
            ListAdapter myListAdapter=myListView.getAdapter();
            if (myListAdapter==null) {
                //do nothing return null
                return;
            }
            //set listAdapter in loop for getting final size
            int totalHeight=0;
            for (int size=0; size < myListAdapter.getCount(); size++) {
                View listItem=myListAdapter.getView(size, null, myListView);
                listItem.measure(0, 0);
                totalHeight+=listItem.getMeasuredHeight();
            }
            //setting listview item in adapter
            ViewGroup.LayoutParams params=myListView.getLayoutParams();
            params.height=totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
            myListView.setLayoutParams(params);
            // print height of adapter on log
            Log.i("height of listItem:", String.valueOf(totalHeight));
        }
    }
}
