package com.app.animalsshelter.content.get_list_animals;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.app.animalsshelter.BaseFragment;
import com.app.animalsshelter.R;
import com.app.animalsshelter.internet.ServerRequest;
import com.app.animalsshelter.model.Animal;

import java.util.ArrayList;

public class GetListAnimalsFragment extends BaseFragment {
    private final String BUNDLE_KEY_ANIMAL = "ANIMAL";
    public static Spinner spinnerFilterSpecies;
    public static Spinner spinnerFilterGender;
    public static Spinner spinnerFilterAge;
    public static boolean searchOn = false;
    public static String filterSpecies;
    public static String filterGender;
    public static String filterAge;
    public CustomAdapterGridViewListAnimals customAdapterGridViewListAnimals = null;
    private GridView gridview;
    private EditText editTextSearch;
    private ArrayList<Animal> listAnimal = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_get_list_animals, container, false);
        getID(root);
        setEvents();

        if (listAnimal.size() == 0) {
            ServerRequest serverRequest = new ServerRequest(GetListAnimalsFragment.this);
            serverRequest.downloadingFromServer(root.getContext());
        }
        return root;
    }

    private void getID(View root) {
        gridview = (GridView) root.findViewById(R.id.gridView);
        editTextSearch = (EditText) root.findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                searchOn = true;
                GetListAnimalsFragment.this.customAdapterGridViewListAnimals.getFilter().filter(charSequence);
                spinnerFilterSpecies.setSelection(0);
                spinnerFilterGender.setSelection(0);
                spinnerFilterAge.setSelection(0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spinnerFilterSpecies = (Spinner) root.findViewById(R.id.spinnerFilterSpecies);
        spinnerFilterGender = (Spinner) root.findViewById(R.id.spinnerFilterGender);
        spinnerFilterAge = (Spinner) root.findViewById(R.id.spinnerFilterAge);

    }


    private void setEvents() {
        customAdapterGridViewListAnimals = new CustomAdapterGridViewListAnimals(getActivity(), R.layout.custom_item_gridview, listAnimal);
        gridview.setAdapter(customAdapterGridViewListAnimals);
        Log.e(getResources().getString(R.string.log_listanimal), listAnimal.toString());

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Animal animal = (Animal) adapterView.getItemAtPosition(position);
                Bundle bundle = new Bundle();

                bundle.putSerializable(BUNDLE_KEY_ANIMAL, animal);

                BaseFragment fragment = new ShowOneAnimalFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        spinnerFilterSpecies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterSpecies = spinnerFilterSpecies.getSelectedItem().toString();
                CharSequence charSequence = spinnerFilterSpecies.getSelectedItem().toString();

                GetListAnimalsFragment.this.customAdapterGridViewListAnimals.getFilter().filter(charSequence);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFilterGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterGender = spinnerFilterGender.getSelectedItem().toString();
                CharSequence charSequence = spinnerFilterGender.getSelectedItem().toString();

                GetListAnimalsFragment.this.customAdapterGridViewListAnimals.getFilter().filter(charSequence);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerFilterAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterAge = spinnerFilterAge.getSelectedItem().toString();
                CharSequence charSequence = spinnerFilterAge.getSelectedItem().toString();

                GetListAnimalsFragment.this.customAdapterGridViewListAnimals.getFilter().filter(charSequence);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public ArrayList<Animal> getListAnimal() {
        return listAnimal;
    }
}
