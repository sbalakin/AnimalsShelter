package com.app.animalsshelter.content.get_list_lost_animals;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.animalsshelter.BaseFragment;
import com.app.animalsshelter.R;
import com.app.animalsshelter.content.send_lost_animal.GPSTracker;
import com.app.animalsshelter.model.Animal;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class GoogleMapLostAnimalsFragment extends BaseFragment {
    private final String BUNDLE_KEY_LIST_ANIMALS = "LIST_ANIMALS";

    GoogleMap map;
    ProgressDialog myProgress;
    Spinner spinner_maps_type;

    ArrayAdapter<String> adapterMap;
    ArrayList<Animal> listAnimals = new ArrayList<>();

    MyInfoWindowAdapter infoWindowAdapter;

    public HashMap<String, Animal> listMarkers = new HashMap<String, Animal>();


    public HashMap<String, Animal> getListMarkers() {
        return listMarkers;
    }

    private static View view;
    private static boolean viewMapFirstTime = true;
    //prevent google map automatically regenerate new markers ids when view map after 1st time
    private static HashMap<String, Animal> listMarkersTemp = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.activity_google_map_lost_animals, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
            viewMapFirstTime = false;
        }

        /*View view = inflater.inflate(R.layout.activity_google_map_lost_animals, container, false);*/
        getIDs(view);
        Bundle bundle = getArguments();
        listAnimals = (ArrayList) bundle.getSerializable(BUNDLE_KEY_LIST_ANIMALS);

        showMap();

        setEvents();
        return view;
    }


    private void getIDs(View view) {
        spinner_maps_type = (Spinner) view.findViewById(R.id.spinner_map_type2);
        Resources res = GoogleMapLostAnimalsFragment.this.getResources();

        String[] arrMap = res.getStringArray(R.array.maps_type);
        adapterMap = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrMap);
        adapterMap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_maps_type.setAdapter(adapterMap);
        spinner_maps_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int type = GoogleMap.MAP_TYPE_NORMAL;
                switch (position) {

                    case 0:
                        type = GoogleMap.MAP_TYPE_NORMAL;
                        break;
                    case 1:
                        type = GoogleMap.MAP_TYPE_HYBRID;
                        break;

                }
                map.setMapType(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setEvents() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                marker.showInfoWindow();
                //ShowInfoWindow again when Picasso load full image... temp solution
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Projection projection = map.getProjection();
                        Point markerScreenPosition = projection.toScreenLocation(marker.getPosition());
                        Point pointHalfScreenAbove = new Point(markerScreenPosition.x, (int) (markerScreenPosition.y - 200));
                        LatLng aboveMarkerLatLng = projection.fromScreenLocation(pointHalfScreenAbove);
                        CameraUpdate center = CameraUpdateFactory.newLatLng(aboveMarkerLatLng);
                        map.moveCamera(center);
                        map.animateCamera(center);
                        marker.showInfoWindow();
                    }
                }, 200);


                Toast.makeText(getActivity(), getResources().getString(R.string.get_list_animals_clicked_marker), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void showMap() {
        myProgress = new ProgressDialog(getActivity());
        myProgress.setTitle(getResources().getString(R.string.get_list_animals_loading_map));
        myProgress.setMessage(getResources().getString(R.string.get_list_animals_please_wait));
        myProgress.setCancelable(true);
        myProgress.show();
        map = ((MapFragment) getFragmentManager().
                findFragmentById(R.id.map_lost_animals)).getMap();
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myProgress.dismiss();
            }
        });
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);
        infoWindowAdapter = new MyInfoWindowAdapter(this);
        map.setInfoWindowAdapter(infoWindowAdapter);
        /*//move Camera to Barnaul
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BARNAUL, 15));
        map.addMarker(new MarkerOptions().position(BARNAUL).title("Barnaul").snippet("My Barnaul"));*/

        //ADD MARKERS OF LOST ANIMALS
        if (viewMapFirstTime) {
            for (Animal animal : listAnimals) {
                if (!animal.getLatitude().isEmpty() && !animal.getLongitude().isEmpty()) {
                    if (!animal.getLatitude().equals(getResources().getString(R.string.get_list_animals_null)) && !animal.getLongitude().equals(getResources().getString(R.string.get_list_animals_null))) {
                        double latitude = Double.parseDouble(animal.getLatitude().toString());
                        double longitude = Double.parseDouble(animal.getLongitude().toString());
                        LatLng location = new LatLng(latitude, longitude);
                        BitmapDescriptor icon;
                        if (animal.getSpecies().equals(getResources().getString(R.string.get_list_animals_dog)))
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.dog);
                        else if (animal.getSpecies().equals(getResources().getString(R.string.get_list_animals_cat)))
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.cat);
                        else {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.alien);
                        }
                        MarkerOptions markerOptions = new MarkerOptions().position(location).title(animal.getSpecies()).snippet(getResources().getString(R.string.get_list_animals_dog)).icon(icon);

                        Marker marker = map.addMarker(markerOptions);

                        listMarkers.put(marker.getId(), animal);
                        listMarkersTemp = listMarkers;

                    }
                }
            }
        } else {
            listMarkers = listMarkersTemp;
        }


        // GPSTracker class
        GPSTracker gps = new GPSTracker(getActivity());
        // Check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            LatLng currentLocation = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            //map.addMarker(new MarkerOptions().position(currentLocation).title("Location").snippet("Your Current Location"));
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }

    }

}
