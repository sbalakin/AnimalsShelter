package com.app.animalsshelter.content.send_animal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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

public class SendAnimalFragment extends BaseFragment {

    Spinner spinnerSpecies;
    Spinner spinnerAge;
    EditText editTextName;
    EditText editTextBreed;
    EditText editTextWeight;
    EditText editTextDescription;
    EditText editTextPhone;
    RadioGroup radioGroupGender;
    CheckBox checkBoxSterilize;

    ImageButton btnCapture;
    ImageButton btnUpload;
    ImageButton btnBrowse;
    public ImageView imageView;
    public LinearLayout linearLayoutBtnUpload;

    CheckBox checkBoxMoreInfo;    //add more information about animal
    ScrollView scrollViewMoreInfo; //view to add more information

    String picturePath;

    private Bitmap mImageBitmap; //Bitmap for Sending to Server

    static final int REQUEST_TAKE_PHOTO = 999;
    static final int REQUEST_BROWSE_PHOTO = 1000;

    String mCurrentPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final String TAG_SPECIES = "species";
    private static final String TAG_NAME = "name";
    private static final String TAG_BREED = "breed";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_AGE = "age";
    private static final String TAG_STERILIZE = "sterilize";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PHONE = "phone";

    private String animalSpecies;
    private String animalName;
    private String animalBreed;
    private String animalDescription;
    private String phoneNumber;
    private String animalWeight;
    private int animalAge, animalGender, animalSterilize;

    Map<String, String> informationAnimal = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_send_animal, container, false);
        btnCapture = (ImageButton) root.findViewById(R.id.btnCapture);
        btnUpload = (ImageButton) root.findViewById(R.id.btnUpload);
        imageView = (ImageView) root.findViewById(R.id.Imageprev1);
        btnBrowse = (ImageButton) root.findViewById(R.id.btnBrowse);
        linearLayoutBtnUpload = (LinearLayout) root.findViewById(R.id.layoutforImgBtnUpload);
        linearLayoutBtnUpload.setVisibility(View.INVISIBLE);

        checkBoxMoreInfo = (CheckBox) root.findViewById(R.id.checkBoxMoreInformation);

        scrollViewMoreInfo = (ScrollView) root.findViewById(R.id.scrollViewMoreInfo);
        scrollViewMoreInfo.setVisibility(View.INVISIBLE);

        editTextName = (EditText) root.findViewById(R.id.editTextName);
        editTextBreed = (EditText) root.findViewById(R.id.editTextBreed);
        editTextWeight = (EditText) root.findViewById(R.id.editTextWeight);
        editTextDescription = (EditText) root.findViewById(R.id.editTextDescription);
        editTextPhone = (EditText) root.findViewById(R.id.editTextPhone);

        spinnerSpecies = (Spinner) root.findViewById(R.id.spinnerSpecies);
        spinnerAge = (Spinner) root.findViewById(R.id.spinnerAge);

        radioGroupGender = (RadioGroup) root.findViewById(R.id.radioGroupGender);
        checkBoxSterilize = (CheckBox) root.findViewById(R.id.checkBoxSterilize);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addEvents();
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
    }

    public void addEvents() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        REQUEST_TAKE_PHOTO);
                //dispatchTakePictureIntent();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextPhone.getText().toString().isEmpty())
                    uploadPictureToServer();
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.send_animal_enter_phone_number), Toast.LENGTH_SHORT).show();
                    editTextPhone.requestFocus();
                }
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processBrowsePicture();
            }
        });


        final Animation animationExpand = AnimationUtils.loadAnimation(getActivity(), R.anim.expand_more_infomation);
        final Animation animationCollapse = AnimationUtils.loadAnimation(getActivity(), R.anim.collapse_more_information);

        checkBoxMoreInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    scrollViewMoreInfo.clearAnimation();
                    scrollViewMoreInfo.startAnimation(animationExpand);
                    scrollViewMoreInfo.setVisibility(View.VISIBLE);
                } else {
                    scrollViewMoreInfo.clearAnimation();
                    scrollViewMoreInfo.startAnimation(animationCollapse);
                    scrollViewMoreInfo.setVisibility(View.INVISIBLE);
                }
            }
        });

        spinnerSpecies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                /*Toast.makeText(getBaseContext(), spinnerSpecies.getSelectedItem().toString() + "Position:" + position,
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                /*Toast.makeText(getBaseContext(), spinnerAge.getSelectedItem().toString() + "Position:" + position,
                        Toast.LENGTH_SHORT).show();*/
                //animalAge = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int memberID) {
                /*if (memberID == R.id.radioButtonMale) {
                    //Toast.makeText(MainActivity.this, "Male", Toast.LENGTH_SHORT).show();
                    animalGender = 1;
                } else if (memberID == R.id.radioButtonFemale) {
                    //Toast.makeText(MainActivity.this, "Female", Toast.LENGTH_SHORT).show();
                    animalGender = 2;
                }*/
            }
        });

        checkBoxSterilize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                /*if (checked) {
                    //Toast.makeText(MainActivity.this, "Sterilized", Toast.LENGTH_SHORT).show();
                    animalSterilize = 1;
                } else {
                    animalSterilize = 2;
                }*/
            }
        });
    }


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
        serverRequest.uploadingToServer(this, informationAnimal);
    }

    private void createInformationToSend() {
        phoneNumber = editTextPhone.getText() + "";

        if (checkBoxMoreInfo.isChecked()) {
            animalSpecies = spinnerSpecies.getSelectedItem().toString();
            animalName = editTextName.getText() + "";
            animalBreed = editTextBreed.getText() + "";
            if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale) {
                animalGender = 1;
            } else if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonFemale) {
                animalGender = 2;
            }
            animalAge = spinnerAge.getSelectedItemPosition() + 1;
            animalWeight = editTextWeight.getText() + "";
            if (animalWeight.isEmpty()) {
                animalWeight = "0";
            }
            if (checkBoxSterilize.isChecked()) {
                animalSterilize = 1;
            } else if (!checkBoxSterilize.isChecked()) {
                animalSterilize = 2;
            }
            animalDescription = editTextDescription.getText() + "";


        } else {
            animalSpecies = "Прочие";
            animalName = "";
            animalBreed = "";
            animalGender = 0;
            animalAge = 0;
            animalWeight = "0";
            animalSterilize = 0;
            animalDescription = "";


        }
        informationAnimal.put(TAG_SPECIES, animalSpecies + "");
        informationAnimal.put(TAG_NAME, animalName);
        informationAnimal.put(TAG_BREED, animalBreed);
        informationAnimal.put(TAG_GENDER, animalGender + "");
        informationAnimal.put(TAG_AGE, animalAge + "");
        informationAnimal.put(TAG_WEIGHT, animalWeight + "");
        informationAnimal.put(TAG_STERILIZE, animalSterilize + "");
        informationAnimal.put(TAG_DESCRIPTION, animalDescription);
        informationAnimal.put(TAG_PHONE, phoneNumber + "");
    }

    public void clearAfterSend() {
        editTextPhone.setText("");
        editTextName.setText("");
        editTextBreed.setText("");
        editTextWeight.setText("");
        editTextDescription.setText("");
        checkBoxMoreInfo.setChecked(false);
        checkBoxSterilize.setChecked(false);
        radioGroupGender.clearCheck();
        spinnerAge.setSelection(0);
        spinnerSpecies.setSelection(0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity(), "Пожалуйста ввести номер телефона!", Toast.LENGTH_SHORT).show();

        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                            byteArray.length);

                    mImageBitmap = bitmap;
                    imageView.setImageBitmap(bitmap);
                    linearLayoutBtnUpload.setVisibility(View.VISIBLE);
                }
                break;
            case REQUEST_BROWSE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    Uri selectedImg = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImg);
                        mImageBitmap = bitmap;
                        imageView.setImageBitmap(bitmap);

                        linearLayoutBtnUpload.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

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
        imageView.setImageBitmap(bitmap);

        imageView.setVisibility(View.VISIBLE);

    }

}
