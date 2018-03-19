package com.example.baeza.popularmoviesapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.R;

/**
 * Created by baeza on 16.03.2018.
 */


public class OverviewFragment extends android.support.v4.app.Fragment implements OnClickListener {

    public final static String TITLE_KEY = "title_key";
    public final static String OVERVIEW = "overview_key";
    public final static String RUNTIME = "runtime_key";
    public final static String VOTE_AVERAGE = "vote_average";
    public final static String RELEASE_DATE = "release_date";

    private static final String TAG = "OverviewFragment";


    ImageButton favoriteButton;
    TextView tv_overview;
    TextView tv_runtime;
    TextView tv_voteAverage;
    TextView tv_year;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.overview_fragment, container, false);
        initLayoutComponents(view);

        String dummyText = "\n"+ "gdasghdasdaudiasjdas dhiashdas dasindjasd "+
                "\n" + "hdauisdh8a yhdashdias hdashdasds nkasdkasd  dbhjaisdhias oadjoasd" +
                "dnjasoidjas jdoas das jdoasjdas d doasjdoas dasd jdoasd ad" +
                "jodasd asdjoas djoaisdj asd jodasjdoad ojdasodjas jodasjd";

        String runtime = getArguments().getString(RUNTIME);
        String year = getArguments().getString(RELEASE_DATE);
        String overview = getArguments().getString(OVERVIEW);
        String voteAverage = getArguments().getString(VOTE_AVERAGE);

        tv_voteAverage.setText(voteAverage);
        tv_runtime.setText(runtime);
        tv_year.setText(year);
        tv_overview.setText(overview +dummyText);

        return view;
    }

    public void initLayoutComponents(View view) {
        favoriteButton = view.findViewById(R.id.btn_mark_favorite);
        tv_overview = view.findViewById(R.id.overview);
        tv_runtime = view.findViewById(R.id.runtime);
        tv_voteAverage = view.findViewById(R.id.voteAverage);
        tv_year = view.findViewById(R.id.year);
        favoriteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mark_favorite: {
                Log.d(TAG, "added");
                Snackbar.make(view, "Added", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "added", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
