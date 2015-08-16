package com.app.animalsshelter.content.send_lost_animal;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.animalsshelter.BaseFragment;
import com.app.animalsshelter.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/**
 * Created by DAT on 7/24/2015.
 */
public class GoogleMapTrackLostAnimalFragment extends BaseFragment {

    static final LatLng BARNAUL = new LatLng(53.3547792, 83.7697832);
    static final LatLng HANOI = new LatLng(21.0277644, 105.8341598);
    GoogleMap map;
    ProgressDialog myProgress;
    Spinner spinner_maps_type;

    ArrayAdapter<String> adapterMap;

    EditText editTextAddress, editTextLatLng;

    Button accept, cancel;

    private String latitudeToSend;
    private String longitudeToSend;

    private static View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.activity_google_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */

        }

        getIDs(view);
        showMap();
        setEvents();
        return view;
    }


    private void getIDs(View view) {
        accept = (Button) view.findViewById(R.id.buttonAcceptAddress);
        cancel = (Button) view.findViewById(R.id.buttonCancelMap);
        editTextAddress = (EditText) view.findViewById(R.id.editTextAddressInMap);
        editTextLatLng = (EditText) view.findViewById(R.id.editTextLatLng);
        spinner_maps_type = (Spinner) view.findViewById(R.id.spinner_map_type);
        Resources res = GoogleMapTrackLostAnimalFragment.this.getResources();

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
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                map.clear();

                // Animating to the touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                map.addMarker(markerOptions);
                locationName(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressResult = editTextAddress.getText() + "";
                if (!addressResult.isEmpty()) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(SendLostAnimalFragment.KEY_ADDRESS, addressResult);
                    bundle.putString(SendLostAnimalFragment.KEY_LAT, latitudeToSend);
                    bundle.putString(SendLostAnimalFragment.KEY_LNG, longitudeToSend);
                    intent.putExtra(SendLostAnimalFragment.BUNDLE_CODE, bundle);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    Toast.makeText(getActivity(), "Пожалуйста выбрать адрес на карте", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showMap() {
        myProgress = new ProgressDialog(getActivity());
        myProgress.setTitle("Loading map ...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(true);
        myProgress.show();
        map = ((MapFragment) getFragmentManager().
                findFragmentById(R.id.map)).getMap();
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myProgress.dismiss();
            }
        });
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);

        /*//move Camera to Barnaul
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BARNAUL, 15));
        map.addMarker(new MarkerOptions().position(BARNAUL).title("Barnaul").snippet("My Barnaul"));*/

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

    public void locationName(double lat, double lng) {
        try {
            Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lat, lng, 1);
            if (addresses.isEmpty()) {
                //Toast.makeText(MainActivity.this, "Waiting for Location" + "lat-long:" + lat + "-" + lng, Toast.LENGTH_SHORT).show();
            } else {
                if (addresses.size() > 0) {
                    String fullAddress = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                    //Toast.makeText(GoogleMapActivity.this, "Address: " + fullAddress, Toast.LENGTH_SHORT).show();
                    editTextAddress.setText(fullAddress);
                    editTextLatLng.setText(lat + "-" + lng);
                    latitudeToSend = lat + "";
                    longitudeToSend = lng + "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
    }
}
