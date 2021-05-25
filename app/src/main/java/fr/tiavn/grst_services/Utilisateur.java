package fr.tiavn.grst_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.tiavn.grst_services.fragments.AddFragment;
import fr.tiavn.grst_services.fragments.ContenuIndisponible;
import fr.tiavn.grst_services.fragments.HomeFragment;
import fr.tiavn.grst_services.fragments.ProfileFragment;
import fr.tiavn.grst_services.fragments.ReservationsFragment;
import fr.tiavn.grst_services.fragments.SearchFragment;

public class Utilisateur extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //savedInstanceState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_utilisateur);

        loadFragmentUtilisateur(new HomeFragment(this), R.string.home);

        //Importer la BottomNavigatonView
        BottomNavigationView navigationView = findViewById(R.id.navigationBarClient);
        navigationView.setOnNavigationItemSelectedListener(navListenerUtilisateur);
        //loadFragment(new HomeFragment(this));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListenerUtilisateur =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.home_page:
                            loadFragmentUtilisateur(new HomeFragment(Utilisateur.this), R.string.home);
                            return true;
                        //break;
                        case R.id.search_page:
                            loadFragmentUtilisateur(new SearchFragment(Utilisateur.this), R.string.search);
                            return true;
                        //break;
                        case R.id.add_page:
                            loadFragmentUtilisateur(new ContenuIndisponible(Utilisateur.this), R.string.add_service);
                            return true;
                        //break;
                        case R.id.mes_services_page:
                            loadFragmentUtilisateur(new ReservationsFragment(Utilisateur.this), R.string.mes_services);
                            return true;
                        //break
                        case R.id.profile_page:
                            loadFragmentUtilisateur(new ProfileFragment(Utilisateur.this), R.string.profil);
                            return true;
                        //break;
                    }
                    return false;
                }
            };

    public void loadFragmentUtilisateur(Fragment fragment, int nomPage){
        //Charger notre ItemHomeRepository
        ServiceRepository serviceRepository = new ServiceRepository();
        serviceRepository = ServiceRepository.getInstance();

        ReservationRepository reservationRepository = ReservationRepository.getInstance();

        //actualiser le titre de la page
        TextView nom_page = (TextView) findViewById(R.id.text_accueil);
        //nom_page.setText(""+String.valueOf(nomPage));
        nom_page.setText(getString(nomPage));
        //mettre à jour la liste des items

        serviceRepository.updateData(new ServiceRepository.Callback() {
            @Override
            public void callback() {
                //injecter le fragment de la page de recherche
                if (!isFinishing()) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.utilisateur_fragment, fragment);
                    transaction.addToBackStack(null);
                    //transaction.commit();
                    transaction.commitAllowingStateLoss();
                }
            }
        });

        reservationRepository.updateData(new ReservationRepository.Callback() {
            @Override
            public void callback() {
                if (!isFinishing()) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.utilisateur_fragment, fragment);
                    transaction.addToBackStack(null);
                    //transaction.commit();
                    transaction.commitAllowingStateLoss();
                }
            }
        });
    }
}