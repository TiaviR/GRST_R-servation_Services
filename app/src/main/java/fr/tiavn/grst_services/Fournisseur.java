package fr.tiavn.grst_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.tiavn.grst_services.fragments.AddFragment;
import fr.tiavn.grst_services.fragments.HomeFragment;
import fr.tiavn.grst_services.fragments.ProfileFragment;
import fr.tiavn.grst_services.fragments.ReservationsFragment;
import fr.tiavn.grst_services.fragments.SearchFragment;
//import fr.tiavn.grst_services.fragments.SearchFragment;


public class Fournisseur extends AppCompatActivity {
    //ItemHomeRepository itemRepo;
    //ServiceRepository serviceRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //savedInstanceState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fournisseur);

        //Charger notre ItemHomeRepository
        //ServiceRepository serviceRepository = new ServiceRepository();
        //serviceRepository = ServiceRepository.getInstance();

        //mettre à jour la liste des items
        /*
        serviceRepository.updateData(new ServiceRepository.Callback() {
            @Override
            public void callback() {
                //injecter le fragment de la page de recherche
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fournisseur_fragment, new SearchFragment(Fournisseur.this));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
         */

        loadFragment(new HomeFragment(this), R.string.home);

        //Importer la BottomNavigatonView
        BottomNavigationView navigationView = findViewById(R.id.navigationBar);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        //loadFragment(new HomeFragment(this));
    }
/*
    private void init(){
        SearchFragment searchFragment = new SearchFragment(this);
        //injecter le fragment de la page de recherche
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fournisseur_fragment, searchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
 */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.home_page:
                        loadFragment(new HomeFragment(Fournisseur.this), R.string.home);
                        return true;
                        //break;
                    case R.id.search_page:
                        loadFragment(new SearchFragment(Fournisseur.this), R.string.search);
                        return true;
                        //break;
                    case R.id.add_page:
                        loadFragment(new AddFragment(Fournisseur.this), R.string.add_service);
                        return true;
                    //break;
                    case R.id.mes_services_page:
                        loadFragment(new ReservationsFragment(Fournisseur.this), R.string.mes_services);
                        return true;
                        //break;
                    case R.id.profile_page:
                        loadFragment(new ProfileFragment(Fournisseur.this), R.string.profil);
                        return true;
                    //break;
                }
                return false;
            }
        };

    public void loadFragment(Fragment fragment, int nomPage){
        //Charger notre ItemHomeRepository
        ServiceRepository serviceRepository = new ServiceRepository();
        serviceRepository = ServiceRepository.getInstance();

        ReservationRepository reservationRepository = ReservationRepository.getInstance();

        //actualiser le titre de la page
        TextView nom_page = (TextView) findViewById(R.id.text_page);
        //nom_page.setText(""+String.valueOf(nomPage));
        nom_page.setText(getString(nomPage));
        //mettre à jour la liste des items

        serviceRepository.updateData(new ServiceRepository.Callback() {
            @Override
            public void callback() {
                //injecter le fragment de la page de recherche
                if (!isFinishing()) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fournisseur_fragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //transaction.commitAllowingStateLoss();
                }
            }
        });

        reservationRepository.updateData(new ReservationRepository.Callback() {
            @Override
            public void callback() {
                if (!isFinishing()) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fournisseur_fragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //transaction.commitAllowingStateLoss();
                }
            }
        });
    }
}
