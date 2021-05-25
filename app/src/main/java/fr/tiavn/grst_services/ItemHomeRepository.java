package fr.tiavn.grst_services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemHomeRepository {

    //Se connecter à la référence "itemsHomeFragment"

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("itemsHomeFragment");

    //Créer une liste qui va contenir nos items

    ArrayList<ItemHomeModel> listItem = new ArrayList<>();

    private static ItemHomeRepository instance;
    public ItemHomeRepository(){
        //Prévient de l'instanciation
    }

    public static ItemHomeRepository getInstance(){
        if (instance == null){
            instance = new ItemHomeRepository();
        }
        return instance;
    }
    /*
    public static class Singleton {

        private static Singleton singleton;
        DatabaseReference myRef;
        ArrayList<ItemHomeModel> listItem;
        //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("itemsHomeFragment");
        //ArrayList<ItemHomeModel> listItem = new ArrayList<>();

        private Singleton(){
            myRef = FirebaseDatabase.getInstance().getReference("itemsHomeFragment");
            listItem = new ArrayList<>();
        }

        public static Singleton get() {
            if (singleton == null) {
                singleton = new Singleton();
            }
            return singleton;
        }

        public DatabaseReference getMyRef() {
            return myRef;
        }

        public ArrayList<ItemHomeModel> getListItem() {
            return listItem;
        }
    }

     */

    //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("itemsHomeFragment");


    public ArrayList<ItemHomeModel> getListItem() {
        //Singleton singleton = new Singleton();
        return listItem;
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
                listItem.clear();

                //Récolter la liste
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ItemHomeModel item = ds.getValue(ItemHomeModel.class);

                    //Vérifier que la plante n'est pas nulle
                    if (item != null){
                        //ajouter l'item à notre liste
                        listItem.add(item);
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
}


