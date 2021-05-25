package fr.tiavn.grst_services.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fr.tiavn.grst_services.Fournisseur;
import fr.tiavn.grst_services.ItemHomeModel;
import fr.tiavn.grst_services.ItemHomeRepository;
import fr.tiavn.grst_services.R;
import fr.tiavn.grst_services.ServiceClass;
import fr.tiavn.grst_services.ServiceRepository;
import fr.tiavn.grst_services.Utilisateur;
import fr.tiavn.grst_services.adapter.RecentItemAdapter;
import fr.tiavn.grst_services.adapter.TopServicesAdapter;
import fr.tiavn.grst_services.adapter.TopServicesItemDecoration;
import fr.tiavn.grst_services.ItemHomeRepository;
//import fr.tiavn.grst_services.ItemHomeRepository.Singleton;

public class HomeFragment extends Fragment {
    Context context;

    //private Fournisseur context;
    //private Utilisateur contextUser;

    public HomeFragment(){
    }

    public HomeFragment(Context context) {
        this.context = context;
    }
    /*
    public HomeFragment(Utilisateur context) {
        this.context = context;
    }

     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("services");
        ArrayList<ServiceClass> listeTopServices = new ArrayList<>();

        myRef.orderByChild("note_moyenne").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ServiceClass service = snapshot.getValue(ServiceClass.class);
                    listeTopServices.add(service);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Récuperer le RecyclerView
        RecyclerView horizontalRecyclerView = view.findViewById(R.id.horizontal_recycler_view);
        horizontalRecyclerView.setAdapter(new RecentItemAdapter(context, ServiceRepository.getInstance().getListService()/*ItemHomeRepository.getInstance().getListItem()*/, R.layout.item_home_recent_service));
        //horizontalRecyclerView.setAdapter(new RecentItemAdapter(this.contextUser, ServiceRepository.getInstance().getListService()/*ItemHomeRepository.getInstance().getListItem()*/, R.layout.item_home_recent_service));

        //Récuperer le second RecycclerView
        RecyclerView horizontalRecyclerView2 = view.findViewById(R.id.horizontal_recycler_view2);
        horizontalRecyclerView2.setAdapter(new RecentItemAdapter(context, ServiceRepository.getInstance().getListService()/*ItemHomeRepository.getInstance().getListItem()*/, R.layout.item_home_recent_consultation));
        //horizontalRecyclerView2.setAdapter(new RecentItemAdapter(this.contextUser, ServiceRepository.getInstance().getListService()/*ItemHomeRepository.getInstance().getListItem()*/, R.layout.item_home_recent_consultation));

        //Récuperer le RecycleView vertical
        RecyclerView verticalRecyclerView = view.findViewById(R.id.vertical_recycler_view);
        verticalRecyclerView.setAdapter(new TopServicesAdapter(context,  ServiceRepository.getInstance().getListService(), R.layout.item_home_top_services));
        //verticalRecyclerView.setAdapter(new RecentItemAdapter(this.contextUser, ServiceRepository.getInstance().getListService()/*ItemHomeRepository.getInstance().getListItem()*/, R.layout.item_home_top_services));
        verticalRecyclerView.addItemDecoration(new TopServicesItemDecoration());

        return view;
    }
}
