package fr.tiavn.grst_services.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopServicesItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = 20;
    }
}
