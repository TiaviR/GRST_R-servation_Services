package fr.tiavn.grst_services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//import fr.tiavn.grst_services.adapter.SearchServiceAdapter;

public class ServiceRepository {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("services");
    //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("services");

    ArrayList<ServiceClass> listService = new ArrayList<>();

    private static ServiceRepository instance;

    public ServiceRepository(){
        //Prévient de l'instanciation
    }

    public static ServiceRepository getInstance(){
        if (instance == null){
            instance = new ServiceRepository();
        }
        return instance;
    }

    public ArrayList<ServiceClass> getListService() {
        //Singleton singleton = new Singleton();
        return listService;
    }

    public interface Callback {
        void callback();
    }

    public void updateData(Callback callback) {

        //Singleton singleton = new Singleton();
        //absorber les données depuis la DatabaseRef - liste items
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retirer les anciennes
                listService.clear();

                //Récolter la liste
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ServiceClass service = ds.getValue(ServiceClass.class);

                    //Vérifier que le service n'est pas nulle
                    if (service != null){
                        //ajouter l'item à notre liste
                        listService.add(service);
                    }
                }
                //Actionner le callback
                callback.callback();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Mettre à jour un service en bdd
    public void updateService(ServiceClass service){
        myRef.child(String.valueOf(service.getService_id())).setValue(service);
    }

    //ajouter un nouveau service
    public void newService(ServiceClass service){
        myRef.child(String.valueOf(service.getService_id())).setValue(service);
    }

    //Supprimer un service de la bdd
    public void deleteService(ServiceClass service){
        myRef.child(String.valueOf(service.getService_id())).removeValue();
    }
}
