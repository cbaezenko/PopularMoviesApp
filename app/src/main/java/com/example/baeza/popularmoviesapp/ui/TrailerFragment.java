package com.example.baeza.popularmoviesapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterTrailer;

/**
 * Created by baeza on 16.03.2018.
 */

public class TrailerFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        RVAdapterTrailer rvAdapterTrailer = new RVAdapterTrailer(getContext());

        recyclerView.setAdapter(rvAdapterTrailer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        return recyclerView;
    }
}
