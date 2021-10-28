package com.example.moviedb.view.fragments;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviedb.R;
import com.example.moviedb.helper.Const;
import com.example.moviedb.model.Movies;
import com.example.moviedb.view.activities.MovieDetailsActivity;
import com.example.moviedb.viewmodel.MovieViewModel;
import com.google.android.material.snackbar.Snackbar;


public class MovieDetailsFragment extends Fragment {


    public MovieDetailsFragment() {
        // Required empty public constructor
    }


    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    private TextView lbl_movie_movieTitle, lbl_movie_movieRating,
            lbl_movie_movieGenre, lbl_movie_movieDesc, lbl_movie_ProductComp,
            lbl_movie_release_date, lbl_movie_popularity;
    private String movie_id = "", movieGenre = "";
    private MovieViewModel viewModel;
    private ImageView img_poster;
    private ImageView  img_backdrop;
    private LinearLayout linearlayout_prodComp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movie_id = getArguments().getString("movieId");

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie_details, container, false);

        lbl_movie_movieTitle = view.findViewById(R.id.lbl_movie_movieTitle_fragment);
        lbl_movie_movieDesc = view.findViewById(R.id.lbl_movie_movieDesc_fragment);
        lbl_movie_movieRating = view.findViewById(R.id.lbl_movie_movieRating_fragment);
        lbl_movie_movieGenre = view.findViewById(R.id.lbl_movie_movieGenre_fragment);
        lbl_movie_ProductComp = view.findViewById(R.id.lbl_movie_productComp_fragment);
        lbl_movie_popularity = view.findViewById(R.id.lbl_movie_popularity_fragment);
        lbl_movie_release_date = view.findViewById(R.id.lbl_movie_release_date_fragment);
        img_backdrop = view.findViewById(R.id.img_backdrop_fragment);
        img_poster = view.findViewById(R.id.img_poster_movieDetails_fragment);
        linearlayout_prodComp = view.findViewById(R.id.linearlayout_prodComp);



        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);

        viewModel.getMovieById(movie_id);
        viewModel.getResultGetMovieById().observe( getActivity(), showResultMovie);

        return view;
    }

    private final Observer<Movies> showResultMovie = new Observer<Movies>() {
        @Override
        public void onChanged(Movies movies) {
            String img_path = Const.IMG_URL + movies.getPoster_path().toString();
            String backdrop_path = Const.IMG_URL +  movies.getBackdrop_path();
            Glide.with(MovieDetailsFragment.this).load(backdrop_path).into(img_backdrop);
            Glide.with(MovieDetailsFragment.this).load(img_path).into(img_poster);// nampilin gambar
            for (int i = 0; i < movies.getGenres().size(); i++) {
                if(i == movies.getGenres().size()-1){
                    movieGenre+=movies.getGenres().get(i).getName();
                }else{
                    movieGenre+=movies.getGenres().get(i).getName()+", ";
                }
            }

            lbl_movie_movieTitle.setText(movies.getTitle());
            lbl_movie_movieDesc.setText(movies.getOverview());//nampilin judul
            lbl_movie_movieGenre.setText(movieGenre); //nampilin genre
            lbl_movie_movieRating.setText("Rating: " + "" + movies.getVote_average() + "(" + movies.getVote_count() + ")"); //nampilin rating
            lbl_movie_popularity.setText("Popularity: " + movies.getPopularity());
            lbl_movie_ProductComp.setText(""+movies.getProduction_companies());
            lbl_movie_release_date.setText("Realease Date: " + movies.getRelease_date());

            for (int i = 0; i < movies.getProduction_companies().size(); i++) {
                ImageView img_movie_prodComp = new ImageView(linearlayout_prodComp.getContext());
                String prodLogo = Const.IMG_URL + movies.getProduction_companies().get(i).getLogo_path();
                String prodName = movies.getProduction_companies().get(i).getName();
                if (movies.getProduction_companies().get(i).getLogo_path() == null) {
                    img_movie_prodComp.setImageDrawable(getResources().getDrawable(R.drawable.company_production, getActivity().getTheme()));
                }else if (prodLogo != "https://image.tmdb.org/3/t/p/w500/null") {
                    Glide.with(getActivity()) .load(prodLogo).into(img_movie_prodComp);
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(250, 250);
                lp.setMargins(20,0,20,0);
                img_movie_prodComp.setLayoutParams(lp);
                linearlayout_prodComp.addView(img_movie_prodComp);
                img_movie_prodComp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar = Snackbar.make(view, prodName, Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(R.id.bottom_nav_main_menu);
                        snackbar.show();
                    }
                });
            }

        }

    };

}