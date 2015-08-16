package com.app.animalsshelter.content.get_list_lost_animals;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import com.app.animalsshelter.BaseFragment;
import com.app.animalsshelter.R;
import com.app.animalsshelter.internet.ServerRequest;
import com.app.animalsshelter.model.Animal;

import java.util.ArrayList;

public class GetListLostAnimalsFragment extends BaseFragment {
    private final String BUNDLE_KEY_LOST_ANIMAL = "LOST_ANIMAL";
    private final String BUNDLE_KEY_LIST_ANIMALS = "LIST_ANIMALS";

    GridView gridview2;
    public CustomAdapterGridviewLostAnimals customAdapterGridviewLostAnimals = null;
    private ArrayList<Animal> listLostAnimal = new ArrayList<>();

    EditText editTextSearch2;

    ImageButton imageButtonOpenMap;


    public static boolean searchOn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_get_list_lost_animals, container, false);
        getIDs(root);
        setEvents();
        if (listLostAnimal.size() == 0) {
            ServerRequest serverRequest = new ServerRequest(GetListLostAnimalsFragment.this);
            serverRequest.downloadingLostAnimalsFromServer(root.getContext());
        }
        return root;
    }

    private void getIDs(View root) {
        imageButtonOpenMap = (ImageButton) root.findViewById(R.id.imageButtonLostAnimalsMap);
        gridview2 = (GridView) root.findViewById(R.id.gridView2);
        editTextSearch2 = (EditText) root.findViewById(R.id.editTextSearch2);
        editTextSearch2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                searchOn = true;
                GetListLostAnimalsFragment.this.customAdapterGridviewLostAnimals.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void setEvents() {
        customAdapterGridviewLostAnimals = new CustomAdapterGridviewLostAnimals(getActivity(), R.layout.custom_item_gridview2, listLostAnimal);
        gridview2.setAdapter(customAdapterGridviewLostAnimals);

        gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Animal animal = (Animal) adapterView.getItemAtPosition(position);

                Bundle bundle = new Bundle();

                bundle.putSerializable(BUNDLE_KEY_LOST_ANIMAL, animal);
                BaseFragment fragment = new ShowOneLostAnimalFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imageButtonOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_KEY_LIST_ANIMALS, listLostAnimal);

                BaseFragment fragment = new GoogleMapLostAnimalsFragment();

                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


    }

    public ArrayList<Animal> getListAnimal() {
        return listLostAnimal;
    }
}
