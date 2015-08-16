package com.app.animalsshelter.content.send_lost_animal;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.animalsshelter.BaseFragment;
import com.app.animalsshelter.R;
import com.app.animalsshelter.camera.AlbumStorageDirFactory;
import com.app.animalsshelter.camera.BaseAlbumDirFactory;
import com.app.animalsshelter.internet.ServerRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendLostAnimalFragment extends BaseFragment {

    Spinner spinnerSpecies2;
    EditText editTextDescription2, editTextPhone2, editTextDate;
    EditText editTextAddress;

    ImageButton btnCapture2, btnUpload2, btnBrowse2, btnOpenMap;
    public ImageView imageView2;
    public LinearLayout linearLayoutBtnUpload2;

    String picturePath;
    private Bitmap mImageBitmap; //Bitmap for Sending to Server

    static final int REQUEST_TAKE_PHOTO = 999;
    static final int REQUEST_BROWSE_PHOTO = 1000;
    String mCurrentPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final String TAG_SPECIES = "species";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_DATE = "date";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";

    private String animalSpecies, animalDescription, phoneNumber, location, date;

    Map<String, String> informationAnimal = new HashMap<>();

    public static final int RESULT_LOCATION = 119;
    public static final int REQUEST_ADDRESS_FROM_MAP = 120;
    public static final String KEY_ADDRESS = "result";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LNG = "longitude";

    private String tempAddress;
    private String latitudeToSend;
    private String longitudeToSend;

    public void setLocation(String location, String latitude, String longitude) {
        /*String address = location;
        latitudeToSend = latitude;
        longitudeToSend = longitude;*/
        editTextAddress.setText(location + "(" + latitude + "-" + longitude + ")");
    }

    /*public void setLocation(String location, String latitude, String longitude) {
        String address = location;
        latitudeToSend = latitude;
        longitudeToSend = longitude;
        editTextAddress.setText(address *//* + "(" + latitudeToSend + "-" + longitudeToSend + ")"*//*);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_send_lost_animal, container, false);
        getIDs(root);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tempAddress != null && !tempAddress.isEmpty()) {
            Toast.makeText(getActivity(), tempAddress + "(" + latitudeToSend + "-" + longitudeToSend + ")", Toast.LENGTH_SHORT).show();
            editTextAddress.setText(tempAddress /*+ "(" + latitudeToSend + "-" + longitudeToSend + ")"*/);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        addEvents();
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
    }

    private void getIDs(View root) {
        btnCapture2 = (ImageButton) root.findViewById(R.id.btnCapture2);
        btnUpload2 = (ImageButton) root.findViewById(R.id.btnUpload2);
        imageView2 = (ImageView) root.findViewById(R.id.Imageprev2);
        btnBrowse2 = (ImageButton) root.findViewById(R.id.btnBrowse2);
        linearLayoutBtnUpload2 = (LinearLayout) root.findViewById(R.id.layoutforImgBtnUpload2);
        linearLayoutBtnUpload2.setVisibility(View.INVISIBLE);


        editTextDescription2 = (EditText) root.findViewById(R.id.editTextDescription2);
        editTextPhone2 = (EditText) root.findViewById(R.id.editTextPhone2);
        editTextDate = (EditText) root.findViewById(R.id.editTextDate);
        spinnerSpecies2 = (Spinner) root.findViewById(R.id.spinnerSpecies2);

        btnOpenMap = (ImageButton) root.findViewById(R.id.imageButtonOpenMap);

        editTextAddress = (EditText) root.findViewById(R.id.editTextAddress);
    }

    public void addEvents() {
        btnCapture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();
            }
        });
        btnUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextPhone2.getText().toString().isEmpty())
                    uploadPictureToServer();
                else {
                    Toast.makeText(getActivity(), "Пожалуйста ввести номер телефона!", Toast.LENGTH_SHORT).show();
                    editTextPhone2.requestFocus();
                }
            }
        });

        btnBrowse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processBrowsePicture();
            }
        });


        spinnerSpecies2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                /*Toast.makeText(getBaseContext(), spinnerSpecies.getSelectedItem().toString() + "Position:" + position,
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
                startActivityForResult(intent, REQUEST_ADDRESS_FROM_MAP);

                Bundle bundle = new Bundle();
                bundle.putSerializable("LIST_ANIMALS", listLostAnimal);*/

                BaseFragment fragment = new GoogleMapTrackLostAnimalFragment();
                fragment.setTargetFragment(SendLostAnimalFragment.this, REQUEST_ADDRESS_FROM_MAP);

                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    public static int FRAGMENT_CODE = 999;
    public static String BUNDLE_CODE = "data";

    /**
     * Encode image and send to Server
     */
    private void uploadPictureToServer() {
        Log.e("path", "----------------" + picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        //selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

        Log.e("base64", "-----" + ba1);

        informationAnimal.put("image", ba1);

        createInformationToSend();
        //Toast.makeText(SendAnimal.this, phoneNumber + "", Toast.LENGTH_SHORT).show();
        ServerRequest serverRequest = new ServerRequest();
        serverRequest.uploadingToServerLostAnimal(this, informationAnimal);
    }

    private void createInformationToSend() {
        phoneNumber = editTextPhone2.getText() + "";

        date = editTextDate.getText() + "";
        location = editTextAddress.getText() + "";
        animalSpecies = spinnerSpecies2.getSelectedItem().toString();

        animalDescription = editTextDescription2.getText() + "";


        informationAnimal.put(TAG_DATE, date + "");
        informationAnimal.put(TAG_LOCATION, location + "");
        informationAnimal.put(TAG_SPECIES, animalSpecies + "");
        informationAnimal.put(TAG_DESCRIPTION, animalDescription);
        informationAnimal.put(TAG_PHONE, phoneNumber + "");
        informationAnimal.put(TAG_LATITUDE, latitudeToSend + "");
        informationAnimal.put(TAG_LONGITUDE, longitudeToSend + "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(SendAnimal.this, "requestCode:" + resultCode + "resultCode:" + resultCode + "", Toast.LENGTH_SHORT).show();


        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (mCurrentPhotoPath != null) {
                        setPic();
                        galleryAddPic();  //add image to Android Gallery
                        mCurrentPhotoPath = null;
                        linearLayoutBtnUpload2.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mCurrentPhotoPath != null) {
                        File fileToDelte = new File(mCurrentPhotoPath);
                        fileToDelte.delete();
                        mCurrentPhotoPath = null;

                    }
                }
                break;
            case REQUEST_BROWSE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    Uri selectedimg = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedimg);
                        mImageBitmap = bitmap;
                        imageView2.setImageBitmap(bitmap);

                        linearLayoutBtnUpload2.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_ADDRESS_FROM_MAP:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getBundleExtra(BUNDLE_CODE);
                    tempAddress = bundle.getString(KEY_ADDRESS);
                    latitudeToSend = bundle.getString(KEY_LAT);
                    longitudeToSend = bundle.getString(KEY_LNG);

                    /*editTextAddress.setText(address + "(" + latitudeToSend + "-" + longitudeToSend + ")");*/
                    /*SendLostAnimalFragment fragment = this;*/
                    /*Toast.makeText(getActivity(), address + "(" + latitudeToSend + "-" + longitudeToSend + ")", Toast.LENGTH_SHORT).show();
                    if (this.isAdded()) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.detach(this);
                        transaction.attach(this);
                        transaction.commit();
                    }*/
                    /*setLocation(address, latitudeToSend, longitudeToSend);*/
                }
                break;
        }

    }


    /**
     * Browse image from device
     */
    public void processBrowsePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_BROWSE_PHOTO);
    }


    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = null;

        try {
            f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }


        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    //add Img to Android Gallery
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = imageView2.getWidth();
        int targetH = imageView2.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageBitmap = bitmap;
        /* Associate the Bitmap to the ImageView */
        imageView2.setImageBitmap(bitmap);

        imageView2.setVisibility(View.VISIBLE);

    }


    public void clearAfterSend() {
        editTextPhone2.setText("");
        editTextDescription2.setText("");
        spinnerSpecies2.setSelection(0);
        editTextDate.setText("");
        editTextAddress.setText("");
    }


}
