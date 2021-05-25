package fr.tiavn.grst_services.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import fr.tiavn.grst_services.R;

public class ContenuIndisponible extends Fragment {
    Context context;

    public ContenuIndisponible(){
    }

    public ContenuIndisponible(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contenue_indisponible_utilisateur, container, false);

        TextView devenir_fournisseur = (TextView) view.findViewById(R.id.text_devenir_fournisseur);
        devenir_fournisseur.setClickable(true);
        devenir_fournisseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
