package com.app.animalsshelter.content.get_list_animals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class ShowOneAnimalFragment extends BaseFragment {
    private final String BUNDLE_KEY_ANIMAL = "ANIMAL";

    ImageView imageViewSolo;
    TextView textViewPK;
    TextView textViewSpecies;
    TextView textViewName;
    TextView textViewBreed;
    TextView textViewGender;
    TextView textViewAge;
    TextView textViewWeight;
    TextView textViewSterilize;
    TextView textViewDescription;
    ProgressBar progressBar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_show_one_animal, container, false);
        getIDs(root);

        putData();
        return root;
    }

    private void putData() {
        Bundle bundle = getArguments();
        Animal animal = (Animal) bundle.getSerializable(BUNDLE_KEY_ANIMAL);

        textViewPK.setText(animal.getPk());
        textViewPK.setVisibility(View.GONE);

        textViewName.setText(animal.getName());
        textViewSpecies.setText(animal.getSpecies());
        textViewBreed.setText(animal.getBreed());
        textViewGender.setText(animal.getGender() + "");
        textViewAge.setText(animal.getAge());
        textViewWeight.setText(animal.getWeight() + "кг");
        textViewSterilize.setText(animal.getSterilize());
        textViewDescription.setText(animal.getDescription());
        progressBar.setVisibility(View.VISIBLE);


        Picasso.with(getActivity())
                .load(animal.getImageURL())
                .placeholder(R.drawable.placeholder)
                .noFade()
                .fit()
                .centerInside()
                .error(R.drawable.missing).memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageViewSolo, new ImageLoadedCallback(progressBar) {
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

    private void getIDs(View root) {
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        imageViewSolo = (ImageView) root.findViewById(R.id.imageViewSolo);
        textViewPK = (TextView) root.findViewById(R.id.textViewPK);
        textViewSpecies = (TextView) root.findViewById(R.id.textViewSpecies);
        textViewName = (TextView) root.findViewById(R.id.textViewName);
        textViewBreed = (TextView) root.findViewById(R.id.textViewBreed);
        textViewGender = (TextView) root.findViewById(R.id.textViewGender);
        textViewAge = (TextView) root.findViewById(R.id.textViewAge);
        textViewWeight = (TextView) root.findViewById(R.id.textViewWeight);
        textViewSterilize = (TextView) root.findViewById(R.id.textViewSterilize);
        textViewDescription = (TextView) root.findViewById(R.id.textViewDescription);
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
