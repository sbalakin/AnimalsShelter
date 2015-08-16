package com.app.animalsshelter.content.get_list_lost_animals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.animalsshelter.BaseFragment;
import com.app.animalsshelter.R;
import com.app.animalsshelter.model.Animal;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class ShowOneLostAnimalFragment extends BaseFragment {
    private final String BUNDLE_KEY_LOST_ANIMAL = "LOST_ANIMAL";

    ImageView imageViewSolo2;
    TextView textViewPK2, textViewSpecies2, textViewDate, textViewLocation, textViewDescription;
    ProgressBar progressBar2 = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_show_one_lost_animal, container, false);
        getIDs(view);

        putData();
        return view;
    }

    private void getIDs(View view) {
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        imageViewSolo2 = (ImageView) view.findViewById(R.id.imageViewSolo);
        textViewPK2 = (TextView) view.findViewById(R.id.textViewPK);
        textViewSpecies2 = (TextView) view.findViewById(R.id.textViewSpecies);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewLocation = (TextView) view.findViewById(R.id.textViewLocation);
        textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
    }

    private void putData() {
        Bundle bundle = getArguments();
        Animal animal = (Animal) bundle.getSerializable(BUNDLE_KEY_LOST_ANIMAL);

        textViewPK2.setText(animal.getPk());
        textViewPK2.setVisibility(View.GONE);

        textViewSpecies2.setText(animal.getSpecies());
        textViewDate.setText(animal.getLast_date_seen());
        textViewLocation.setText(animal.getLast_location());
        textViewDescription.setText(animal.getDescription());
        progressBar2.setVisibility(View.VISIBLE);


        Picasso.with(getActivity())
                .load(animal.getImageURL())
                .placeholder(R.drawable.placeholder)
                .noFade()
                .fit()
                .centerInside()
                .error(R.drawable.missing).memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageViewSolo2, new ImageLoadedCallback(progressBar2) {
                    @Override
                    public void onSuccess() {

                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }


                });
    }

    class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

}
